package com.gamesbykevin.tetris.board.piece;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.tetris.menu.CustomMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.UUID;

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
    
    //the dimensions of each block
    public static final int ISOMETRIC_WIDTH = 30;
    public static final int ISOMETRIC_HEIGHT = 30;
    
    //the color of the block
    private Color color;
    
    //all blocks that are part of the same piece will have the same id
    private UUID id;
    
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
    protected Block(final int col, final int row, final Color color, final UUID id)
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
    public UUID getId()
    {
        return this.id;
    }
    
    private void setId(final UUID id)
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
     * @param renderIndex the type of rendering we are doing
     */
    private void assignCoordinates(final int x, final int y, final int renderIndex)
    {
        if (frontSide == null)
            frontSide = new Polygon();
        if (rightSide == null)
            rightSide = new Polygon();
        if (topSide == null)
            topSide = new Polygon();
        
        switch (renderIndex)
        {
            case CustomMenu.RENDER_ISOMETRIC_1:
                frontSide.reset();
                frontSide.addPoint(x, y);
                frontSide.addPoint(x + (WIDTH/2), y + (HEIGHT/2));
                frontSide.addPoint(x + (WIDTH/2), y);
                frontSide.addPoint(x, y - (HEIGHT/2));

                rightSide.reset();
                rightSide.addPoint(x + (WIDTH/2), y + (HEIGHT/2));
                rightSide.addPoint(x + WIDTH, y);
                rightSide.addPoint(x + WIDTH, y - (HEIGHT / 2));
                rightSide.addPoint(x + (WIDTH/2), y);

                topSide.reset();
                topSide.addPoint(x, y - (HEIGHT/2));
                topSide.addPoint(x + (WIDTH/2), y);
                topSide.addPoint(x + WIDTH, y - (HEIGHT/2));
                topSide.addPoint(x + (WIDTH/2), y - HEIGHT);
                break;
                
            case CustomMenu.RENDER_ISOMETRIC_2:
                frontSide.reset();
                frontSide.addPoint(x, y);
                frontSide.addPoint(x + (ISOMETRIC_WIDTH/2), y + (ISOMETRIC_HEIGHT/2));
                frontSide.addPoint(x + (ISOMETRIC_WIDTH/2), y);
                frontSide.addPoint(x, y - (ISOMETRIC_HEIGHT/2));

                rightSide.reset();
                rightSide.addPoint(x + (ISOMETRIC_WIDTH/2), y + (ISOMETRIC_HEIGHT/2));
                rightSide.addPoint(x + ISOMETRIC_WIDTH, y);
                rightSide.addPoint(x + ISOMETRIC_WIDTH, y - (ISOMETRIC_HEIGHT / 2));
                rightSide.addPoint(x + (ISOMETRIC_WIDTH/2), y);

                topSide.reset();
                topSide.addPoint(x, y - (ISOMETRIC_HEIGHT/2));
                topSide.addPoint(x + (ISOMETRIC_WIDTH/2), y);
                topSide.addPoint(x + ISOMETRIC_WIDTH, y - (ISOMETRIC_HEIGHT/2));
                topSide.addPoint(x + (ISOMETRIC_WIDTH/2), y - ISOMETRIC_HEIGHT);
                break;
                
            case CustomMenu.RENDER_ISOMETRIC_3:
                frontSide.reset();
                frontSide.addPoint(x, y);
                frontSide.addPoint(x + WIDTH, y + (int)(HEIGHT * .25));
                frontSide.addPoint(x + WIDTH, y - (int)(HEIGHT * .75));
                frontSide.addPoint(x, y - HEIGHT);
                
                rightSide.reset();
                rightSide.addPoint(x + WIDTH, y + (int)(HEIGHT * .25));
                rightSide.addPoint(x + WIDTH + (int)(WIDTH * .75), y);
                rightSide.addPoint(x + WIDTH + (int)(WIDTH * .75), y - HEIGHT);
                rightSide.addPoint(x + WIDTH, y - (int)(HEIGHT * .75));
                
                topSide.reset();
                topSide.addPoint(x + WIDTH, y - (int)(HEIGHT * .75));
                topSide.addPoint(x + WIDTH + (int)(WIDTH * .75), y - HEIGHT);
                topSide.addPoint(x + WIDTH - (int)(WIDTH * .25), y - HEIGHT - (int)(HEIGHT * .25));
                topSide.addPoint(x, y - HEIGHT);
                break;
                
            case CustomMenu.RENDER_2D:
                frontSide.reset();
                frontSide.addPoint(x, y);
                frontSide.addPoint(x + WIDTH, y);
                frontSide.addPoint(x + WIDTH, y + HEIGHT);
                frontSide.addPoint(x, y + HEIGHT);
                break;
        }
    }
    
    @Override
    public void dispose()
    {
        this.color = null;
    }
    
    /**
     * Calculate the x-coordinate for rendering
     * @param block The block containing the location
     * @return x-coordinate
     */
    public static int get2dX(final Block block)
    {
        return get2dX((int)block.getCol());
    }
    
    /**
     * Calculate the x-coordinate for rendering
     * @param col The location of the column
     * @return x-coordinate
     */
    public static int get2dX(final int col)
    {
        return (col * WIDTH);
    }
    
    /**
     * Calculate the y-coordinate for rendering
     * @param block The block containing the location
     * @return y-coordinate
     */
    public static int get2dY(final Block block)
    {
        return get2dY((int)block.getRow());
    }
    
    /**
     * Calculate the y-coordinate for rendering
     * @param row The location of the row
     * @return y-coordinate
     */
    public static int get2dY(final int row)
    {
        return (row * HEIGHT);
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param piece The piece containing the column, row location
     * @return y-coordinate
     */
    public static int getIsometric1Y(final Piece piece)
    {
        return getIsometric1Y(piece.getCol(), piece.getRow());
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param block The block containing the column, row location
     * @return y-coordinate
     */
    public static int getIsometric1Y(final Block block)
    {
        return getIsometric1Y(block.getCol(), block.getRow());
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param col The column location
     * @param row The row location
     * @return y-coordinate
     */
    public static int getIsometric1Y(final double col, final double row)
    {
        return (int)((col + row) * (HEIGHT / 2));
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param piece The piece containing the column, row location
     * @return x-coordinate
     */
    public static int getIsometric1X(final Piece piece)
    {
        return getIsometric1X(piece.getCol(), piece.getRow());
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param block The block containing the column, row location
     * @return x-coordinate
     */
    public static int getIsometric1X(final Block block)
    {
        return getIsometric1X(block.getCol(), block.getRow());
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param col The column location
     * @param row The row location
     * @return x-coordinate
     */
    public static int getIsometric1X(final double col, final double row)
    {
        return (int)((col - row) * (WIDTH / 2));
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param piece The piece containing the column, row location
     * @return y-coordinate
     */
    public static int getIsometric2Y(final Piece piece)
    {
        return getIsometric2Y(piece.getCol(), piece.getRow());
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param block The block containing the column, row location
     * @return y-coordinate
     */
    public static int getIsometric2Y(final Block block)
    {
        return getIsometric2Y(block.getCol(), block.getRow());
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param piece The piece containing the column location
     * @return x-coordinate
     */
    public static int getIsometric2X(final Piece piece)
    {
        return getIsometric2X(piece.getCol());
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param block The block containing the column location
     * @return x-coordinate
     */
    public static int getIsometric2X(final Block block)
    {
        return getIsometric2X(block.getCol());
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param col The column location
     * @param row The row location
     * @return y-coordinate
     */
    public static int getIsometric2Y(final double col, final double row)
    {
        return (int)((col + row) * (ISOMETRIC_HEIGHT/2));
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param col The column location
     * @return x-coordinate
     */
    public static int getIsometric2X(final double col)
    {
        return (int)(col * (ISOMETRIC_WIDTH / 2));
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param piece The piece containing the column, row location
     * @return y-coordinate
     */
    public static int getIsometric3Y(final Piece piece)
    {
        return getIsometric3Y(piece.getCol(), piece.getRow());
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param block The block containing the column, row location
     * @return y-coordinate
     */
    public static int getIsometric3Y(final Block block)
    {
        return getIsometric3Y(block.getCol(), block.getRow());
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param piece The piece containing the column location
     * @return x-coordinate
     */
    public static int getIsometric3X(final Piece piece)
    {
        return getIsometric3X(piece.getCol());
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param block The block containing the column location
     * @return x-coordinate
     */
    public static int getIsometric3X(final Block block)
    {
        return getIsometric3X(block.getCol());
    }
    
    /**
     * Calculate the y-coordinate for isometric rendering
     * @param col The column location
     * @param row The row location
     * @return y-coordinate
     */
    public static int getIsometric3Y(final double col, final double row)
    {
        return (int)((row * HEIGHT) + (col * (HEIGHT * .25)));
    }
    
    /**
     * Calculate the x-coordinate for isometric rendering
     * @param col The column location
     * @return x-coordinate
     */
    public static int getIsometric3X(final double col)
    {
        return (int)(col * HEIGHT);
    }
    
    /**
     * Render block
     * @param graphics Object used to draw block
     * @param x starting x-coordinate
     * @param y starting y-coordinate
     * @param renderIndex The way we want to render our block
     */
    public void render(final Graphics graphics, final double x, final double y, final int renderIndex)
    {
        //assign polygon coordinates
        assignCoordinates((int)x, (int)y, renderIndex);
        
        switch (renderIndex)
        {
            case CustomMenu.RENDER_ISOMETRIC_1:
            case CustomMenu.RENDER_ISOMETRIC_2:
            case CustomMenu.RENDER_ISOMETRIC_3:
                //set block color and fill shape
                graphics.setColor(getColor());

                if (topSide != null)
                    graphics.fillPolygon(topSide);

                if (rightSide != null)
                    graphics.fillPolygon(rightSide);

                if (frontSide != null)
                    graphics.fillPolygon(frontSide);

                //set outline color and draw outline
                graphics.setColor(Color.WHITE);

                if (frontSide != null)
                    graphics.drawPolygon(frontSide);

                if (rightSide != null)
                    graphics.drawPolygon(rightSide);

                if (topSide != null)
                    graphics.drawPolygon(topSide);
                break;
                
            case CustomMenu.RENDER_2D:
                
                //set block color and fill shape
                graphics.setColor(getColor());
                
                if (frontSide != null)
                    graphics.fillPolygon(frontSide);
                
                //set outline color and draw outline
                graphics.setColor(Color.WHITE);
                
                if (frontSide != null)
                    graphics.drawPolygon(frontSide);
                break;
        }
    }
}