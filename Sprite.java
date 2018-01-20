package JavaHouse;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.awt.Point;

import javax.imageio.ImageIO;

import JavaHouse.Game;

public class Sprite
{
    // set up my instance vars
    //sprite image
    private String fileName;
    private BufferedImage spriteSheet;
    private BufferedImage spriteImage;
    //position vars
    private Point loc;
    private Point target;
    private Point lastSafeLoc;
    //height and width
    private int spriteWidth;
    private int spriteHeight;
    //animation
    //private Animation animation;
    /*NPC and Player attributes*/
    private String facing;
    private String looking;

    //game reference
    //private Game game;
    //private TileMap map;

    /**Construct a complete sprite */
    public Sprite(int x, int y, int spriteSize, String file)
    {
        //position is set with setters
        this.loc = new Point(x,y);
        this.lastSafeLoc = new Point(x,y);
        this.spriteWidth = spriteSize;
        this.spriteHeight = spriteSize;
        //filename
        this.fileName = "JavaHouse/src/Sprites/"+file;
        //make sure theres no target problems
        this.target = new Point(x,y);      
        //this keeps track of facing for display purposes
        this.facing = "right";
        //this keeps track of facing for logic purposes
        this.looking = "right";

        this.spriteImage = getSprite(0,0);
    }

    public BufferedImage getSprite(int x, int y)
    {
        if (this.spriteSheet == null)
        {
            this.spriteSheet = loadSpriteSheet();
        }
        if(this.spriteWidth == 0 || this.spriteHeight == 0)
        {
            this.spriteWidth = spriteSheet.getWidth();
            this.spriteHeight = spriteSheet.getHeight();
        }
        try
        {
            return spriteSheet.getSubimage(x*this.spriteWidth, y*this.spriteHeight, this.spriteWidth, this.spriteHeight);
        } catch(Exception e)
        {
            return spriteSheet.getSubimage(0, 0, this.spriteWidth, this.spriteHeight);
        }
    }

    /** Loads the specified image from the Sprites folder of the project.
     *
     * @param name the full name of the image, including the extension.
     * @return
     */
    private BufferedImage loadSpriteSheet()
    {
        BufferedImage ssImage = null;
        try {
            ssImage = ImageIO.read(new File(this.fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ssImage;
    }

    public void update()
    {
        if(this.target.equals(this.loc))
        {
            this.lastSafeLoc = this.loc.getLocation();
            //this.facingLocked = false;
        }
        //setFacing();
        move();
    }

    //take a step towards target
    public void move()
    {
        if(this.loc.x != this.target.x)
        {
            if(this.loc.x < this.target.x) this.setSpriteX(this.loc.x+Game.TILE_SIZE/8);
            if(this.loc.x > this.target.x) this.setSpriteX(this.loc.x-Game.TILE_SIZE/8);
        }
        if(this.loc.y != this.target.y)
        {
            if(this.loc.y < this.target.y) this.setSpriteY(this.loc.y+Game.TILE_SIZE/8);
            if(this.loc.y > this.target.y) this.setSpriteY(this.loc.y-Game.TILE_SIZE/8);
        }
    }
    public void draw(Graphics2D g2d)
    {
        //draw it to the screen
        
        if (this.facing.equals("left"))
        {
            g2d.drawImage(this.spriteImage, this.loc.x+Game.TILE_SIZE, this.loc.y, -Game.TILE_SIZE, Game.TILE_SIZE, null);
        }
        else if (this.facing.equals("right"))
        {
            g2d.drawImage(this.spriteImage, this.loc.x, this.loc.y, Game.TILE_SIZE, Game.TILE_SIZE, null);
        }
    }


    /**All the getters and setters */

    //MY GETTERS

    public int getSpriteX() {
        return this.loc.x;
    }

    public int getSpriteY() {
        return this.loc.y;
    }

    public Point getTarget()
    {
        return new Point(this.target.x, this.target.y);
    }

    public BufferedImage getSpriteImage() {
        return spriteImage;
    }

    public BufferedImage getSpriteSheet() {
        return spriteSheet;
    }

    public String getFacing()
    {
        return this.facing;
    }

    public Point getLoc()
    {
        return this.loc;
    }

    public boolean isMoving()
    {
        if(this.loc.equals(this.target))
        {
            return false;
        }
        else return true;
    }

    public Point getLookingCoords()
    {
        int x = this.loc.x;
        int y = this.loc.y;
        
        if(this.looking.equals("up")) {
            x = this.loc.x;
            y = this.loc.y-Game.TILE_SIZE;
        }
        else if(this.looking.equals("right")) {
            x = this.loc.x+Game.TILE_SIZE;
            y = this.loc.y;
        }
        else if(this.looking.equals("down")) {
            x = this.loc.x;
            y = this.loc.y+Game.TILE_SIZE;
        }
        else if(this.looking.equals("left")) {
            x = this.loc.x-Game.TILE_SIZE;
            y = this.loc.y;
        }
        return new Point(x,y);
    }
    //MY SETTERS

    public void setSpriteX(int spriteXPosition) {
        this.loc.x = spriteXPosition;
    }

    public void setSpriteY(int spriteYPosition) {
        this.loc.y = spriteYPosition;
    }

    public void setMoveTarget(int dx, int dy) {
        if(this.target.equals(this.loc))
        {
            this.lastSafeLoc = this.loc.getLocation();
            this.target.x = this.loc.x + dx;
            this.target.y = this.loc.y + dy;
            setFacing();
        }
    }
    
    public void cancelTarget()
    {
        this.target = new Point(lastSafeLoc.x, lastSafeLoc.y);
    }

    //override safe location
    public void setSafe(Point p)
    {
        this.lastSafeLoc = p.getLocation();
    }

    //sets facing based on target
    public void setFacing()
    {
        if (this.loc.x < this.target.x)
        {
            this.facing = "right";
            this.looking = "right";
        }
        else if (this.loc.x > this.target.x)
        {
            this.facing = "left";
            this.looking = "left";
        }
        else if (this.loc.y < this.target.y)
        {
            this.looking = "down";
        }
        else if (this.loc.y > this.target.y)
        {
            this.looking = "up";
        }
        
    }

    public void setFacing(String f)
    {
        if(f.equals("right") || f.equals("left"))
        {
            this.facing = f;
        }
    }

    public void setSpriteImage(BufferedImage spriteImage) {
        this.spriteImage = spriteImage;
    }

}