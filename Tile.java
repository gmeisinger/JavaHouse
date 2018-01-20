package JavaHouse;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Tile
{
    //vars
    private ArrayList<BufferedImage> tileImages;
    private HashMap<String,String> desc;

    //constructor for tileset
    public Tile(BufferedImage tileSetImage, int x, int y, int tileWidth, int tileHeight)
    {
        //this.tileSetImage = tileSetImage;
        //this.loadImage();
        this.tileImages = new ArrayList<BufferedImage>();
        this.tileImages.add(tileSetImage.getSubimage(x, y, tileWidth, tileHeight));
    }

    public Tile(Tile t)
    {
        this.tileImages = new ArrayList<BufferedImage>();
        this.tileImages.add(t.getImage());
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

    public void setDesc(HashMap<String,String> desc)
    {
        this.desc = desc;
    }

    //MY GETTERS
    //this returns the base image, useful for combining tiles
    public BufferedImage getImage()
    {
        return this.tileImages.get(0);
    }

    public HashMap<String,String> getDesc()
    {
        return this.desc;
    }

    public boolean has(String attribute)
    {
        if(this.desc.containsKey(attribute))
        {
            return true;
        }
        else return false;
    }

    public String get(String attribute)
    {
        return this.desc.get(attribute);
    }
    
}