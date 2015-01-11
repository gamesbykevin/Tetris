package com.gamesbykevin.tetris.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.tetris.board.piece.*;
import java.awt.Color;

import java.awt.Graphics;

/**
 *  This is the board where the tetris pieces will be placed
 * @author GOD
 */
public final class Board extends Sprite implements Disposable
{
    //the table containing the blocks
    private Block[][] board;
    
    //the size of the board
    private static final int ROWS = 20;
    private static final int COLS = 10;
    
    //the default place to start the piece
    public static final int START_COL = (COLS / 2);
    public static final int START_ROW = 0;
    
    public Board()
    {
        //create a new board
        this.board = new Block[ROWS][COLS];
        
        //set the size of the board
        super.setDimensions(COLS * Block.WIDTH, ROWS * Block.HEIGHT);
        
        //set the bounds of the board to determine if the location is in bounds
        super.setBounds(0, COLS - 1, 0, ROWS - 1);
    }
    
    /**
     * Add the piece to the board
     * @param piece The piece we want to add
     * @throws Exception If a block already exists in place an exception will be thrown
     */
    public void add(final Piece piece) throws Exception
    {
        //if a block exists the piece can't be added
        if (hasBlock(piece))
            throw new Exception("A block already exists here and the piece can't be placed.");
        
        //add each block to the board
        for (int i = 0; i < piece.getBlocks().size(); i++)
        {
            //get the current block
            Block block = piece.getBlocks().get(i);
            
            //calculate location for block
            int col = (int)(block.getCol() + piece.getCol());
            int row = (int)(block.getRow() + piece.getRow());
            
            //add block at location
            add(col, row, block);
        }
    }
    
    /**
     * Is the piece located within the bounds of the board?<br>
     * Here we do not check if the piece collides with other pieces
     * @param piece The piece we want to check
     * @return true if piece is located within the board bounds, false otherwise
     */
    public boolean hasBounds(final Piece piece)
    {
        for (int i = 0; i < piece.getBlocks().size(); i++)
        {
            //get the current location of this block
            double col = piece.getBlocks().get(i).getCol() + piece.getCol();
            double row = piece.getBlocks().get(i).getRow() + piece.getRow();
            
            //if this location is not within, then the piece is not in bounds
            if (!super.hasBounds(col, row))
                return false;
        }
        
        //no blocks contained in the piece were out of bounds, return true
        return true;
    }
    
    /**
     * Get the board array containing the placed blocks.
     * @return The board array that may contain blocks
     */
    public Block[][] getBlocks()
    {
        return this.board;
    }
    
    /**
     * Add block to the board
     * @param col Column
     * @param row Row
     * @param block The block to be placed
     */
    private void add(final int col, final int row, final Block block)
    {
        this.board[row][col] = block;
    }
    
    /**
     * Does a block already occupy this space?
     * @param piece The piece we want to check
     * @return true if a block already exists where the piece is located, false otherwise
     */
    public boolean hasBlock(final Piece piece)
    {
        for (int i = 0; i < piece.getBlocks().size(); i++)
        {
            int col = (int)(piece.getBlocks().get(i).getCol() + piece.getCol());
            int row = (int)(piece.getBlocks().get(i).getRow() + piece.getRow());
            
            //if there is a block here return true
            if (hasBlock(col, row))
                return true;
        }
        
        //no blocks exist here, return false
        return false;
    }
        
    private boolean hasBlock(final int col, final int row)
    {
        return (this.board[row][col] != null);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        for (int row = 0; row < board.length; row++)
        {
            for (int col = 0; col < board[0].length; col++)
            {
                Block block = board[row][col];
                
                if (block != null)
                {
                    block.dispose();
                    block = null;
                }
            }
        }
        
        board = null;
    }
    
    /**
     * Draw the board.
     * @param graphics 
     */
    public void render(final Graphics graphics)
    {
        int startX = (int)getX();
        int startY = (int)getY();
        
        //draw blocks
        for (int row = 0; row < board.length; row++)
        {
            for (int col = 0; col < board[0].length; col++)
            {
                //only draw block if we have one
                if (hasBlock(col, row))
                {
                    //calculate drawing coordinate
                    int drawX = startX + (col * Block.WIDTH);
                    int drawY = startY + (row * Block.HEIGHT);
                    
                    //set color
                    graphics.setColor(board[row][col].getColor());

                    //fill block
                    graphics.fillRect(drawX, drawY, Block.WIDTH, Block.HEIGHT);
                }
            }
        }
        
        //draw block outline
        for (int row = 0; row < board.length; row++)
        {
            for (int col = 0; col < board[0].length; col++)
            {
                //only draw outline if we have a block
                //if (hasBlock(col, row))
                //{
                    //calculate drawing coordinate
                    int drawX = startX + (col * Block.WIDTH);
                    int drawY = startY + (row * Block.HEIGHT);

                    //set outline color
                    graphics.setColor(Color.WHITE);

                    //draw outline
                    graphics.drawRect(drawX, drawY, Block.WIDTH, Block.HEIGHT);
                //}
            }
        }
        
        //set color of outline
        graphics.setColor(Color.WHITE);
        
        //now draw outline of board
        graphics.drawRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }
}