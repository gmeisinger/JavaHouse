package JavaHouse;

import java.awt.Graphics2D;
import java.awt.Point;

import JavaHouse.Sprite;

public class Item
{
    //vars
    private String name;
    private Point loc;
    private Sprite sprite;

    public Item(int x, int y, String name, Sprite s)
    {
        this.name = name;
        this.loc = new Point(x*Game.TILE_SIZE, y*Game.TILE_SIZE);
        this.sprite = s;
        initialize();
    }


    public void initialize()
    {
        setLoc(this.loc);
    }

    public void draw(Graphics2D g2d)
    {
        this.sprite.draw(g2d);
    }

    //MY SETTERS
    public void setLoc(Point p)
    {
        this.sprite.setSpriteX(p.x);
        this.sprite.setSpriteY(p.y);
    }

    //MY GETTERS
    public String getName()
    {
        return this.name;
    }

    public Point getLoc()
    {
        return this.loc;
    }
}