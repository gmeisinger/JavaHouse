package javahouse;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javahouse.Game;
import javahouse.Tile;

public class Door
{
    //vars
    private boolean locked = false;
    private boolean closed = true;
    private String keyName;
    private BufferedImage img;
    private Point loc;

    public Door(BufferedImage img, int x, int y)
    {
        loc = new Point(x*Game.TILE_SIZE, y*Game.TILE_SIZE);
        this.img = img;
    }

    public void draw(Graphics2D g2d)
    {
        if(this.closed)
            g2d.drawImage(img, this.loc.x, this.loc.y, Game.TILE_SIZE, Game.TILE_SIZE, null);
    }

    //MY SETTERS
    public void setLocked(boolean b)
    {
        this.locked = b;
    }

    public void setClosed(boolean b)
    {
        this.closed = b;
    }

    public void setKeyName(String s)
    {
        this.keyName = s;
    }


    //MY GETTERS
    public boolean isLocked()
    {
        return this.locked;
    }

    public boolean isClosed()
    {
        return this.closed;
    }

    public Point getLoc()
    {
        return this.loc;
    }

    public String getKey()
    {
        return this.keyName;
    }

}