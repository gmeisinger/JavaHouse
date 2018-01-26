package JavaHouse;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.KeyEvent;



/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 */

public class Game {

    //vars
    public static final int TILE_SIZE = 64;

    private TileMap map;
    private Player player = null;
    private int frameWidth;
    private int frameHeight;
    private Camera camera;
    private DialogBox dialog;
    private boolean paused = false;
    private HashMap<String,TileMap> gameMaps;

    //time
    private long startTime;

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
        this.startTime = System.currentTimeMillis();
        //setup the maps
        this.gameMaps = new HashMap<String,TileMap>();
        this.gameMaps.put("bedroom", new TileMap("bedroom.txt", this));
        this.gameMaps.put("hallway", new TileMap("hallway.txt", this));
        this.gameMaps.put("hallroom1", new TileMap("hallroom1.txt", this));
        this.gameMaps.put("hallroom2", new TileMap("hallroom2.txt", this));
        this.gameMaps.put("hallroom3", new TileMap("hallroom3.txt", this));
        this.gameMaps.put("hallroom4", new TileMap("hallroom4.txt", this));
        this.gameMaps.put("foyer", new TileMap("foyer.txt", this));
        
        this.map = this.gameMaps.get("foyer");
        //player generated by map
        //camera
        this.camera = new Camera(this);
        //dialog box
        this.dialog = new DialogBox(this);
        //menus

    }
    
    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {
        //this.map.parseMap();
        try {
            this.map.initialize();
        } catch(FileNotFoundException e) {e.printStackTrace();}
        //images
        //camera (it needs it all loaded to init)
        this.camera.initialize();
    
    }    
    
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        
    }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        if(this.paused)
        {
            //update menus
            if(dialog.isVisible())
            {
                dialog.update();
            }
        }
        else
        {   
            //update player logic
            this.player.update();
            //sprites
            for(Npc npc : this.map.getNpcs())
            {
                npc.update();
            }
            //update camera
            this.camera.update();
            //update dialog boxes
            //collision prevention in sprite class
        }
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.translate(-this.camera.camX, -this.camera.camY);
        this.map.drawMap(g2d);
        this.player.draw(g2d);
        for(Npc npc : this.map.getNpcs())
        {
            npc.draw(g2d);
        }
        this.map.drawOverlays(g2d);
        //translate back so the dialog box is centered correctly
        g2d.translate(this.camera.camX, this.camera.camY);
        if(this.dialog.isVisible())
        {
            this.dialog.draw(g2d);
        }
    }

    //MY SETTERS

    public void setFrameWidth(int width)
    {
        this.frameWidth = width;
    }
    public void setFrameHeight(int height)
    {
        this.frameHeight = height;
    }

    public void setPlayer(Player p)
    {
        if(this.player == null)
        {
            this.player = p;
        }
    }

    public void setPaused(boolean b)
    {
        this.paused = b;
    }

    public void showDialog(boolean b)
    {
        if(b)
        {
            this.paused = true;
            this.dialog.setVisible(true);
        }
        else
        {
            this.dialog.close();
            this.paused = false;
        }
    }

    public void setMap(String mapName, Point startLoc)
    {
        this.map = this.gameMaps.get(mapName);
        if(!this.map.isInit())
        {
            try {
                this.map.initialize();
            } catch(FileNotFoundException e) {e.printStackTrace();}
        }
        this.player.setMap(this.map);
        this.player.setLoc(startLoc);
        for(Door d : this.map.getDoors())
        {
            d.setClosed(true);
        }
        this.camera.initialize();
    }

    //MY GETTERS

    //returns reference to map
    public TileMap getMap()
    {
        return this.map;
    }

    //reference to player
    public Player getPlayer()
    {
        return this.player;
    }

    //reference to dialogBox
    public DialogBox getDialogBox()
    {
        return this.dialog;
    }

    //frame size
    public int getFrameWidth()
    {
        return this.frameWidth;
    }
    public int getFrameHeight()
    {
        return this.frameHeight;
    }

    //pause state
    public boolean isPaused()
    {
        return this.paused;
    }

    //runtime
    public int getRunTime()
    {
        long time = System.currentTimeMillis();
        int runtime = (int) (time - this.startTime)/1000;
        return runtime;
    }
}
