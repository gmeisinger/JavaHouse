package javahouse;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javahouse.Canvas;
import javahouse.Game;
import javahouse.TileMap;

public class Player
{
    //vars
    private Sprite sprite;
    private Animation animation;
    private Game game;
    private TileMap map;
    //inventory
    //knowledge inventory

    //LOCATION

    //ANIMATIONS
    private Animation standing;
    private Animation walking;

    //constructor
    public Player(Sprite s, Game game)
    {
        this.sprite = s;
        this.game = game;
        this.map = this.game.getMap();
        initialize();
    }

    private void initialize()
    {
        //ANIMATIONS
        BufferedImage[] animStanding = {this.sprite.getSprite(0, 0)};
        //BufferedImage[] animWalkLeft = {};
        BufferedImage[] animWalkRight = {this.sprite.getSprite(0, 1), this.sprite.getSprite(1, 1)};

        this.standing = new Animation(animStanding, 2);
        this.walking = new Animation(animWalkRight, 2);

        this.animation = standing;
    }

    public void update()
    {
        animation.update();
        this.sprite.setSpriteImage(animation.getSprite());

        //INTERACT
        if(Canvas.isKeyHit(KeyEvent.VK_SPACE))
        {
            Tile t = this.map.tileAtPoint(getLookingLoc());
            String msg;
            DialogBox dbox = this.game.getDialogBox();

            if(t.has("msg"))
            {
                msg = t.get("msg");
                dbox.setMessage(msg);
                this.game.showDialog(true);
            }
            else
            {
                for(Npc npc : this.map.getNpcs())
                {
                    if(npc.getLoc().equals(this.getLookingLoc()))
                    {
                        msg = npc.getMessage();
                        dbox.setMessage(msg);
                        this.game.showDialog(true);
                    }
                }
            }
            
        }

        //MOVEMENT
        if (Canvas.isKeyHit(KeyEvent.VK_DOWN))
        {
            this.sprite.setMoveTarget(0, Game.TILE_SIZE);
        }
        if (Canvas.isKeyHit(KeyEvent.VK_RIGHT))
        {
            this.sprite.setMoveTarget(Game.TILE_SIZE, 0);
        }
        if (Canvas.isKeyHit(KeyEvent.VK_LEFT))
        {
            this.sprite.setMoveTarget(-Game.TILE_SIZE, 0);
        }
        if (Canvas.isKeyHit(KeyEvent.VK_UP))
        {
            this.sprite.setMoveTarget(0, -Game.TILE_SIZE);
        }

        //collision prevention
        if(!this.map.canBeMovedTo(this.getTarget()))
        {
            this.cancelTarget();
        }

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
        }

        this.sprite.update();
    }

    public void draw(Graphics2D g2d)
    {
        this.sprite.draw(g2d);
    }


    //MY SETTERS

    public void setMoveTarget(int dx, int dy)
    {
        this.sprite.setMoveTarget(dx, dy);
    }

    public void cancelTarget()
    {
        this.sprite.cancelTarget();
    }

    public void setLoc(Point p)
    {
        this.sprite.setSpriteX(p.x);
        this.sprite.setSpriteY(p.y);
    }

    //MY GETTERS

    public Sprite getSprite()
    {
        return this.sprite;
    }

    public Point getTarget()
    {
        return this.sprite.getTarget();
    }

    public Point getLoc()
    {
        return new Point(this.sprite.getSpriteX(), this.sprite.getSpriteY());
    }

    public Point getLookingLoc()
    {
        return this.sprite.getLookingCoords();
    }
}