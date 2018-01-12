package javahouse;

import java.awt.Color;
import java.awt.Graphics2D;

public class MenuBox
{
    //vars
    private int x;
    private int y;
    private int width;
    private int height;
    //private String color;


    public MenuBox(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    //draw the box
    public void draw(Graphics2D g2d)
    {
        //draw rect  
        g2d.setColor(Color.BLACK);
        g2d.fillRect(this.x,this.y, this.width, this.height);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(this.x,this.y, this.width, this.height);
    }


    //MY SETTERS

    //MY GETTERS
}