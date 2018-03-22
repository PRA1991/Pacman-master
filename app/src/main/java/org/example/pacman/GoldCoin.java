package org.example.pacman;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {
    public boolean taken;
    public int xPos;
    public int yPos;

    public GoldCoin(boolean taken, int xPos, int yPos){
        this.taken = taken;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    Integer getX(){return  xPos;}
    void setX(Integer x){this.xPos = x;}
    Integer getY(){return  yPos;}
    void setY(Integer y){ this.yPos = y;}
    Boolean getTak(){return taken;}
    void setTak(Boolean tak){this.taken = tak;}



}
