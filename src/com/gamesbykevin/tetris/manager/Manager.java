package com.gamesbykevin.tetris.manager;

import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.menu.Menu;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.tetris.engine.Engine;
import com.gamesbykevin.tetris.menu.CustomMenu;
import com.gamesbykevin.tetris.menu.CustomMenu.*;

import com.gamesbykevin.tetris.player.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements IManager
{
    //where gameplay occurs
    private Rectangle window;
    
    //the player
    private Players players;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine Engine for our game that contains all objects needed
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //set the audio depending on menu setting
        engine.getResources().setAudioEnabled(Toggle.values()[engine.getMenu().getOptionSelectionIndex(LayerKey.OptionsInGame, OptionKey.Sound)] == Toggle.Off);
        
        //set the game window where game play will occur
        setWindow(engine.getMain().getScreen());
        
        //do we hide the detail screen
        //setEnableDetailScreen(Toggle.values()[engine.getMenu().getOptionSelectionIndex(CustomMenu.LayerKey.Options, CustomMenu.OptionKey.EnableDetail)] == Toggle.Off);
    }
    
    @Override
    public void reset(final Engine engine) throws Exception
    {
        if (players == null)
        {
            players = new Players();
        }
    }
    
    @Override
    public Rectangle getWindow()
    {
        return this.window;
    }
    
    @Override
    public void setWindow(final Rectangle window)
    {
        this.window = new Rectangle(window);
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        if (window != null)
            window = null;
        
        if (players != null)
        {
            players.dispose();
            players = null;
        }
        
        try
        {
            //recycle objects
            super.finalize();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Update all elements
     * @param engine Our game engine
     * @throws Exception 
     */
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (players != null)
        {
            players.update(engine);
        }
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        if (players != null)
        {
            players.render(graphics);
        }
    }
}