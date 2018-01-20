package JavaHouse;

import java.awt.Point;

import JavaHouse.Game;
import JavaHouse.Sprite;
import JavaHouse.TileMap;

public class Camera
{
    //vars
    private int viewPortX;
    private int viewPortY;
    private int worldSizeX;
    private int worldSizeY;
    private int offsetMaxX;
    private int offsetMaxY;
    private int offsetMinX = 0;
    private int offsetMinY = 0;
    private Game game;
    private TileMap map;
    //can be applied to a sprite to target it
    private Sprite target;
    private Point targetLoc;
    //the cam position
    int camX;
    int camY;

    //constructor
    public Camera(Game game)
    {
        this.game = game;
    }

    /**gets info about the screen and the target
     * target defaults to player, but can be applied to any sprite
     * when game map changes, this MUST be run
     */
    public void initialize()
    {
        this.map = game.getMap();

        this.viewPortX = this.game.getFrameWidth();
        this.viewPortY = this.game.getFrameHeight();

        this.worldSizeX = this.map.getCols() * Game.TILE_SIZE;
        this.worldSizeY = this.map.getRows() * Game.TILE_SIZE;

        this.offsetMaxX = worldSizeX - viewPortX;
        this.offsetMaxY = worldSizeY - viewPortY;

        this.offsetMinX = 0;
        this.offsetMinY = 0;

        this.target = game.getPlayer().getSprite();

        this.targetLoc = this.target.getLoc();
    }

    /**Updates the location of camera */
    public void update()
    {
        this.targetLoc = this.target.getLoc();
        camX = targetLoc.x - viewPortX / 2;
        camY = targetLoc.y - viewPortY / 2;

        /*check offsets
        if (camX > offsetMaxX)
        {
            camX = offsetMaxX;
        }
        else if (camX < offsetMinX)
        {
            camX = offsetMinX;
        }
        if (camY > offsetMaxY)
        {
            camY = offsetMaxY;
        }
        else if (camY < offsetMinY)
        {
            camY = offsetMinY;
        }

        //small room bugfix
        if(worldSizeX < viewPortX)
        {
            camX = -worldSizeX/4;
        }
        if(worldSizeY < viewPortY)
        {
            camY = -worldSizeY/4;
        }*/
    }
}