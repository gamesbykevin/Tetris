package com.gamesbykevin.tetris.board.piece;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

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
                add(-2, 0, PIECE_ONE_COLOR);
                add(-1, 0, PIECE_ONE_COLOR);
                add(0, 0, PIECE_ONE_COLOR);
                add(1, 0, PIECE_ONE_COLOR);
                break;
                
            /**
             * J
             */
            case PIECE_TWO:
                add(0, 0, PIECE_TWO_COLOR);
                add(0, 1, PIECE_TWO_COLOR);
                add(1, 1, PIECE_TWO_COLOR);
                add(2, 1, PIECE_TWO_COLOR);
                break;
                
            /**
             * L
             */
            case PIECE_THREE:
                add(0,  0, PIECE_THREE_COLOR);
                add(0,  1, PIECE_THREE_COLOR);
                add(-1, 1, PIECE_THREE_COLOR);
                add(-2, 1, PIECE_THREE_COLOR);
                break;
                
            /**
             * Square
             */
            case PIECE_FOUR:
                add(-1,  0, PIECE_FOUR_COLOR);
                add(0,  0, PIECE_FOUR_COLOR);
                add(0,  1, PIECE_FOUR_COLOR);
                add(-1,  1, PIECE_FOUR_COLOR);
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