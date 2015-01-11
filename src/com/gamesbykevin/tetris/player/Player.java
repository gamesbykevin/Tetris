package com.gamesbykevin.tetris.player;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.tetris.board.Board;
import com.gamesbykevin.tetris.board.piece.Block;
import com.gamesbykevin.tetris.board.piece.Piece;
import com.gamesbykevin.tetris.engine.Engine;
import com.gamesbykevin.tetris.shared.IElement;

import java.awt.Graphics;

/**
 * This class will represent the player
 * @author GOD
 */
public abstract class Player implements Disposable, IElement
{
    //the players board
    private Board board;
    
    //the current piece in play for the player
    private Piece piece;
    
    //timer that determines when a piece will drop
    private Timer timer;
    
    private static final long PIECE_DROP_DELAY = Timers.toNanoSeconds(1000L);
    
    protected Player()
    {
        //create a new board
        this.board = new Board();
        
        //create the timer that controls when the piece falls
        this.timer = new Timer(PIECE_DROP_DELAY);
    }
    
    public Board getBoard()
    {
        return this.board;
    }
    
    protected Piece getPiece()
    {
        return this.piece;
    }
    
    protected void removePiece()
    {
        this.piece = null;
    }
    
    /**
     * Create a tetris piece of the specified type and at the default starting location
     * @param type The type of piece we want to add
     * @throws Exception Exception will be thrown if type does not exist
     */
    protected void createPiece(final int type) throws Exception
    {
        this.piece = new Piece(Board.START_COL, Board.START_ROW, type);
    }
    
    protected Timer getTimer()
    {
        return this.timer;
    }
    
    /**
     * Update common elements for the player
     * @param engine 
     */
    protected void updateBasic(final Engine engine) throws Exception
    {
        //if time has passed move piece down
        if (getTimer().hasTimePassed())
        {
            //reset timer
            getTimer().reset();
            
            //move piece south
            getPiece().increaseRow();
            
            //if we are out of bounds or intersecting another block on the board
            if (!getBoard().hasBounds(getPiece()) || getBoard().hasBlock(getPiece()))
            {
                //move piece back to previous
                getPiece().decreaseRow();
                
                //add piece to board
                getBoard().add(getPiece());
                
                //now remove the piece
                removePiece();
            }
        }
        else
        {
            //update timer
            getTimer().update(engine.getMain().getTime());
        }
    }
    
    /**
     * Each player is required to have their own update method
     * @param engine Object containing all game elements
     */
    @Override
    public abstract void update(final Engine engine) throws Exception;
    
    @Override
    public void dispose()
    {
        if (board != null)
        {
            board.dispose();
            board = null;
        }
        
        if (piece != null)
        {
            piece.dispose();
            piece = null;
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        if (getBoard() != null)
        {
            //draw board
            getBoard().render(graphics);
        }
        
        if (getPiece() != null)
        {
            //calculate the coordinates where the render should start
            int x = (int)(getBoard().getX() + (getPiece().getCol() * Block.WIDTH));
            int y = (int)(getBoard().getY() + (getPiece().getRow() * Block.HEIGHT));
            
            //render the piece
            getPiece().render(x, y, graphics);
        }
        
        
        //we may draw some common components of the player here??
    }
}