package com.gamesbykevin.tetris.board.piece;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.tetris.board.Board;
import com.gamesbykevin.tetris.menu.CustomMenu;

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
    public static final Color PIECE_FOUR_COLOR = Color.MAGENTA;
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
                add(1, 0, PIECE_TWO_COLOR);
                add(0, 1, PIECE_TWO_COLOR);
                add(0, 2, PIECE_TWO_COLOR);
                break;
                
            /**
             * J
             */
            case PIECE_THREE:
                add(0,  0, PIECE_THREE_COLOR);
                add(1,  0, PIECE_THREE_COLOR);
                add(1,  1, PIECE_THREE_COLOR);
                add(1, 2, PIECE_THREE_COLOR);
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
     * Is this piece between the minimum, maximum columns on the tetris board?
     * @return True if all blocks that make the piece are within the min,max of the board. False otherwise
     */
    public boolean hasVerticalBounds()
    {
        for (int i = 0; i < getBlocks().size(); i++)
        {
            //get the current block
            final Block block = getBlocks().get(i);
            
            //if the column is less than the minimum
            if (getCol() + block.getCol() < 0)
                return false;
            
            //if the column is greater than the maximum
            if (getCol() + block.getCol() >= Board.COLS)
                return false;
        }
        
        //none of the blocks are out of bounds
        return true;
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
     * Render the blocks (for isometric rendering)
     * @param renderIndex Different rendering calls to order the blocks differently
     */
    private void sortBlocks(final int renderIndex)
    {
        //were objects swapped
        boolean swapped = true;
        int j = 0;
        
        //continue as long as objects have been swapped
        while (swapped)
        {
            swapped = false;
            j++;
            
            //sort each block
            for (int i = 0; i < getBlocks().size() - j; i++) 
            {
                //check if we can sort these 2 blocks
                final Block block1 = getBlocks().get(i);
                final Block block2 = getBlocks().get(i + 1);
                
                switch (renderIndex)
                {
                    case CustomMenu.RENDER_2D:
                    case CustomMenu.RENDER_ISOMETRIC_1:
                        if (block1.getRow() > block2.getRow() || block1.getRow() >= block2.getRow() && block1.getCol() > block2.getCol())
                        {
                            //swap objects
                            getBlocks().set(i, block2);
                            getBlocks().set(i + 1, block1);

                            //flag that objects are swapped
                            swapped = true;
                        }
                        break;
                        
                    case CustomMenu.RENDER_ISOMETRIC_2:
                    case CustomMenu.RENDER_ISOMETRIC_3:
                        if (block1.getRow() < block2.getRow() || block1.getRow() <= block2.getRow() && block1.getCol() > block2.getCol())
                        {
                            //swap objects
                            getBlocks().set(i, block2);
                            getBlocks().set(i + 1, block1);

                            //flag that objects are swapped
                            swapped = true;
                        }
                        break;
                }
                
            }
        }
    }
    
    /**
     * Draw the piece.
     * @param graphics Object used to draw image
     * @param x x-coordinate where piece starts
     * @param y y-coordinate where piece starts
     * @param renderIndex How do we render the piece
     */
    public void render(final Graphics graphics, final double x, final double y, final int renderIndex)
    {
        //sort the blocks
        sortBlocks(renderIndex);
        
        //draw every block
        for (int i = 0; i < getBlocks().size(); i++)
        {
            //get the current block
            final Block block = getBlocks().get(i);
            
            final double startX;
            final double startY;
            
            switch (renderIndex)
            {
                case CustomMenu.RENDER_2D:
                default:
                    startX = x + Block.get2dX(block);
                    startY = y + Block.get2dY(block);
                    break;
                    
                case CustomMenu.RENDER_ISOMETRIC_1:
                    startX = x + Block.getIsometric1X(block);
                    startY = y + Block.getIsometric1Y(block);
                    break;
                    
                case CustomMenu.RENDER_ISOMETRIC_2:
                    startX = x + Block.getIsometric2X(block);
                    startY = y + Block.getIsometric2Y(block);
                    break;
                    
                case CustomMenu.RENDER_ISOMETRIC_3:
                    startX = x + Block.getIsometric3X(block);
                    startY = y + Block.getIsometric3Y(block);
                    break;
            }
            
            //draw block
            block.render(graphics, startX, startY, renderIndex);
        }
    }
}