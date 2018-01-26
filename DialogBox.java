package JavaHouse;

import java.io.File;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import JavaHouse.Canvas;
import JavaHouse.Game;

public class DialogBox
{
    //vars
    //the box the text will sit in
    private MenuBox box;
    private int screenWidth;
    private int screenHeight;
    private int x;
    private int y;
    private int width;
    private int height;
    private ArrayList<String> msg;
    private boolean visible = false;
    private Font font;
    private FontMetrics fm;
    private int fontSize;
    private Game game;
    //for dialogBox
    private boolean hasMore;
    private ArrayList<String> next;
    private final String MORE = "...";
    private HashMap<String,String> varMap;

    public DialogBox(Game game)
    {
        this.game = game;
        this.screenWidth = game.getFrameWidth();
        this.screenHeight = game.getFrameHeight(); 
        this.fontSize = this.screenWidth/40; 
        initialize();
    }

    public void initialize()
    {
        this.x = this.screenWidth/4;
        this.y = this.screenHeight/3;
        this.width = this.screenWidth/2;
        this.height = this.screenHeight/4;
        this.box = new MenuBox(x, y, width, height);
        this.font = loadFont();
        this.varMap = new HashMap<String,String>();
    }

    //setup font
    public Font loadFont()
    {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/retroFont.ttf")));
        } catch (IOException|FontFormatException e) {
            //Handle exception
        }

        return new Font("Zig", Font.PLAIN, this.fontSize);
    }

    public void update()
    {
        setVars();
        if(Canvas.isKeyHit(KeyEvent.VK_SPACE))
        {
            if(this.hasMoreText())
            {
                this.setToNext();
            }
            else
            {
                this.game.showDialog(false);
            }
        }
    }

    public void draw(Graphics2D g2d)
    {
        g2d.setFont(this.font);
        this.fm = g2d.getFontMetrics();
        int xCoord = this.x + this.width/16;
        int yCoord = this.y + this.height/4;
        int xMax = this.x +this.width-this.width/12;
        int yLines = 1;
        int words = this.msg.size();
        int wordsPrinted = 0;

        //draw the box
        this.box.draw(g2d);
        
        for(String s : this.msg)
        {
            //bugfix for newline chars at the beginning of a box
            if(s.equals("\\n") && yLines == 1 && xCoord == this.x + this.width/16)
                s = "";
            if(s.startsWith("|"))
            {
                s = varMap.get(s.substring(1));
            }
            if(this.hasMore)
            {
                g2d.drawString(this.MORE, this.x+(this.width/2) - (this.fm.stringWidth(this.MORE)/2), this.y+this.height-this.fm.getAscent()/4);
            }
            if(this.msg.indexOf(s) == 0)
            {
                g2d.drawString(s, xCoord, yCoord);
                wordsPrinted++;
                xCoord = xCoord + this.fm.stringWidth(s) + this.fm.stringWidth(" ");                
            }
            else
            {
                if (xCoord + this.fm.stringWidth(s)>xMax)
                {
                    xCoord = this.x + this.width/16;
                    yCoord = yCoord + this.fm.getAscent()*2;
                    yLines++;
                }
                //if the box is full
                if (yLines >3 && this.msg.indexOf(s) != this.msg.size())
                {
                    this.next = new ArrayList<String>(this.msg.subList(this.msg.indexOf(s), this.msg.size()));
                    this.hasMore = true;
                    break;
                }
                //similarly, if we need a new box
                if (s.equals("\\n"))
                {
                    this.next = new ArrayList<String>(this.msg.subList(this.msg.indexOf(s) + 1, this.msg.size()));
                    this.hasMore = true;
                    break;
                }
                else
                {
                    g2d.drawString(s, xCoord, yCoord);
                    wordsPrinted++;
                    xCoord = xCoord + this.fm.stringWidth(s) + this.fm.stringWidth(" ");
                }
            }
        }
        if(wordsPrinted == words)
        {
            this.hasMore = false;
        }   
    }

    public void close()
    {
        this.setVisible(false);
        this.hasMore = false;
    }

    //MY SETTERS

    public void setToNext()
    {
        this.msg = this.next;
    }

    public void setMessage(String msg)
    {
        this.msg = new ArrayList<String>();
        for (String word : msg.split(" "))
        {
            this.msg.add(word);
        }
    }

    public void setVisible(boolean b)
    {
        this.visible = b;
    }

    public void setVars()
    {
        //time var
        String time = Integer.toString(this.game.getRunTime());
        this.varMap.put("time", time);
    }

    //MY GETTERS

    public boolean hasMoreText()
    {
        return this.hasMore;
    }

    public String getMessage()
    {
        String message = "";
        for(String s : this.msg)
        {
            message = s + " ";
        }
        return message;
    }

    public boolean isVisible()
    {
        return this.visible;
    }

    public Font getFont()
    {
        return this.font;
    }

}