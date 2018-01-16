package javahouse;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
    private ArrayList<String> inventory;
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
        //INVENTORY
        this.inventory = new ArrayList<String>();
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
        //check for map change
        Tile currentTile = this.map.tileAtPoint(getLoc());
        if(currentTile.has("portal") && !this.sprite.isMoving())
        {
            Point start = this.map.parsePoint(currentTile.get("start"));
            start.x = start.x*Game.TILE_SIZE;
            start.y = start.y*Game.TILE_SIZE;
            this.game.setMap(currentTile.get("portal"), start);
        }

        //update animation
        animation.update();
        this.sprite.setSpriteImage(animation.getSprite());

        //INTERACT
        if(Canvas.isKeyHit(KeyEvent.VK_SPACE))
        {
            interact();   
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

    //Handles logic for interact button, used in update()
    public void interact()
    {
        Tile t = this.map.tileAtPoint(getLookingLoc());
        String msg;
        DialogBox dbox = this.game.getDialogBox();

        for(Npc npc : this.map.getNpcs())
        {
            if(npc.getLoc().equals(this.getLookingLoc()))
            {
                msg = npc.getMessage();
                dbox.setMessage(msg);
                this.game.showDialog(true);
                return;
            }
        }
        for(Door door : this.map.getDoors())
        {
            if(door.getLoc().equals(this.getLookingLoc()))
            {
                doorCheck(door);
                return;
            }
        }
        for(Item item : this.map.getItems())
        {
            if(item.getLoc().equals(this.getLookingLoc()))
            {
                //get the item, remove it from list
                this.inventory.add(item.getName());
                this.map.removeItem(item);
                //dialog
                msg = "You found " + item.getName() + "!";
                dbox.setMessage(msg);
                this.game.showDialog(true);
                return;
            }
        }
        if(t.has("msg"))
        {
            msg = t.get("msg");
            dbox.setMessage(msg);
            this.game.showDialog(true);
            return;
        }

    }

    //Handles door logic, used in interact
    private void doorCheck(Door door)
    {
        if(door.isClosed() && door.isLocked())
        {
            DialogBox dbox = this.game.getDialogBox();
            String key = door.getKey();
            String msg;
            if(this.hasItem(key))
            {
                door.setLocked(false);
                door.setClosed(false);
                msg = "You used the " + key + "!";
                dbox.setMessage(msg);
                this.game.showDialog(true);
            }
            else
            {
                msg = "The door is locked.";
                dbox.setMessage(msg);
                this.game.showDialog(true);
            }
        }
        else if(door.isClosed())
        {
            door.setClosed(false);
        }
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

    public void stabilize()
    {
        this.sprite.setSafe(this.getLoc());
    }

    //set the map reference to the active map
    public void setMap(TileMap tMap)
    {
        this.map = tMap;
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

    public boolean hasItem(String item)
    {
        if(this.inventory.isEmpty()) return false;
        if(this.inventory.contains(item))
        {
            return true;
        }
        else return false;
    }
}