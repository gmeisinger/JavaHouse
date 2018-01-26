package JavaHouse;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.lang.Math;

import JavaHouse.Game;
import JavaHouse.Sprite;
import JavaHouse.TileMap;

public class Npc
{
    //vars
    private String msg;
    private Sprite sprite;
    private Animation animation;
    private Game game;
    private TileMap map;
    //wandering npcs
    private int maxWanderX = 0;
    private int maxWanderY = 0;
    private int xDistTraveled = 0;
    private int yDistTraveled = 0;
    //private Point startingLoc;

    //ANIMATIONS
    private Animation standing;
    private Animation walking;

    public Npc(Sprite s, Game game)
    {
        this.sprite = s;
        this.game = game;
        this.map = this.game.getMap();
        initialize();
    }

    public void initialize()
    {
        //ANIMATIONS
        BufferedImage[] animStanding = {this.sprite.getSprite(0, 0)};
        //BufferedImage[] animWalkLeft = {};
        BufferedImage[] animWalkRight = {this.sprite.getSprite(1, 0), this.sprite.getSprite(2, 0)};

        this.standing = new Animation(animStanding, 2);
        this.walking = new Animation(animWalkRight, 2);

        this.animation = standing;
        //this.startingLoc = new Point(this.sprite.getLoc());
    }

    public void update()
    {
        animation.update();
        this.sprite.setSpriteImage(animation.getSprite());

        //animation
        if(this.sprite.isMoving())
        {
            this.animation = this.walking;
            this.animation.start();
        }
        else
        {
            this.animation.stop();
            this.animation.reset();
            this.animation = this.standing;
            if(this.maxWanderX > 0 || this.maxWanderY > 0)
            {
                wander();
            }
        }

        this.sprite.update();
    }

    public void draw(Graphics2D g2d)
    {
        this.sprite.draw(g2d);
    }

    public void wander()
    {
        Random rand = new Random();
        int whereTo = rand.nextInt(100);
        int dx = 0;
        int dy = 0;
        if(whereTo == 0 && maxWanderY != 0)
        {
            //go up
            dy = -1;
        }
        if(whereTo == 1 && maxWanderX != 0)
        {
            //right
            dx = 1;
        }
        if(whereTo == 2 && maxWanderY != 0)
        {
            //down
            dy = 1;
        }
        if(whereTo == 3 && maxWanderX != 0)
        {
            //left
            dx = -1;
        }
        if(Math.abs(this.xDistTraveled + dx) > this.maxWanderX)
            dx = 0;
        if(Math.abs(this.yDistTraveled + dy) > this.maxWanderY)
            dy = 0;

        this.sprite.setMoveTarget(dx*Game.TILE_SIZE,dy*Game.TILE_SIZE);
        //collision prevention
        if(!this.map.canBeMovedTo(this.sprite.getTarget()))
        {
            this.sprite.cancelTarget();
        }
        if(this.sprite.getTarget().equals(this.game.getPlayer().getLoc()) || this.sprite.getTarget().equals(this.game.getPlayer().getTarget()))
            this.sprite.cancelTarget();
        else
        {
            xDistTraveled = xDistTraveled + dx;
            yDistTraveled = yDistTraveled + dy;
        }
    }

    //MY SETTERS
    public void setMessage(String msg)
    {
        this.msg = msg;
    }

    public void setWander(int distance)
    {
        this.maxWanderX = distance;
        this.maxWanderY = distance;
    }
    public void setWander(int xDistance, int yDistance)
    {
        if(xDistance != 0) this.maxWanderX = xDistance;
        if(yDistance != 0) this.maxWanderY = yDistance;
    }

    //MY GETTERS
    public Point getLoc()
    {
        return new Point(this.sprite.getSpriteX(), this.sprite.getSpriteY());
    }

    public String getMessage()
    {
        return this.msg;
    }
}