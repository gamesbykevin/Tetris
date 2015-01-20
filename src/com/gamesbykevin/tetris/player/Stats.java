package com.gamesbykevin.tetris.player;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.tetris.menu.CustomMenu;
import java.awt.Color;
import java.awt.Font;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * This class will display the player stats on-screen
 * @author GOD
 */
public final class Stats implements Disposable
{
    //timer for our game
    private Timer gameTimer;
    
    //timed mode will be 2 minutes long
    private static final long TIMED_MODE_DELAY = Timers.toNanoSeconds(2);
    
    //the image we render the time on
    private BufferedImage timeImage;
    
    //image of current level
    private BufferedImage levelImage;
    
    //image of lines completed
    private BufferedImage linesImage;
    
    //image of player name
    private BufferedImage nameImage;
    
    //image of player health (tug-of-war mode only)
    private BufferedImage healthImage;
    
    //the image that will display winner/loser
    private BufferedImage resultImage;
    
    //game font
    private Font font;
    
    //store the game mode we are playing
    private final int modeIndex;
    
    //store player stats
    private int health = 100;
    private int level = 0;
    private int lines = 0;
    private String name = "";
    private long passed;
    
    //image dimensions
    private static final int IMAGE_WIDTH = 150;
    private static final int IMAGE_HEIGHT = 25;
    
    //the offset where the stats will be drawn
    private static final int OFFSET_X = 235;
    private static final int OFFSET_Y = 225;
    
    //the pixels to offset each individual stat
    private static final int OFFSET_STAT_X = 10;
    private static final int OFFSET_STAT_Y = 4;
    
    //get player info at first
    private boolean setup = true;
    
    //location where render will start
    private double x, y;
    
    protected Stats(final int modeIndex)
    {
        //store the game mode
        this.modeIndex = modeIndex;
        
        switch (modeIndex)
        {
            case CustomMenu.GAME_MODE_INFINITE:
            case CustomMenu.GAME_MODE_NORMAL:
            case CustomMenu.GAME_MODE_TUG_OF_WAR:
            default:
                this.gameTimer = new Timer();
                break;
                
            case CustomMenu.GAME_MODE_TIMED:
                this.gameTimer = new Timer(TIMED_MODE_DELAY);
                break;
        }
    }
    
    /**
     * Assign the font for the stats
     * @param font The font we want to add
     */
    public void setFont(final Font font)
    {
        this.font = font.deriveFont(18f);
    }
    
    public Font getFont()
    {
        return this.font;
    }
    
    /**
     * Update the player health.<br>
     * Regardless of parameter health will remain between 0 - 100
     * @param change The number we want to add to the total health
     */
    public void updateHealth(final int change)
    {
        this.health += change;
        
        //keep the health within range
        if (getHealth() < 0)
            this.health = 0;
        if (getHealth() > 100)
            this.health = 100;
    }
    
    /**
     * Get the health of this player 
     * @return The health of the player, a number between 0 - 100
     */
    public int getHealth()
    {
        return this.health;
    }
    
    /**
     * Get the game play timer
     * @return Object representing amount of time passed/left
     */
    public Timer getGameTimer()
    {
        return this.gameTimer;
    }
    
    @Override
    public void dispose()
    {
        gameTimer = null;
        font = null;
        
        if (healthImage != null)
        {
            healthImage.flush();
            healthImage = null;
        }
        
        if (nameImage != null)
        {
            nameImage.flush();
            nameImage = null;
        }

        if (levelImage != null)
        {
            levelImage.flush();
            levelImage = null;
        }
        
        if (linesImage != null)
        {
            linesImage.flush();
            linesImage = null;
        }
        
        if (timeImage != null)
        {
            timeImage.flush();
            timeImage = null;
        }
    }
    
    /**
     * Update display image
     */
    private void renderLevelImage()
    {
        if (levelImage == null)
            levelImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        
        //get graphics object to write image
        Graphics2D g2d = this.levelImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        
        //set font if available
        if (getFont() != null)
            g2d.setFont(getFont());
        
        //draw info
        g2d.setColor(Color.WHITE);
        g2d.drawString("Level: " + level, OFFSET_STAT_X, IMAGE_HEIGHT - OFFSET_STAT_Y);
    }

    /**
     * Create the result image that will display lose or victory
     * @param win True if we won, false otherwise
     */
    public void renderResultImage(final boolean win)
    {
        resultImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT * 2, BufferedImage.TYPE_INT_ARGB);
        
