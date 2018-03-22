package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Random;

/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private int points = 0; //how points do we have

    //bitmap of the pacman
    private Bitmap pacBitmap;
    //textview reference to points
    private TextView pointsView;
    private TextView countView;
    private int pacx, pacy;
    private int ghostx, ghosty;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen
    private Bitmap coinBitmap;
    private Bitmap smallCoin;
    private Bitmap ghostBitmap;
    private Bitmap smallGhost;
    Random r = new Random();
    public int coinsTaken = 0;

    public Game(Context context, TextView view, TextView countView)
    {
        this.context = context;
        this.pointsView = view;
        this.countView = countView;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.coin);
        smallCoin = Bitmap.createScaledBitmap(coinBitmap,60,60,false);
        ghostBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ghost);
        smallGhost = Bitmap.createScaledBitmap(ghostBitmap,80,80,false);

    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    //TODO initialize goldcoins also here
    public void newGame()
    {
        getCoins().clear();
        for(int i =0; i<10; i++){
            coins.add(new GoldCoin(false,r.nextInt(1000-1)+1,r.nextInt(1000-1)+1));
        }
        pacx = 50;
        pacy = 400; //just some starting coordinates
        ghostx= 900;
        ghosty= 900;
        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
        countView.setText("Time left:30");
        coinsTaken=0;
        gameView.invalidate(); //redraw screen

    }

    public void newLevel(){
        getCoins().clear();
        for (int i = 0; i < 10; i++) {
            coins.add(new GoldCoin(false, r.nextInt(1000-1)+1, r.nextInt(1000-1)+1));
        }
        pacx = 50;
        pacy = 400;
        ghosty = 900;
        ghostx = 900;
        coinsTaken=0;
        gameView.invalidate();
    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public void movePacmanX(int pixels)
    {
        //still within our boundaries?
        if (pacx+pixels+pacBitmap.getWidth()<w) {
            pacx = pacx + pixels;
            doCollisionCheck();
            doCollisionCoin();
            doDieCollision();
            gameView.invalidate();
        }
    }
    public void movePacmanY(int pixels)
    {
        //still within our boundaries?
        if (pacy+pixels+pacBitmap.getWidth()<w) {
            pacy = pacy + pixels;
            doCollisionCheck();
            doCollisionCoin();
            doDieCollision();
            gameView.invalidate();
        }
    }

    public void moveGhostX(int pixels){
        if(ghostx+pixels+smallGhost.getWidth()<w){
            ghostx = ghostx + pixels;
            doEnemyCollision();
            gameView.invalidate();
        }
    }

    public void moveGhostY(int pixels){
        if(ghosty+pixels+smallGhost.getWidth()<w){
            ghosty = ghosty + pixels;
            doEnemyCollision();
            gameView.invalidate();
        }
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    public void doCollisionCheck()
    {
        if (pacx < 10){
            pacx=10;
        }
        else if (pacy < 10){
            pacy=10;
        }
        else if (pacx > w- pacBitmap.getWidth()){
            pacx = w - pacBitmap.getWidth();
        }
        else if (pacy > h - pacBitmap.getHeight()){
            pacy = h - pacBitmap.getHeight();
        }
    }

    public void doEnemyCollision(){
        if (ghostx < 10){
            ghostx = 10;
        }
        else if (ghosty < 10){
            ghosty = 10;
        } else if (ghostx > w - smallGhost.getWidth()) {
            ghostx = w - smallGhost.getWidth();
        } else if (ghosty > h - smallGhost.getHeight()) {
            ghosty = h - smallGhost.getHeight();
        }
    }

    public void doCollisionCoin() {
        for (GoldCoin coin : getCoins()) {
            double colx = Math.subtractExact(pacx, coin.getX());
            double coly = Math.subtractExact(pacy, coin.getY());
            double xtx = colx * colx;
            double yty = coly * coly;
            double almost = xtx + yty;
            double distance = Math.sqrt(almost);
            if (distance < 100 && coin.getTak()==false){
                points = getPoints()+5;
                coinsTaken = coinsTaken+1;
                pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
            }
            if (distance < 100) {
                coin.setTak(true);
            }
        }
    }

    public void doDieCollision(){
        double colx = Math.subtractExact(pacx, ghostx);
        double coly = Math.subtractExact(pacy, ghosty);
        double xtx = colx * colx;
        double yty = coly * coly;
        double almost = xtx + yty;
        double distance = Math.sqrt(almost);
        if (distance < 80) {
            newGame();
        }
    }


    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getPoints()
    {
        return points;
    }

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }
    public Bitmap getCoinBitmap(){return smallCoin;}
    public Bitmap getGhostBitmap(){return smallGhost;}
    public int getGhostx(){return ghostx;}
    public int getGhosty(){return ghosty;}

}
