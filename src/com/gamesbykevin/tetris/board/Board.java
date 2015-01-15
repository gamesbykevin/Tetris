package com.gamesbykevin.tetris.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.tetris.player.Cpu;
import com.gamesbykevin.tetris.board.piece.*;
import com.gamesbykevin.tetris.shared.Shared;
import java.awt.Color;

import java.awt.Graphics;

/**
 * This is the board where the tetris pieces will be placed
 * @author GOD
 */
public final class Board extends Sprite implements Disposable
{
    //the table containing the blocks
    private Block[][] board;
    
    //the size of the board
    public static final int ROWS = 20;
    public static final int COLS = 10;
    
    //the default place to start the piece
    public static final int START_COL = (COLS / 2);
    public static final int START_ROW = 0;
    
    //do we have a complete line
    private boolean complete = false;
    
    //the number of lines completed
    private int lines = 0;
    
    public Board()
    {
        //create a new board
        this.board = new Block[ROWS][COLS];
        
        //set the size of the board
        super.setDimensions(COLS * Block.WIDTH, ROWS * Block.HEIGHT);
        
        //set the bounds of the board to determine if the location is in bounds
        super.setBounds(0, COLS - 1, 0, ROWS - 1);
        
        //reset the board
        reset();
    }
    
    /**
     * Set the number of lines completed
     * @param lines The total number of lines completed overall
     */
    public void setLines(final int lines)
    {
        this.lines = lines;
    }
    
    /**
     * Get the number of lines completed
     * @return The total number of lines completed
     */
    public int getLines()
    {
        return this.lines;
    }
    
    /**
     * Mark a line completion
     * @param complete true if all columns in 1 row have existing blocks
     */
    public void setComplete(final boolean complete)
    {
        this.complete = complete;
    }
    
    /**
     * Do we have at least 1 completed line
     * @return true if at least 1 row is complete, false otherwise
     */
    public boolean hasComplete()
    {
        return this.complete;
    }
    
    /**
     * Remove all the blocks from the board that have the same id as the piece.<br>
     * @param piece The piece we want to match
     */
    public void removePiece(final Piece piece)
    {
        for (int row = 0; row < board.length; row++)
        {
            for (int col = 0; col < board[0].length; col++)
            {
                if (hasBlock(col, row))
                {
                    //if the block belongs to the piece, remove it
                    if (board[row][col].getId() == piece.getId())
                        setBlock(col, row, null);
                }
            }
        }
    }
    
    /**
     * Add the piece to the board
     * @param piece The piece we want to add
     * @throws Exception If a block already exists in place an exception will be thrown
     */
    public void addPiece(final Piece piece) throws Exception
    {
        //if a block exists the piece can't be added
        if (hasBlock(piece))
        {
            System.out.println("Lines = " + this.getLines());
            throw new Exception("A block already exists here and the piece can't be placed.");
        }
        
        //add each block to the board
        for (int i = 0; i < piece.getBlocks().size(); i++)
        {
            //get the current block
            Block block = piece.getBlocks().get(i);
            
            //calculate location for block
            int col = (int)(block.getCol() + piece.getCol());
            int row = (int)(block.getRow() + piece.getRow());
            
            //add block at location
            setBlock(col, row, block);
        }
    }
    
    /**
     * Is the specified row empty
     * @param row The row we want to check
     * @return true if all columns are empty for the given row, false otherwise
     */
    public boolean hasEmptyRow(final int row)
    {
        for (int col = 0; col < board[0].length; col++)
        {
            //if a block exists the row is not empty
            if (hasBlock(col, row))
                return false;
        }
        
        //all blocks are empty return true
        return true;
    }
    
    /**
     * Drop the blocks of all rows that have blocks but have 0 blocks underneath
     */
    public void dropBlocks()
    {
        //do we check all rows
        boolean check = true;
        
        while(check)
        {
            //no longer check all rows
            check = false;
            
            for (int row = 0; row < board.length - 1; row++)
            {
                //if the current row is not empty and the one below it is
                if (!hasEmptyRow(row) && hasEmptyRow(row + 1))
                {
                    //move the blocks in the current row to the row below
                    dropRow(row);
                    
                    //we need to check the rows again
                    check = true;
                }
            }
        }
    }
    
    /**
     * Move all blocks in the specified row to the row below
     * @param row The row containing blocks we want to move south
     */
    private void dropRow(final int row)
    {
        for (int col = 0; col < board[0].length; col++)
        {
            if (hasBlock(col, row))
            {
                //move the block to the row below
                board[row + 1][col] = board[row][col];
                
                //remove the previous block
                board[row][col] = null;
            }
        }
    }
    
    /**
     * Is the specified row complete?
     * @param row The row we want to check
     * @return true if every column in this row is a block, false otherwise
     */
    public boolean hasCompletedRow(final int row)
    {
        for (int col = 0; col < board[0].length; col++)
        {
            if (!hasBlock(col, row))
                return false;
        }
        
        return true;
    }
    
    /**
     * Get the completer of rows
     * @return The total number of rows that have a block across all columns
     */
    public int getCompletedRowCount()
    {
        int count = 0;
        
        for (int row = 0; row < board.length; row++)
        {
            //if this row has been completed, add to count
            if (hasCompletedRow(row))
                count++;
        }
        
        //return the total count
        return count;
    }
    
