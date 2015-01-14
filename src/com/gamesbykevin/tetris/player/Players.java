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
    
    public Players()
    {
        this.players = new ArrayList<>();
        
        //create player
        Player player = new Cpu();
        player.getBoard().setLocation(0, 0);
        
        //add player
        add(player);
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