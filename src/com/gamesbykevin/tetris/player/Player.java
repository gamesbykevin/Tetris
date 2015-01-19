package com.gamesbykevin.tetris.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.tetris.board.Board;
import com.gamesbykevin.tetris.board.piece.Block;
import com.gamesbykevin.tetris.board.piece.Piece;
import com.gamesbykevin.tetris.engine.Engine;
import com.gamesbykevin.tetris.resources.GameAudio;
import com.gamesbykevin.tetris.shared.IElement;

import java.awt.Graphics;

/**
 * This class will represent the player
 * @author GOD
 */
public abstract class Player extends Sprite implements Disposable, IElement
{
    //the players board
    private Board board;
    
    //the current piece in play for the player
    private Piece piece;
    
    //the next piece to be in play
    private Piece next;
    
    //timer that determines when a piece will drop
    private Timer timer;
    
    //timer that determines how long to display a completed line(s)
    private Timer complete;
    
    //default time between piece drops
    protected static final long DEFAULT_PIECE_DROP_DELAY = Timers.toNanoSeconds(1000L);
    
    //default time to show a completed line(s)
    private static final long COMPLETED_LINE_DELAY = Timers.toNanoSeconds(1000L);
    
    //is the game finished for the player (meaning the board crashed)
    private boolean gameover = false;
    
    //do we render isometric
    private boolean isometric = false;
    
    //the game game we are playing
    private final int modeIndex;
    
    //the text description of player
    private final String name;
    
    //store game stats here
    private Stats stats;
    
    //is this player human
    private final boolean human;
    
    protected Player(final int modeIndex, final String name, final boolean human)
    {
        //store the game mode
        this.modeIndex = modeIndex;
        
        //store the player name description
        this.name = name;
        
        //is this player human
        this.human = human;
        
        //create a new board
        this.board = new Board();
        
        //create the timer that controls when the piece falls
        this.timer = new Timer(DEFAULT_PIECE_DROP_DELAY);
        
        //create timer to track completed line
        this.complete = new Timer(COMPLETED_LINE_DELAY);
        
        //create object for game stats
        this.stats = new Stats(getModeIndex());
    }
    
    public void reset() throws Exception
    {
        //reset board
        getBoard().reset();
        
        //reset timers
        getTimer().reset();
        getCompletedTimer().reset();
        
        piece = null;
        next = null;
        
        //game is not over
        setGameover(false);
    }
    
    /**
     * Is this player human
     * @return true - yes, false otherwise
     */
    public boolean isHuman()
    {
        return this.human;
    }
    
    /**
     * Get game stats
     * @return Object representing game stats for this player
     */
    public Stats getStats()
    {
        return this.stats;
    }
    
    /**
     * Get the description of the player
     * @return The text description of this player
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Get the mode of play
     * @return Index of the mode playing
     */
    public final int getModeIndex()
    {
        return this.modeIndex;
    }
    
    /**
     * Set the render to isometric
     * @param isometric true - isometric, false - 2d
     */
    protected void setIsometric(final boolean isometric)
    {
        this.isometric = isometric;
    }
    
    /**
     * Do we render the board isometric
     * @return true - isometric, false - 2d
     */
    protected boolean hasIsometric()
    {
        return this.isometric;
    }
    
    /**
     * Get the board
     * @return The board where the player places tetris blocks
     */
    public Board getBoard()
    {
        return this.board;
    }
    
