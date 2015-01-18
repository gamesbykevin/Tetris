package com.gamesbykevin.tetris.board.piece;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

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
    
    //these polygon objects are for the isometric blocks
    private Polygon topSide;
    private Polygon frontSide;
    private Polygon rightSide;
    
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
        setColor(color);
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
    
    public void setColor(final Color color)
    {
        this.color = color;
    }
    
    public Color getColor()
    {
        return this.color;
    }
    
    /**
     * Assign polygon coordinates for isometric rendering
     * @param x x-coordinate
     * @param y y-coordinate
     */
    private void assignCoordinates(final int x, final int y)
    {
        if (frontSide == null)
            frontSide = new Polygon();
        
        frontSide.reset();
        frontSide.addPoint(x, y);
        frontSide.addPoint(x + (WIDTH/2), y + (HEIGHT/2));
        frontSide.addPoint(x + (WIDTH/2), y);
        frontSide.addPoint(x, y - (HEIGHT/2));
        
        if (rightSide == null)
            rightSide = new Polygon();
        
        rightSide.reset();
        rightSide.addPoint(x + (WIDTH/2), y + (HEIGHT/2));
        rightSide.addPoint(x + WIDTH, y);
        rightSide.addPoint(x + WIDTH, y - (HEIGHT / 2));
        rightSide.addPoint(x + (WIDTH/2), y);
        
        if (topSide == null)
            topSide = new Polygon();
        
        topSide.reset();
        topSide.addPoint(x, y - (HEIGHT/2));
        topSide.addPoint(x + (WIDTH/2), y);
        topSide.addPoint(x + WIDTH, y - (HEIGHT/2));
        topSide.addPoint(x + (WIDTH/2), y - HEIGHT);
    }
    
    @Override
    public void dispose()
    {
        this.color = null;
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param piece The piece containing the column, row location
     * @return y-coordinate
     */
    public static int getIsometricY(final Piece piece)
    {
        return getIsometricY(piece.getCol(), piece.getRow());
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param block The block containing the column, row location
     * @return y-coordinate
     */
    public static int getIsometricY(final Block block)
    {
        return getIsometricY(block.getCol(), block.getRow());
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param col The column location
     * @param row The row location
     * @return y-coordinate
     */
    public static int getIsometricY(final double col, final double row)
    {
        return (int)((col + row) * (HEIGHT / 2));
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param piece The piece containing the column, row location
     * @return x-coordinate
     */
    public static int getIsometricX(final Piece piece)
    {
        return getIsometricX(piece.getCol(), piece.getRow());
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param block The block containing the column, row location
     * @return x-coordinate
     */
    public static int getIsometricX(final Block block)
    {
        return getIsometricX(block.getCol(), block.getRow());
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param col The column location
     * @param row The row location
     * @return x-coordinate
     */
    public static int getIsometricX(final double col, final double row)
    {
        return (int)((col - row) * (WIDTH / 2));
    }
    
    public void renderIsometric(final Graphics graphics, final double x, final double y)
    {
        //assign polygon coordinates
        assignCoordinates((int)x, (int)y);
        
        //set block color and fill shape
        graphics.setColor(getColor());
        graphics.fillPolygon(topSide);
        graphics.fillPolygon(rightSide);
        graphics.fillPolygon(frontSide);
        
        //set outline color and draw outline
        graphics.setColor(Color.WHITE);
        graphics.drawPolygon(frontSide);
        graphics.drawPolygon(rightSide);
        graphics.drawPolygon(topSide);
    }
    
    /**
     * Draw the block
     * @param graphics Object used to create image
     * @param outline Do we draw the outline
     * @param x Starting x-coordinate of location
     * @param y Starting y-coordinate of location
     */
    public void render2d(final Graphics graphics, final int x, final int y)
    {
        //set block color
        graphics.setColor(getColor());
        
        //fill the block
        graphics.fillRect(x, y, WIDTH, HEIGHT);
     
        //set outline color
        graphics.setColor(Color.WHITE);

        //draw the outline
        graphics.drawRect(x, y, WIDTH, HEIGHT);
    }
}