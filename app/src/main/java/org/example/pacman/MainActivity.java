package org.example.pacman;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import org.w3c.dom.Text;

import static android.R.attr.button;

public class MainActivity extends Activity {
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;
    private boolean gyro;

    //reference to the main view
    GameView gameView;
    //reference to the game class.
    Game game;

    private Timer ghostTimer;
    private Timer minTimer;
    public Timer countTimer;
    public int counting;
    private boolean running = false;
    private int direction;
    public int level = 1;
    static TextView lvl;
    static TextView timer;
    static TextView onoff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gyro = false;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscopeSensor == null) {
            Toast.makeText(this, "The device has no gyroscope", Toast.LENGTH_SHORT).show();
        }
        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (gyro) {
                    if (sensorEvent.values[1] > 0.5f) {
                        direction = 1;
                    } else if (sensorEvent.values[1] < -0.5f) {
                        direction = 2;
                    } else if (sensorEvent.values[0] > 0.5f) {
                        direction = 3;
                    } else if (sensorEvent.values[0] < -0.5f) {
                        direction = 4;
                    }

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }


        };

        Button gyroButton = findViewById(R.id.gyro);
        gyroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gyro) {
                    gyro = false;
                    onoff.setText("Off");
                } else
                    gyro = true;
                onoff.setText("On");
            }
        });

        gameView = findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);
        onoff = (TextView) findViewById(R.id.onoff);
        lvl = (TextView) findViewById(R.id.level);
        timer = (TextView) findViewById(R.id.counter);
        counting = 30;
        direction = 0;
        minTimer = new Timer();
        countTimer = new Timer();
        ghostTimer = new Timer();
        running = false;
        minTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 50);
        countTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod2();
            }
        }, 0, 1000);
        ghostTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod3();
            }

        }, 0, 50);
        game = new Game(this,textView,timer);
        game.setGameView(gameView);
        gameView.setGame(game);
        game.newGame();

        Button buttonPause = findViewById(R.id.pause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
            }
        });

        Button buttonContínue = findViewById(R.id.startagain);
        buttonContínue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running == false) {
                    running = true;
                }
            }
        });

        Button buttonNew = findViewById(R.id.newGame);
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.newGame();
                counting = 30;
                level = 1;
                running = false;
                lvl.setText("Level:" + level);
            }
        });

        Button buttonRight = findViewById(R.id.moveRight);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direction = 1;
                running = true;
            }
        });

        Button buttonLeft = findViewById(R.id.moveLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direction = 3;
                running = true;
            }
        });

        Button buttonDown = findViewById(R.id.moveDown);
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direction = 2;
                running = true;
            }
        });

        Button buttonUp = findViewById(R.id.moveUp);
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direction = 4;
                running = true;
            }
        });

        gameView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                direction = 4;
                running = true;
            }


            public void onSwipeBottom() {
                direction = 2;
                running = true;
            }


            public void onSwipeRight() {
                direction = 1;
                running = true;
            }


            public void onSwipeLeft() {
                direction = 3;
                running = true;
            }
        });
    }
        protected void onResume(){
            super.onResume();
            sensorManager.registerListener(gyroscopeEventListener,gyroscopeSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }
        @Override
        protected void onPause(){
            super.onPause();
            sensorManager.unregisterListener(gyroscopeEventListener);
        }

        @Override
        protected void onStop(){
            super.onStop();
            minTimer.cancel();
            countTimer.cancel();
            ghostTimer.cancel();
        }

        private void TimerMethod(){this.runOnUiThread(Timer_Tick);}

        private Runnable Timer_Tick = new Runnable() {
            @Override
            public void run() {
                if (running) {
                    if (direction == 1) {
                        game.movePacmanX(35);
                    }
                    if(direction == 2){
                        game.movePacmanY(35);
                    }
                    if(direction == 3){
                        game.movePacmanX(-35);
                    }
                    if(direction == 4){
                        game.movePacmanY(-35);
                    }
                }
            }
        };

        public void TimeWent(){
            if(counting <= 0){
                direction = 0;
                level = 1;
                counting = 30;
                running = false;
                game.newGame();
                Toast.makeText(this, "Times up!", Toast.LENGTH_LONG).show();
            }
        }

        public void LevelUp(){
            if(game.coinsTaken == 10){
                level = level +1;
                counting = 30;
                lvl.setText("Level: " + level);
                game.newLevel();

            }
        }

        private void TimerMethod2(){this.runOnUiThread(Timer_Points);}
        private Runnable Timer_Points = new Runnable() {
            @Override
            public void run() {
                if(running){
                    counting--;
                    timer.setText("Time left: "+counting);
                    TimeWent();
                    LevelUp();
                }
            }
        };

        public void TimerMethod3(){this.runOnUiThread(Timer_Ghost);}
        private Runnable Timer_Ghost = new Runnable() {
            @Override
            public void run() {
                if(running) {
                    if (level == 1) {
                        if (game.getPacx() > game.getGhostx()) {
                            game.moveGhostX(10);
                        }
                        if (game.getPacx() < game.getGhostx()) {
                            game.moveGhostX(-10);
                        }
                        if (game.getPacy() > game.getGhosty()) {
                            game.moveGhostY(10);
                        }
                        if (game.getPacy() < game.getGhosty()) {
                            game.moveGhostY(-10);
                        }
                        double colx = Math.subtractExact(game.getPacx(), game.getGhostx());
                        double coly = Math.subtractExact(game.getPacy(), game.getGhosty());
                        double xtx = colx * colx;
                        double yty = coly * coly;
                        double almost = xtx + yty;
                        double distance = Math.sqrt(almost);
                        if (distance < 80) {
                            running = false;
                            direction = 0;
                            counting = 30;
                            level = 1;
                            lvl.setText("Level: " + level);
                            game.newGame();
                            dieToast();

                        }
                    }
                }
                if(level > 1) {
                    if (game.getPacx() > game.getGhostx()) {
                        game.moveGhostX(20);
                    }
                    if (game.getPacx() < game.getGhostx()) {
                        game.moveGhostX(-20);
                    }
                    if (game.getPacy() > game.getGhosty()) {
                        game.moveGhostY(20);
                    }
                    if (game.getPacy() < game.getGhosty()) {
                        game.moveGhostY(-20);
                    }
                    double colx = Math.subtractExact(game.getPacx(), game.getGhostx());
                    double coly = Math.subtractExact(game.getPacy(), game.getGhosty());
                    double xtx = colx * colx;
                    double yty = coly * coly;
                    double almost = xtx + yty;
                    double distance = Math.sqrt(almost);
                    if (distance < 80) {
                        running = false;
                        direction = 0;
                        counting = 30;
                        level = 1;
                        lvl.setText("Level: " + level);
                        game.newGame();
                        dieToast();
                    }
                }
            }
        };

    public void dieToast(){Toast.makeText(this,"You were captured!", Toast.LENGTH_LONG).show();}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings clicked",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this,"New Game clicked",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
