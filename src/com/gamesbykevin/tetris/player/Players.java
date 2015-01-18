package com.gamesbykevin.tetris.player;

import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.tetris.engine.Engine;
import com.gamesbykevin.tetris.shared.IElement;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * This object will contain the players in the game
 * @author GOD
 */
public final class Players implements Disposable, IElement
{
    //the list of players in the game
    List<Player> players;
    
    //y-coordinate for player board
    private static final int PLAYER_START_Y = 25;
    
    public Players(final boolean multiple)
    {
        this.players = new ArrayList<>();
        
        //objecs representing human/cpu
        Player human, cpu;
        
        if (multiple)
        {
            //create human player
            human = new Human();
            human.getBoard().setLocation(42, PLAYER_START_Y);
            add(human);
            
            cpu = new Cpu();
            cpu.getBoard().setLocation(426, PLAYER_START_Y);
            add(cpu);
        }
        else
        {
            //create human player
            human = new Human();
            human.getBoard().setLocation(234, PLAYER_START_Y);
            add(human);
        }
    }
    
    /**
     * Set the render for our players, all will be same
     * @param isometric If true blocks will be rendered isometric, false blocks will be rendered 2d
     */
    public void setIsometric(final boolean isometric)
    {
        for (int i = 0; i < players.size(); i++)
        {
            players.get(i).setIsometric(isometric);
        }
    }
    
    /**
     * Are the players rendering isometric?
     * @return true - if the first player in List is rendering isometric, false - if not or if the List of players is empty
     */
    public boolean hasIsometric()
    {
        if (players.isEmpty())
            return false;
        
        return players.get(0).hasIsometric();
    }
    
    /**
     * Reset all players, board(s), etc....
     */
    public void reset() throws Exception
    {
        for (int i = 0; i < players.size(); i++)
        {
            players.get(i).reset();
        }
    }
    
    private void add(final Player player)
    {
        this.players.add(player);
    }
    
    @Override
    public void dispose()
    {
        for (int i = 0; i < players.size(); i++)
        {
            players.get(i).dispose();
            players.set(i, null);
        }
        
        players.clear();
        players = null;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        for (int i = 0; i < players.size(); i++)
        {
            //get the current player
            Player player = players.get(i);
            
            //skip player if the game is over
            if (!player.hasGameover())
            {
                //if  there is a completed line, only update basic
                if (player.getBoard().hasComplete())
                {
                    player.updateBasic(engine);
                }
                else
                {
                    player.update(engine);
                }
            }
            else
            {
                player.reset();
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        for (int i = 0; i < players.size(); i++)
        {
            players.get(i).render(graphics);
        }
    }
}