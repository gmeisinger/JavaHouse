package javahouse;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class TileSet
{
    //vars
    private BufferedImage tileSetImage;
    private int tileWidth;
    private int tileHeight;
    private int imgWidth;
    private int imgHeight;
    //gonna try the 2d array thing
    private Tile[][] tiles;
    //constructor
    public TileSet(String filename, int tileWidth, int tileHeight)
    {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        loadImage(filename);
        this.imgWidth = this.tileSetImage.getWidth();
        this.imgHeight = this.tileSetImage.getHeight();
        int rows = this.imgWidth/tileWidth;
        int cols = this.imgHeight/tileHeight;
        this.tiles = new Tile[rows][cols];
        loadTiles();
    }

    /**
     * loads tileset from disk
     */
    public void loadImage(String filename)
    {
        try
        {
            URL imgURL = this.getClass().getResource(filename);
            this.tileSetImage = ImageIO.read(imgURL);
        } catch(IOException e){}
    }
    /**
     * fills the array of Tiles from the sheet image
     */
    public void loadTiles()
    {
        int cols = this.imgWidth/this.tileWidth;
        int rows = this.imgHeight/this.tileHeight;
        for (int r=0;r<rows;r++)
        {
            for (int c=0;c<cols;c++)
            {
                this.tiles[c][r] = new Tile(this.tileSetImage,c*this.tileWidth,r*this.tileHeight,this.tileWidth,this.tileHeight);
            }
        }
    }

    public Tile getTileAt(int row, int col)
    {
        return tiles[col][row];
    }
}