package com.gamesbykevin.tetris.board.piece;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;

import java.awt.Color;

/**
 * This is a Tetris block.<br> 
 * Multiple blocks form a Tetris piece
 * @author GOD
 */
public final class Block extends Cell implements Disposable
{
    //the dimensions of each block
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    
    //the color of the block
    private Color color;
    
    //all blocks that are part of the same piece will have the same id
    private long id;
    
    /**
     * Create new block
     * @param col Column
     * @param row Row
     * @param color Color of block
     * @param id The id, all blocks that are part of the same piece will have the same id
     */
    protected Block(final int col, final int row, final Color color, final long id)
    {
        //call to parent constructor
        super(col, row);
        
        //set the id
        setId(id);
        
        //assign the color of the block
        this.color = color;
    }
    
    /**
     * Get the block id.<br>
     * All blocks that are part of the same piece will have the same id.
     * @return The id of the piece this block belongs to.
     */
    public long getId()
    {
        return this.id;
    }
    
    private void setId(final long id)
    {
        this.id = id;
    }
    
    public Color getColor()
    {
        return this.color;
    }
    
    @Override
    public void dispose()
    {
        this.color = null;
    }
}