    /**
     * Remove all blocks that are part of a completed row(s).<br>
     * Will also add the number of completed rows to the total
     */
    public void clearCompletedRows()
    {
        for (int row = 0; row < board.length; row++)
        {
            //if this row has been completed, clear it
            if (hasCompletedRow(row))
            {
                //clear the row
                clearRow(row);
            }
        }
    }
    
    /**
     * Remove all blocks for a given row
     * @param row The row we want to remove all blocks from
     */
    private void clearRow(final int row)
    {
        for (int col = 0; col < board[0].length; col++)
        {
            //set the location as null
            setBlock(col, row, null);
        }
    }
    
    /**
     * Remove all blocks on the board
     */
    public void reset()
    {
        //there are no completed lines
        setComplete(false);
        
        //reset lines completed
        setLines(0);
        
        //clear each row
        for (int row = 0; row < board.length; row++)
        {
            clearRow(row);
        }
    }
    
    /**
     * Check the board for a completed row and flag if there is at least 1 completed row
     */
    public void markCompletedRow()
    {
        for (int row = 0; row < board.length; row++)
        {
            //if one row has been completed, set complete true
            if (hasCompletedRow(row))
            {
                //flag complete
                setComplete(true);
                
                //exit loop
                break;
            }
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
     * Get the penalty for blocks placed directly above an empty space
     * @param piece The piece we placed
     * @return The total score penalty for empty blocks
     */
    public double getBlockadeScore(final Piece piece)
    {
        //the count
        int count = 0;
        
        for (int row = 0; row < board.length; row++)
        {
            for (int col = 0; col < board[0].length; col++)
            {
                //make sure location is within bounds
                if (hasBounds(col, row))
                {
                    //if the piece is part of this location and is also part of the board
                    if (piece.hasBlock(col, row) && hasBlock(col, row))
                    {
                        //if directly below is in bounds and there is not a block
                        if (hasBounds(col, row + 1) && !hasBlock(col, row + 1))
                            count++;
                    }
                }
            }
        }
        
        //return the total score
        return (count * Cpu.PENALTY_BLOCKADE);
    }
    
    /**
     * Get the penalty for all the holes on the <b>entire</b> board
     * @return The total score for each block that has an empty space below it.
     */
    public double getHolesScore()
    {
        double score = 0;
        
        for (int row = 0; row < board.length; row++)
        {
            for (int col = 0; col < board[0].length; col++)
            {
                //make sure current location and below are in bounds
                if (hasBounds(col, row) && hasBounds(col, row + 1))
                {
                    //if we have a block here, but not below it is a hole
                    if (hasBlock(col, row) && !hasBlock(col, row + 1))
                        score += Cpu.PENALTY_HOLES;
                }
            }
        }
        
        //return the total score
        return score;
    }
    
    /**
     * Get the total score for the piece placed.<br>
     * Will check the following:<br>
     * 1. Blocks that border each other (west, east, south directions)
     * 2. Blocks that border the side walls
     * 3. Blocks that border the floor
     * @param piece The piece we placed on the board
     * @return The total score of the piece placed
     */
    public double getCoveredBlockScore(final Piece piece)
    {
        //our return score
        double score = 0;
        
        for (int row = 0; row < board.length; row++)
        {
            for (int col = 0; col < board[0].length; col++)
            {
                //make sure location is within bounds
                if (hasBounds(col, row))
                {
                    //also make sure the location is part of the piece
                    if (piece.hasBlock(col, row))
                    {
                        //if we don't have bounds to the left we are touching a wall
                        if (!hasBounds(col - 1, row))
                        {
                            score += Cpu.BONUS_WALL_COVER;
                        }
                        else
                        {
                            //we are inbounds and have a bordering block, add touching block score
                            if (hasBlock(col - 1, row))
                                score += Cpu.BONUS_BLOCK_COVER;
                        }
                        
                        //if we don't have bounds to the right we are touching a wall
                        if (!hasBounds(col + 1, row))
                        {
                            score += Cpu.BONUS_WALL_COVER;
                        }
                        else
                        {
                            //we are inbounds and have a bordering block, add touching block score
                            if (hasBlock(col + 1, row))
                                score += Cpu.BONUS_BLOCK_COVER;
                        }
                        
                        //if we don't have bounds below we are touching the floor
                        if (!hasBounds(col, row + 1))
                        {
                            score += Cpu.BONUS_FLOOR_COVER;
                        }
                        else
                        {
                            //we are inbounds and have a bordering block, add touching block score
                            if (hasBlock(col, row + 1))
                                score += Cpu.BONUS_BLOCK_COVER;
                        }
                    }
                }
            }
        }
        
        //return total score
        return score;
    }
    
    /**
     * Assign the block to the board
     * @param col Column
     * @param row Row
     * @param block The block to be placed
     */
    private void setBlock(final int col, final int row, final Block block)
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
        for (int row = 0; row < board.length; row++)
        {
            for (int col = 0; col < board[0].length; col++)
            {
                //if a block exists here, and the block is part of the piece
                if (hasBlock(col, row) && piece.hasBlock(col, row))
                    return true;
            }
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
     * Draw the board
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