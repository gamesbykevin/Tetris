package com.gamesbykevin.tetris.board.piece;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.tetris.board.Board;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Tetris piece and the blocks that make the piece
 * @author GOD
 */
public final class Piece extends Sprite implements Disposable
{
    //the list of blocks that make the tetris piece
    private List<Block> blocks;
    
    /**
     * Each piece can be rotated 4 times
     */
    public static final int TOTAL_ROTATIONS = 4;
    
    /**
     * The different possible pieces
     */
    public static final int PIECE_ONE   = 0;
    public static final int PIECE_TWO   = 1;
    public static final int PIECE_THREE = 2;
    public static final int PIECE_FOUR  = 3;
    public static final int PIECE_FIVE  = 4;
    public static final int PIECE_SIX   = 5;
    public static final int PIECE_SEVEN = 6;
    
    /**
     * The different colors for each piece
     */
    public static final Color PIECE_ONE_COLOR = Color.BLUE;
    public static final Color PIECE_TWO_COLOR = Color.CYAN;
    public static final Color PIECE_THREE_COLOR = Color.GRAY;
    public static final Color PIECE_FOUR_COLOR = Color.YELLOW;
    public static final Color PIECE_FIVE_COLOR = Color.GREEN;
    public static final Color PIECE_SIX_COLOR = Color.ORANGE;
    public static final Color PIECE_SEVEN_COLOR = Color.RED;
    
    //array of all possible pieces
    public static final int[] PIECES = {PIECE_ONE, PIECE_TWO, PIECE_THREE, PIECE_FOUR, PIECE_FIVE, PIECE_SIX, PIECE_SEVEN};
    
    //keep track of the number of rotations
    private int rotations = 0;
    
    public Piece(final int col, final int row, final int type) throws Exception
    {
        //call to parent constructor
        super();
        
        //assign the start base location
        super.setCol(col);
        super.setRow(row);
        
        //create new list of blocks
        this.blocks = new ArrayList<>();
        
        switch (type)
        {
            /**
             * Straight line
             */
            case PIECE_ONE:
                add(0, 0, PIECE_ONE_COLOR);
                add(1, 0, PIECE_ONE_COLOR);
                add(2, 0, PIECE_ONE_COLOR);
                add(3, 0, PIECE_ONE_COLOR);
                break;
                
            /**
             * L
             */
            case PIECE_TWO:
                add(0, 0, PIECE_TWO_COLOR);
                add(0, 1, PIECE_TWO_COLOR);
                add(0, 2, PIECE_TWO_COLOR);
                add(1, 2, PIECE_TWO_COLOR);
                break;
                
            /**
             * J
             */
            case PIECE_THREE:
                add(0,  0, PIECE_THREE_COLOR);
                add(0,  1, PIECE_THREE_COLOR);
                add(0,  2, PIECE_THREE_COLOR);
                add(-1, 2, PIECE_THREE_COLOR);
                break;
                
            /**
             * Square
             */
            case PIECE_FOUR:
                add(0,  0, PIECE_FOUR_COLOR);
                add(1,  0, PIECE_FOUR_COLOR);
                add(1,  1, PIECE_FOUR_COLOR);
                add(0,  1, PIECE_FOUR_COLOR);
                break;
                
            /**
             * S
             */
            case PIECE_FIVE:
                add(0,  0, PIECE_FIVE_COLOR);
                add(1,  0, PIECE_FIVE_COLOR);
                add(0,  1, PIECE_FIVE_COLOR);
                add(-1,  1, PIECE_FIVE_COLOR);
                break;
            
            /**
             * half-plus
             */
            case PIECE_SIX:
                add(0,  0, PIECE_SIX_COLOR);
                add(0,  1, PIECE_SIX_COLOR);
                add(1,  1, PIECE_SIX_COLOR);
                add(-1, 1, PIECE_SIX_COLOR);
                break;
                
            /**
             * Z
             */
            case PIECE_SEVEN:
                add(0,  0, PIECE_SEVEN_COLOR);
                add(-1, 0, PIECE_SEVEN_COLOR);
                add(0,  1, PIECE_SEVEN_COLOR);
                add(1,  1, PIECE_SEVEN_COLOR);
                break;
                
            default:
                throw new Exception("Piece is not setup here - " + type);
        }
    }
    
    /**
     * Is the block part of the piece
     * @param col column we are searching for
     * @param row row we are searching for
     * @return true if the location matches a block, false otherwise
     */
    public boolean hasBlock(final int col, final int row)
    {
        for (int i = 0; i < getBlocks().size(); i++)
        {
            //get the current block
            final Block block = getBlocks().get(i);
            
            //if the location matches, return true
            if (getCol() + block.getCol() == col && getRow() + block.getRow() == row)
                return true;
        }
        
        //we did not find a match
        return false;
    }
    
