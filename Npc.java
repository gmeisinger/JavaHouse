package JavaHouse;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

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
        }

        this.sprite.update();
    }

    public void draw(Graphics2D g2d)
    {
        this.sprite.draw(g2d);
    }

    //MY SETTERS
    public void setMessage(String msg)
    {
        this.msg = msg;
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