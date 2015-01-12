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
    
    //the next piece to be in play
    private Piece nextPiece;
    
    //timer that determines when a piece will drop
    private Timer timer;
    
    //timer that determines how long to display a completed line(s)
    private Timer complete;
    
    //default time between piece drops
    private static final long DEFAULT_PIECE_DROP_DELAY = Timers.toNanoSeconds(1000L);

    //default time to show a completed line(s)
    private static final long COMPLETED_LINE_DELAY = Timers.toNanoSeconds(1000L);
    
    
    protected Player()
    {
        //create a new board
        this.board = new Board();
        
        //create the timer that controls when the piece falls
        this.timer = new Timer(DEFAULT_PIECE_DROP_DELAY);
        
        //create timer to track completed line
        this.complete = new Timer(COMPLETED_LINE_DELAY);
    }
    
    public Board getBoard()
    {
        return this.board;
    }
    
    protected Piece getNextPiece()
    {
        return this.nextPiece;
    }
    
    protected Piece getPiece()
    {
        return this.piece;
    }
    
    protected void assignNextPiece()
    {
        //assign the next piece as current
        this.piece = getNextPiece();
        
        //place in starting point
        this.piece.setCol(Board.START_COL);
        this.piece.setRow(Board.START_ROW);
    }
    
    /**
     * Remove the current piece
     */
    protected void removePiece()
    {
        this.piece = null;
    }
    
    /**
     * Create a tetris piece of the specified type and at the default starting location.<BR>
     * @param type The type of piece we want to add
     * @throws Exception Exception will be thrown if type does not exist
     */
    protected void createNextPiece(final int type) throws Exception
    {
        this.nextPiece = new Piece(Board.START_COL + Board.COLS, Board.START_ROW, type);
    }
    
    /**
     * Get the timer that represents the delay between each piece drop.
     * @return The timer that represents the delay between each piece drop.
     */
    protected Timer getTimer()
    {
        return this.timer;
    }
    
    /**
     * Get the timer that represents the delay to player activity when at least 1 line has been completed.
     * @return The timer that represents the delay to player activity when at least 1 line has been completed.
     */
    protected Timer getCompletedTimer()
    {
        return this.complete;
    }
    
    /**
     * Update common elements for the player
     * @param engine 
     */
    protected void updateBasic(final Engine engine) throws Exception
    {
        //if the human does not have a tetris piece, create one
        if (getPiece() == null && !getBoard().hasComplete())
        {
            //if next piece has been created assign as current
            if (getNextPiece() != null)
                assignNextPiece();

            //create the next piece
            createNextPiece(Piece.PIECES[engine.getRandom().nextInt(Piece.PIECES.length)]);
        }
        else
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
                    getBoard().addPiece(getPiece());

                    //check for a complete line
                    getBoard().markCompletedRow();

                    //now remove the piece
                    removePiece();
                }
            }
            else
            {
                //at least 1 row has been completed
                if (getBoard().hasComplete())
                {
                    if (!getCompletedTimer().hasTimePassed())
                    {
                        //update timer until complete
                        getCompletedTimer().update(engine.getMain().getTime());
                    }
                    else
                    {
                        //remove completed lines
                        getBoard().clearCompletedRows();

                        //drop above blocks
                        getBoard().dropBlocks();

                        //the board no longer has a completed line(s)
                        getBoard().setComplete(false);

                        //reset timer
                        getCompletedTimer().reset();
                    }
                }
                else
                {
                    //update timer
                    getTimer().update(engine.getMain().getTime());
                }
            }
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
        
        if (nextPiece != null)
        {
            nextPiece.dispose();
            nextPiece = null;
        }
        
        if (timer != null)
            timer = null;
        
        if (complete != null)
            complete = null;
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        if (getBoard() != null)
        {
            //draw board
            getBoard().render(graphics);
        }
        
        //only draw the current piece if no lines have been completed
        if (!getBoard().hasComplete())
        {
            if (getPiece() != null)
            {
                //calculate the coordinates where the render should start
                int x = (int)(getBoard().getX() + (getPiece().getCol() * Block.WIDTH));
                int y = (int)(getBoard().getY() + (getPiece().getRow() * Block.HEIGHT));

                //render the piece
                getPiece().render(x, y, graphics);
            }
        }
        
        if (getNextPiece() != null)
        {
            //calculate the coordinates where the render should start
            int x = (int)(getBoard().getX() + (getNextPiece().getCol() * Block.WIDTH));
            int y = (int)(getBoard().getY() + (getNextPiece().getRow() * Block.HEIGHT));
            
            //render the piece
            getNextPiece().render(x, y, graphics);
        }
        
        //we may draw some common components of the player here??
    }
}