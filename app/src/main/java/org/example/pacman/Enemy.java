package org.example.pacman;

/**
 * Created by peter on 21-03-2018.
 */

public class Enemy {
    public int Ex;
    public int Ey;
    public boolean isAlive;

    public Enemy(boolean alive, int xPos, int yPos){
        this.isAlive = alive;
        this.Ex = xPos;
        this.Ey = yPos;
    }

    Integer getEX(){return Ex;}
    void setEX(Integer x){this.Ex = x;}
    Integer getEY(){return Ey;}
    void SetEY(Integer y){this.Ey = y;}





}