    /**
     * Get the total height of this piece.<br>
     * The very bottom of the board will have a height of 1 and the row above will have a height of 2, etc...
     * @return The sum of all the rows for each block in the piece
     */
    public int getTotalHeight()
    {
        //the total height
        int total = 0;
        
        for (int i = 0; i < getBlocks().size(); i++)
        {
            //add the height of this block to the total
            total += (Board.ROWS - ((getRow() + getBlocks().get(i).getRow()) + 1));
        }
        
        //return the total height
        return total;
    }
    
    /**
     * Is any block in this piece above the ceiling
     * @return true if at least 1 block has a row less than 0, false otherwise
     */
    public boolean isAboveCeiling()
    {
        for (int i = 0; i < getBlocks().size(); i++)
        {
            if (getRow() + getBlocks().get(i).getRow() < 0)
                return true;
        }
        
        //none were on the floor
        return false;
    }
    
    /**
     * Check if any of the blocks are located on the floor of the board
     * @return true if at least 1 block is on the same row as the floor of the board, false otherwise
     */
    public boolean hasFloor()
    {
        for (int i = 0; i < getBlocks().size(); i++)
        {
            if (getRow() + getBlocks().get(i).getRow() == Board.ROWS - 1)
                return true;
        }
        
        //none were on the floor
        return false;
    }
    
    /**
     * Add block to tetris piece
     * @param col Column position from the piece location
     * @param row Row position from the piece location
     * @param color Color of block
     */
    private void add(final int col, final int row, final Color color)
    {
        blocks.add(new Block(col, row, color, super.getId()));
    }
    
    /**
     * Get the blocks that form the piece
     * @return The list of blocks that form the piece
     */
    public List<Block> getBlocks()
    {
        return this.blocks;
    }
    
    /**
     * Set the rotation
     * @param rotations The number rotation we are on will range from 0 - 3
     */
    public void setRotations(final int rotations)
    {
        //assign number
        this.rotations = rotations;
        
        //if out of range reset to 0
        if (rotations < 0 || rotations >= TOTAL_ROTATIONS)
            this.rotations = 0;
    }
    
    /**
     * Get the number of rotations
     * @return The number of rotations ranging from 0 - 3
     */
    public int getRotation()
    {
        return this.rotations;
    }
    
    /**
     * Rotate the piece 90 degrees counter-clockwise
     */
    public void rotateCounterClockwise()
    {
        for (int i = 0; i < blocks.size(); i++)
        {
            Block block = blocks.get(i);
            
            //get current location
            final double col = block.getCol();
            final double row = block.getRow();
         
            //rotate coordinate
            block.setCol(-row);
            block.setRow(col);
        }
        
        //keep track of rotation
        setRotations(getRotation() - 1);
    }
    
    /**
     * Rotate the piece 90 degrees clockwise
     */
    public void rotateClockwise()
    {
        for (int i = 0; i < blocks.size(); i++)
        {
            Block block = blocks.get(i);
            
            //get current location
            final double col = block.getCol();
            final double row = block.getRow();
         
            //rotate coordinate
            block.setCol(row);
            block.setRow(-col);
        }
        
        //keep track of rotation
        setRotations(getRotation() + 1);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        for (int i = 0; i < blocks.size(); i++)
        {
            blocks.get(i).dispose();
            blocks.set(i, null);
        }

        blocks.clear();
        blocks = null;
    }
    
    /**
     * Draw the piece.
     * @param x x-coordinate where piece starts
     * @param y y-coordinate where piece starts
     * @param graphics Object used to draw image
     */
    public void render(final int x, final int y, final Graphics graphics)
    {
        for (int i = 0; i < getBlocks().size(); i++)
        {
            Block block = getBlocks().get(i);
            
            //set color
            graphics.setColor(block.getColor());
            
            final int drawX = x + (int)(block.getCol() * Block.WIDTH);
            final int drawY = y + (int)(block.getRow() * Block.HEIGHT);
            
            //fill the block
            graphics.fillRect(drawX, drawY, Block.WIDTH, Block.HEIGHT);
        }
        
        for (int i = 0; i < getBlocks().size(); i++)
        {
            Block block = getBlocks().get(i);
            
            //set outline color
            graphics.setColor(Color.WHITE);
            
            final int drawX = x + (int)(block.getCol() * Block.WIDTH);
            final int drawY = y + (int)(block.getRow() * Block.HEIGHT);
            
            //fill the block
            graphics.drawRect(drawX, drawY, Block.WIDTH, Block.HEIGHT);
        }
    }
}