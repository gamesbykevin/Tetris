package com.gamesbykevin.tetris.player;

import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.tetris.engine.Engine;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * The human controlled player
 * @author GOD
 */
public final class Human extends Player implements Disposable
{
    public Human()
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
            //get keyboard input
            final Keyboard keyboard = engine.getKeyboard();

            if (keyboard.hasKeyPressed(KeyEvent.VK_LEFT))
            {
                //move piece west
                getPiece().decreaseCol();

                //if we are out of bounds or intersecting another block on the board
                if (!getBoard().hasBounds(getPiece()) || getBoard().hasBlock(getPiece()))
                {
                    //move piece back to previous
                    getPiece().increaseCol();
                }

                //remove the event
                keyboard.removeKeyPressed(KeyEvent.VK_LEFT);
            }
            else if (keyboard.hasKeyPressed(KeyEvent.VK_RIGHT))
            {
                //move piece east
                getPiece().increaseCol();

                //if we are out of bounds or intersecting another block on the board
                if (!getBoard().hasBounds(getPiece()) || getBoard().hasBlock(getPiece()))
                {
                    //move piece back to previous
                    getPiece().decreaseCol();
                }

                //remove the event
                keyboard.removeKeyPressed(KeyEvent.VK_RIGHT);
            }
            else if (keyboard.hasKeyPressed(KeyEvent.VK_UP))
            {
                //rotate the piece
                super.rotate();
                
                //remove the event
                keyboard.removeKeyPressed(KeyEvent.VK_UP);
            }
            else if (keyboard.hasKeyPressed(KeyEvent.VK_DOWN))
            {
                //automatically expire timer so piece drops
                getTimer().setRemaining(0);

                //remove the event
                keyboard.removeKeyPressed(KeyEvent.VK_DOWN);
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        super.render(graphics);
    }
}