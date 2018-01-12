package javahouse;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Tile
{
    //vars
    private ArrayList<BufferedImage> tileImages;
    //private HashMap<String,String> desc;

    //constructor for tileset
    public Tile(BufferedImage tileSetImage, int x, int y, int tileWidth, int tileHeight)
    {
        //this.tileSetImage = tileSetImage;
        //this.loadImage();
        this.tileImages = new ArrayList<BufferedImage>();
        this.tileImages.add(tileSetImage.getSubimage(x, y, tileWidth, tileHeight));
    }

    public void draw(Graphics2D g2d, int x, int y, int width, int height)
    {
        for(BufferedImage tileImage : this.tileImages)
        {
            g2d.drawImage(tileImage, x, y, width, height, null);
        }
    }

    //MY SETTERS
    public void addImage(BufferedImage img)
    {
        this.tileImages.add(img);
    }

    //MY GETTERS
    //this returns the base image, useful for combining tiles
    public BufferedImage getImage()
    {
        return this.tileImages.get(0);
    }
    
}