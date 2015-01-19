package com.gamesbykevin.tetris.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.tetris.board.piece.*;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Polygon;

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
    
    //area at top where we check to see if a block exists for gameover
    public static final int START_RANGE = 2;
    
    //do we have a complete line
    private boolean complete = false;
    
    //the number of lines completed
    private int lines = 0;
    
    //the background of the board for 2d and isometric
    private Polygon background2d, backgroundIso;
    
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
     * Get the number of lines completed overall
     * @return The total number of lines completed overall
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
    public void removePiece(final Piece piece) throws Exception
    {
        for (int col = 0; col < board[0].length; col++)
        {
            for (int row = 0; row < board.length; row++)
            {
                if (hasBlock(col, row))
                {
                    //if the block belongs to the piece, remove it
                    if (getBlock(col, row).getId() == piece.getId())
                        setBlock(col, row, null);
                }
            }
        }
    }
    
    /**
     * Add the piece to the board where there isn't a block.<br>
     * This is used to add the last piece before a player has gameover
     * @param piece The piece we want to add
     */
    public void fillPiece(final Piece piece)
    {
        //add each block to the board where we can
        for (int i = 0; i < piece.getBlocks().size(); i++)
        {
            //get the current block
            Block block = piece.getBlocks().get(i);
            
            //calculate location for block
            int col = (int)(block.getCol() + piece.getCol());
            int row = (int)(block.getRow() + piece.getRow());
            
            //make sure we aren't placing this specific block out of bounds
            if (hasBounds(col, row))
            {
                //also make sure there isn't already a block here
                if (!hasBlock(col, row))
                {
                    //add block at location
                    setBlock(col, row, block);
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
            throw new Exception("A block already exists here and the piece can't be placed");
        
        //add each block to the board
        for (int i = 0; i < piece.getBlocks().size(); i++)
        {
            //get the current block
            Block block = piece.getBlocks().get(i);
            
            //calculate location for block
            int col = (int)(block.getCol() + piece.getCol());
            int row = (int)(block.getRow() + piece.getRow());
            
            //make sure we aren't placing this specific block out of bounds
            if (hasBounds(col, row))
            {
                //add block at location
                setBlock(col, row, block);
            }
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
                board[row + 1][col] = getBlock(col, row);
                
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
    public void clearCompletedRows() throws Exception
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
     * @throws exception
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
     * @return Will return true if at least 1 line is complete, false otherwise
     */
    public boolean markCompletedRow()
    {
        for (int row = 0; row < board.length; row++)
        {
            //if one row has been completed, set complete true
            if (hasCompletedRow(row))
            {
                //flag complete
                setComplete(true);
                
                //exit
                return true;
            }
        }
        
        //no completed rows
        return false;
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
     * Assign the block to the board
     * @param col Column
     * @param row Row
     * @param block The block to be placed
     */
    private void setBlock(final int col, final int row, final Block block)
    {
        this.board[row][col] = block;
    }
    
    private Block getBlock(final int col, final int row)
    {
        return this.board[row][col];
    }
    
    /**
     * Get the column height
     * @param col column
     * @return The height of the highest block in the given column, if no blocks exist 0 will be returned
     */
    private int getColumnHeight(final int col)
    {
        for (int row = 0; row < board.length; row++)
        {
            //if there is a block add the height
            if (hasBlock(col, row))
            {
                //return height
                return (Board.ROWS - row);
            }
        }
        
        return 0;
    }
    
    /**
     * Get the height of the highest block in each column
     * @return The total height of the highest block in all columns
     */
    public int getAggregateHeight()
    {
        int height = 0;
        
        for (int col = 0; col < board[0].length; col++)
        {
            //add height to total
            height += getColumnHeight(col);
        }
        
        //return the total height
        return height;
    }
    
    /**
     * Count the number of holes on the board
     * @return the total number of holes with at least a block above it
     */
    public int getHoleCount()
    {
        int count = 0;
        
        for (int col = 0; col < board[0].length; col++)
        {
            //did we hit a block yet
            boolean hitBlock = false;
            
            for (int row = 0; row < board.length; row++)
            {
                //if we found a block, flag it
                if (hasBlock(col, row))
                {
                    hitBlock = true;
                }
                else
                {
                    //if this is not a block and we already found one, then this is a hole
                    if (hitBlock)
                        count++;
                }
            }
        }
        
        //return the total number of holes
        return count;
    }
    
    /**
     * Calculate the bumpiness of the board, the differences in height on the board
     * @return The sum of the absolute differences between all two side-by-side columns
     */
    public int getBumpiness()
    {
        int bumpiness = 0;
        
        for (int col = 0; col < board[0].length - 1; col++)
        {
            //get the height of the 2 neighboring columns
            final int height1 = getColumnHeight(col);
            final int height2 = getColumnHeight(col + 1);
            
            //calculate bumpiness and add to total
            if (height1 > height2)
            {
                bumpiness += (height1 - height2);
            }
            else
            {
                bumpiness += (height2 - height1);
            }
        }
        
        //return bumpiness
        return bumpiness;
    }
    
    /**
     * Does a block already occupy this space?
     * @param piece The piece we want to check
     * @return true if a block already exists where the piece is located, false otherwise
     */
    public boolean hasBlock(final Piece piece)
    {
        for (int col = 0; col < board[0].length; col++)
        {
            for (int row = 0; row < board.length; row++)
            {
                //if a block exists here, and the block is part of the piece
                if (hasBlock(col, row) && piece.hasBlock(col, row))
                    return true;
            }
        }
        
        //no blocks exist here, return false
        return false;
    }
        
    /**
     * Does the block exist
     * @param col column
     * @param row row
     * @return true if a block exists at the specified location (col, row), false otherwise
     */
    public boolean hasBlock(final int col, final int row)
    {
        //if the location is out of bounds we can't have a block
        if (!hasBounds(col, row))
            return false;
        
        return (getBlock(col, row) != null);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        for (int col = 0; col < board[0].length; col++)
        {
            for (int row = 0; row < board.length; row++)
            {
                Block block = getBlock(col, row);
                
                if (block != null)
                {
                    block.dispose();
                    block = null;
                }
            }
        }
        
        board = null;
        background2d = null;
        backgroundIso = null;
    }
    
    /**
     * Assign background coordinates for board outline
     */
    public void assignBackground()
    {
        if (this.background2d == null)
            this.background2d = new Polygon();
        
        if (this.backgroundIso == null)
            this.backgroundIso = new Polygon();
        
            this.background2d.reset();
            this.background2d.addPoint((int)getX(), (int)getY());
            this.background2d.addPoint((int)(getX() + getWidth()), (int)getY());
            this.background2d.addPoint((int)(getX() + getWidth()), (int)(getY() + getHeight()));
            this.background2d.addPoint((int)getX(), (int)(getY() + getHeight()));
            
            this.backgroundIso.reset();
            this.backgroundIso.addPoint((int)(getX() + Block.getIsometricX(0, -1)), (int)(getY() + Block.getIsometricY(0, -1)));
            this.backgroundIso.addPoint((int)(getX() + Block.getIsometricX(Board.COLS, 0)), (int)(getY() + Block.getIsometricY(Board.COLS, 0)));
            this.backgroundIso.addPoint((int)(getX() + Block.getIsometricX(Board.COLS, Board.ROWS - 1)), (int)(getY() + Block.getIsometricY(Board.COLS, Board.ROWS - 1)));
            this.backgroundIso.addPoint((int)(getX() + Block.getIsometricX(0, Board.ROWS - 1)), (int)(getY() + Block.getIsometricY(0, Board.ROWS - 1)));
    }
    
    /**
     * Draw the board
     * @param graphics Object used to draw board
     * @param isometric Do we render the board isometric (true) or 2d (false)?
     */
    public void render(final Graphics graphics, final boolean isometric)
    {
        //set background color
        graphics.setColor(Color.BLACK);
        
        //fill the board outline
        graphics.fillPolygon((isometric) ? backgroundIso : background2d);
        
        //set color of outline
        graphics.setColor(Color.WHITE);
        
        //draw the board outline
        graphics.drawPolygon((isometric) ? backgroundIso : background2d);
        
        //draw blocks
        for (int row = 0; row < board.length; row++)
        {
            for (int col = 0; col < board[0].length; col++)
            {
                //only draw block if we have one
                if (hasBlock(col, row))
                {
                    final double drawX;
                    final double drawY;
                    
                    if (isometric)
                    {
                        //isometric coordinates
                        drawX = getX() + Block.getIsometricX(col, row);
                        drawY = getY() + Block.getIsometricY(col, row);
                        
                        //draw block
                        getBlock(col, row).renderIsometric(graphics, (int)drawX, (int)drawY);
                    }
                    else
                    {
                        //calculate coordinates, 2d coordinates.
                        drawX = getX() + (col * Block.WIDTH);
                        drawY = getY() + (row * Block.HEIGHT);
                        
                        //draw block
                        getBlock(col, row).render2d(graphics, (int)drawX, (int)drawY);
                    }
                }
            }
        }
    }
}