    protected Piece getNextPiece()
    {
        return this.next;
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
        this.next = new Piece(Board.COLS + 2, Board.START_ROW + Board.ROWS - 3, type);
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
        //update stats
        getStats().update(this, engine.getMain().getTime());
        
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

                //make sure the piece is not at the very top of the board
                if (!getPiece().isAboveCeiling())
                {
                    //if we are out of bounds or intersecting another block on the board
                    if (!getBoard().hasBounds(getPiece()) || getBoard().hasBlock(getPiece()))
                    {
                        //move piece back to previous
                        getPiece().decreaseRow();

                        //if the piece is still not within bounds the player lost
                        if (!getBoard().hasBounds(getPiece()))
                            setGameover(true);
                        
                        //make sure we aren't placing piece over another
                        if (!getBoard().hasBlock(getPiece()))
                        {
                            //add piece to board
                            getBoard().addPiece(getPiece());
                        }
                        else
                        {
                            //fill piece where possible
                            getBoard().fillPiece(getPiece());
                            
                            //we are placing a piece over another, it is gameover
                            setGameover(true);
                        }
                        
                        //check around the start area at top to see if there is gameover
                        for (int col = 0; col <= Board.START_RANGE; col++)
                        {
                            //if there is a block here, it is gameover
                            if (getBoard().hasBlock(Board.START_COL - col, Board.START_ROW))
                                setGameover(true);
                            if (getBoard().hasBlock(Board.START_COL + col, Board.START_ROW))
                                setGameover(true);
                        }
                        
                        //check and mark for a complete line
                        if (getBoard().markCompletedRow())
                        {
                            //play sound effect
                            engine.getResources().playGameAudio(GameAudio.Keys.Clear);
                        }
                        else
                        {
                            //play sound effect
                            engine.getResources().playGameAudio(GameAudio.Keys.Place);
                        }

                        //now remove the piece
                        removePiece();
                    }
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
                        //add the completed rows towards the total lines completed count
                        getBoard().setLines(getBoard().getLines() + getBoard().getCompletedRowCount());
                        
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
     * Is the game over for this player?
     * @return true if the board is filled with blocks, false otherwise
     */
    protected boolean hasGameover()
    {
        return this.gameover;
    }
    
    /**
     * Flag the game finished (or not)
     * @param gameover Has the board has been filled with blocks?
     */
    protected void setGameover(final boolean gameover)
    {
        this.gameover = gameover;
    }
    
    /**
     * Rotate piece
     */
    protected void rotate()
    {
        //rotate piece
        getPiece().rotateClockwise();
                
        if (!getPiece().isAboveCeiling())
        {
            //if we are out of bounds or intersecting another block on the board
            if (!getBoard().hasBounds(getPiece()) || getBoard().hasBlock(getPiece()))
            {
                //rotate piece backwards
                getPiece().rotateCounterClockwise();
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
        super.dispose();
        
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
        
        if (next != null)
        {
            next.dispose();
            next = null;
        }
        
        if (stats != null)
        {
            stats.dispose();
            stats = null;
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
            //only draw the current piece if no lines have been completed
            if (!getBoard().hasComplete())
            {
                if (getPiece() != null)
                {
                    try
                    {
                        if (!getBoard().hasBlock(getPiece()))
                        {
                            //add piece to board
                            getBoard().addPiece(getPiece());

                            //draw board
                            getBoard().render(graphics, hasIsometric());

                            //now remove the piece
                            getBoard().removePiece(getPiece());
                        }
                        else
                        {
                            //draw board
                            getBoard().render(graphics, hasIsometric());
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    //draw board
                    getBoard().render(graphics, hasIsometric());
                }
            }
            else
            {
                //draw board
                getBoard().render(graphics, hasIsometric());
            }
        }
        
        if (getNextPiece() != null)
        {
            double x;
            double y;
            
            if (hasIsometric())
            {
                //calculate the isometric coordinates where render should start
                x = getBoard().getX() + Block.getIsometricX(getNextPiece());
                y = getBoard().getY() + Block.getIsometricY(getNextPiece());
            }
            else
            {
                //calculate the 2d coordinates where the render should start
                x = (int)(getBoard().getX() + (getNextPiece().getCol() * Block.WIDTH));
                y = (int)(getBoard().getY() + (getNextPiece().getRow() * Block.HEIGHT));
            }
            
            //render the piece
            getNextPiece().render(graphics, x, y, hasIsometric());
        }
        
        if (getStats() != null)
        {
            getStats().render(graphics);
        }
    }
}