        //get graphics object to write image
        Graphics2D g2d = resultImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, resultImage.getWidth(), resultImage.getHeight());
        
        //set font if available
        if (getFont() != null)
            g2d.setFont(getFont());
        
        //draw info
        g2d.setColor((win) ? Color.GREEN : Color.RED);
        g2d.drawString((win) ? "Winner" : "Loser", OFFSET_STAT_X, IMAGE_HEIGHT - OFFSET_STAT_Y);
        g2d.drawString("Press \"esc\"", OFFSET_STAT_X, (IMAGE_HEIGHT * 2) - OFFSET_STAT_Y);
    }
    
    /**
     * Update display image
     */
    private void renderNameImage()
    {
        if (nameImage == null)
            nameImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        
        //get graphics object to write image
        Graphics2D g2d = this.nameImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, nameImage.getWidth(), nameImage.getHeight());
        
        //set font if available
        if (getFont() != null)
            g2d.setFont(getFont());
        
        //draw info
        g2d.setColor(Color.WHITE);
        g2d.drawString("" + name, OFFSET_STAT_X, IMAGE_HEIGHT - OFFSET_STAT_Y);
    }
    
    /**
     * Update display image
     */
    private void renderLinesImage()
    {
        if (linesImage == null)
            linesImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        
        //get graphics object to write image
        Graphics2D g2d = this.linesImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        
        //set font if available
        if (getFont() != null)
            g2d.setFont(getFont());
        
        //draw info
        g2d.setColor(Color.WHITE);
        g2d.drawString("Lines: " + lines, OFFSET_STAT_X, IMAGE_HEIGHT - OFFSET_STAT_Y);
    }
    
    /**
     * Update display image
     */
    private void renderTimeImage()
    {
        if (timeImage == null)
            timeImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        
        //get graphics object to write image
        Graphics2D g2d = this.timeImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g2d.setColor(Color.WHITE);
        
        //set font if available
        if (getFont() != null)
            g2d.setFont(getFont());
        
        switch(modeIndex)
        {
            //for timed mode get remaining time
            case CustomMenu.GAME_MODE_TIMED:
                
                //draw time remaining info
                g2d.drawString("Time: " + getGameTimer().getDescRemaining(Timers.FORMAT_8), OFFSET_STAT_X, IMAGE_HEIGHT - OFFSET_STAT_Y);
                break;
                
            //everything else get time passed
            default:
                
                //draw time passed info
                g2d.drawString("Time: " + getGameTimer().getDescPassed(Timers.FORMAT_8), OFFSET_STAT_X, IMAGE_HEIGHT - OFFSET_STAT_Y);
                break;
        }
    }
    
    /**
     * Update display image
     */
    public void renderHealthImage()
    {
        if (healthImage == null)
            healthImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        
        //get graphics object to write image
        Graphics2D g2d = this.healthImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        
        //set font if available
        if (getFont() != null)
            g2d.setFont(getFont());
        
        //draw player health
        if (health <= 25)
        {
            g2d.setColor(Color.RED);
        }
        else if (health > 25 && health < 50)
        {
            g2d.setColor(Color.ORANGE);
        }
        else if (health >= 50 && health < 75)
        {
            g2d.setColor(Color.YELLOW);
        }
        else
        {
            g2d.setColor(Color.GREEN);
        }
        
        g2d.drawString("Health: " + health, OFFSET_STAT_X, IMAGE_HEIGHT - OFFSET_STAT_Y);
    }
    
    /**
     * Update the game timer
     * @param player The current player
     * @param time nanoseconds per each update
     */
    protected void update(final Player player, final long time)
    {
        //grab initial values
        if (setup)
        {
            //unflag setup
            setup = false;
            
            //set stat starting coordinate
            x = player.getX() + OFFSET_X;
            y = player.getY() + OFFSET_Y;
            
            //set default
            health = 100;
            
            //get the level we are on
            level = (int)(player.getBoard().getLines() / 10);
            
            //get total completed lines
            lines = player.getBoard().getLines();
            
            //store player name description
            name = player.getName();
            
            //we only need to create this image once
            renderNameImage();
            
            //render current level image
            renderLevelImage();
            
            //render lines image
            renderLinesImage();
            
            //object representing timer
            renderTimeImage();
            
            //object with player health
            renderHealthImage();
        }
        
        //check if the number of completed lines has changed
        if (lines != player.getBoard().getLines())
        {
            //store new info
            lines = player.getBoard().getLines();
            
            //update image
            this.renderLinesImage();
        }
        
        //check if the level has changed
        if (level != (int)(player.getBoard().getLines() / 10))
        {
            //store new info
            level = (int)(player.getBoard().getLines() / 10);
            
            //update image
            this.renderLevelImage();
        }
        
        //update timer
        getGameTimer().update(time);
        
        //don't allow negative time for timed mode
        if (modeIndex == CustomMenu.GAME_MODE_TIMED)
        {
            if (getGameTimer().getRemaining() < 0)
                getGameTimer().setRemaining(0);
        }
        
        //track time passed
        passed += time;
        
        //if 1 second has passed render new time image
        if (passed >= Timers.NANO_SECONDS_PER_SECOND)
        {
            //draw new time image
            renderTimeImage();
            
            //reset time passed
            passed = passed - Timers.NANO_SECONDS_PER_SECOND;
        }
    }
    
    protected void render(final Graphics graphics)
    {
        if (resultImage != null)
            graphics.drawImage(resultImage, (int)x, (int)y + (-2 * IMAGE_HEIGHT), null);
        if (nameImage != null)
            graphics.drawImage(nameImage,   (int)x, (int)y + (0 * IMAGE_HEIGHT), null);
        if (levelImage != null)
            graphics.drawImage(levelImage,  (int)x, (int)y + (1 * IMAGE_HEIGHT), null);
        if (linesImage != null)
            graphics.drawImage(linesImage,  (int)x, (int)y + (2 * IMAGE_HEIGHT), null);
        if (timeImage != null)
            graphics.drawImage(timeImage,   (int)x, (int)y + (3 * IMAGE_HEIGHT), null);
        
        if (modeIndex == CustomMenu.GAME_MODE_TUG_OF_WAR)
            graphics.drawImage(healthImage, (int)x, (int)y + (4 * IMAGE_HEIGHT), null);
    }
}