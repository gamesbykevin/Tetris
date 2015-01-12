package com.gamesbykevin.tetris.player;

import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.tetris.engine.Engine;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * CPU opponent
 * @author GOD
 */
public final class Cpu extends Player implements Disposable
{
    public Cpu()
    {
        super();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update common elements
        super.updateBasic(engine);

        //make sure there is a current piece in play
        if (getPiece() != null)
        {
            
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        super.render(graphics);
    }
}