package javahouse;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.Point;

import javahouse.Game;

public class TileMap
{
    //vars
    //map filename
    private String mapFile;
    /**Specified tileSet for this map. */
    private TileSet ts;
    private int cols;
    private int rows;

    /**The list of npc sprites */
    private ArrayList<Npc> NpcSprites;

    /**The map, stored in an array list of strings */
    private ArrayList<String> charMap;
    /**The map data, stored in a map where strings are paired to hash maps
     * these hashmaps contain descriptions and settings for the specific key
     */
    private HashMap<String,HashMap<String,String>> key = new HashMap<String, HashMap<String, String>>();
    /**Reference to the Game */
    private Game game;

    /**The main data structure, an array of tiles. */
    private Tile[][] map;

    //constructor
    public TileMap(String mapFile, Game game)
    {
        this.mapFile = "javahouse/src/Maps/"+mapFile;
        this.game = game;
        this.charMap = new ArrayList<String>();
        this.NpcSprites = new ArrayList<Npc>();
    }

    /**Reads the map in from .txt file, and sets up variables
     * most importantly, the key
     */
    public void initialize() throws FileNotFoundException
    {
        Scanner scan = new Scanner(new File(this.mapFile));
        String line;
        String currentKey = "settings";
        String attribute;
        String data;
        HashMap<String,String> desc = new HashMap<String,String>();
        while (scan.hasNextLine())
        {
            line = scan.nextLine();
            if (line.startsWith(" "))
            {
                this.charMap.add(line.trim());
            }
            else
            {
                if(line.startsWith("["))
                {
                    if(!desc.isEmpty()) this.key.put(currentKey, desc);
                    currentKey = line.replaceAll("\\[", "").replaceAll("]", "");
                    desc = new HashMap<String,String>();
                }
                else
                {
                    attribute = line.replaceAll("\\s=\\s.*", "");
                    data = line.replaceAll(".*\\s=\\s", "");
                    desc.put(attribute, data);
                }
            }
            if(!scan.hasNextLine()) this.key.put(currentKey, desc);
        }
        scan.close();
        //get some settings from the key
        String tileSetFile = "/javahouse/src/Tiles/"+getData("settings", "tileset");
        int tsTileSize = Integer.parseInt(getData("settings", "tilesize"));
        this.ts = new TileSet(tileSetFile, tsTileSize, tsTileSize);
        this.cols = this.charMap.get(0).length();
        this.rows = this.charMap.size();
        this.map = new Tile[cols][rows];
        buildMap();
    }

    /**Builds a 2d array of tiles
     * the tiles are going to hold data like whether or not 
     * it can be moved to, items it may hold, etc.
     */
    public void buildMap()
    {
        Tile t;
        for(int r=0;r<this.rows;r++)
        {
            for(int c=0;c<this.cols;c++)
            {
                String ch = String.valueOf(this.charMap.get(r).charAt(c));
                if(this.key.containsKey(ch)) 
                {
                    
                    t = new Tile(getTileAt(getData(ch, "tile")));
                    if(this.key.get(ch).containsKey("wall") && !this.isWall(c, r+1))
                    {
                        t = getTileAt(getData(ch, "wall"));
                    }
                    //tiles drawn onto other tiles
                    if(this.key.get(ch).containsKey("over"))
                    {
                        t.addImage(getTileAt(getData(ch, "over")).getImage());
                    }
                    t.setDesc(this.key.get(ch));
                    this.map[c][r] = t;
                }
            }
        }
    }

    public void drawMap(Graphics2D g2d)
    {
        for(int r=0; r<this.rows;r++)
        {
            for(int c=0;c<this.cols;c++)
            {
                Tile t = this.map[c][r];
                t.draw(g2d, c*Game.TILE_SIZE, r*Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE);
            }
        }
    }

    public void loadSprites()
    {
        for(int r=0;r<this.rows;r++)
        {
            for(int c=0;c<this.cols;c++)
            {
                String ch = String.valueOf(this.charMap.get(r).charAt(c));
                if(this.key.get(ch).containsKey("sprite"))
                {
                    Sprite s = new Sprite(c*Game.TILE_SIZE, r*Game.TILE_SIZE, Integer.parseInt(getData(ch, "size")), getData(ch, "sprite"));
                    //this.game.addSprite(s);
                    //custom facing
                    if(this.key.get(ch).containsKey("facing")) s.setFacing(getData(ch, "facing"));
                    //set player
                    if(this.key.get(ch).containsKey("player")) 
                    {
                        game.setPlayer(new Player(s, this.game));
                    }
                    //the map will keep the list of its sprites
                    else if(this.key.get(ch).containsKey("npc"))
                    {
                        Npc npc = new Npc(s, this.game);
                        npc.setMessage(getData(ch, "npcMsg"));
                        this.NpcSprites.add(npc);
                    }

                }
            }
        }
    }

    //CLASS HELPERS
    //some helpers
    public String getData(String section, String attribute)
    {
        return this.key.get(section).get(attribute);
    }

    //return tiles from tileset, using coord stored in the tile attribute
    //parses a string like "0,2"
    public Tile getTileAt(String coordinate)
    {
        int row = Integer.parseInt(coordinate.replaceAll(",.*", ""));
        int col = Integer.parseInt(coordinate.replaceAll(".*,", ""));
        return ts.getTileAt(col, row);

    }

    //useful methods
    public boolean hasAttribute(int col, int row, String attribute)
    {
        if(col < 0 || col>this.cols-1 || row < 0 || row>this.rows-1) {
            return false;
        }
        else {
            String ch = String.valueOf(this.charMap.get(row).charAt(col));
            return this.key.get(ch).containsKey(attribute);
        }
    }

    public boolean hasAttribute(String ch, String attribute)
    {
        return this.key.get(ch).containsKey(attribute);
    }

    public boolean isWall(int col, int row)
    {
        return hasAttribute(col, row, "wall");
    }
    public boolean isBlocking(int col, int row)
    {
        return hasAttribute(col, row, "block");
    }

    

    //MY GETTERS
    public int getCols()
    {
        return this.cols;
    }
    public int getRows()
    {
        return this.rows;
    }

    public ArrayList<Npc> getNpcs()
    {
        return this.NpcSprites;
    }

    //determines if the tile is safe to move to
    public boolean canBeMovedTo(Point p)
    {
        Tile t = tileAtPoint(p);
        for(Npc npc : this.NpcSprites)
        {
            if(npc.getLoc().equals(p)) return false;
        }
        if(t.getDesc().containsKey("block"))
            return false;
        else return true;
    }
    //takes a point and returns the char on the map
    public Tile tileAtPoint(Point p)
    {
        int col = p.x/Game.TILE_SIZE;
        int row = p.y/Game.TILE_SIZE;
        //String ch = String.valueOf(this.charMap.get(row).charAt(col));
        //return ch;
        return this.map[col][row];
    }

    

}