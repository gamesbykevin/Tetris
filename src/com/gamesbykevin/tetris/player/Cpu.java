package com.gamesbykevin.tetris.player;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.tetris.board.Board;
import com.gamesbykevin.tetris.board.piece.Piece;

import com.gamesbykevin.tetris.engine.Engine;

import java.awt.Graphics;

/**
 * CPU opponent containing AI logic<br>
 * The AI logic was inspired by this article
 * https://codemyroad.wordpress.com/2013/04/14/tetris-ai-the-near-perfect-player/
 * @author GOD
 */
public final class Cpu extends Player implements Disposable
{
    //do he have the destination
    private boolean destination = false;
    
    //the target column we want the ai to move to
    private int column;
    
    //the target number of rotations
    private int roationCount;
    
    //aggregate height score weight
    private static final double WEIGHT_AGGREGATE_HEIGHT = -0.66569;
    
    //completed lines score weight
    private static final double WEIGHT_COMPLETED_LINES = 0.99275;
    
    //holes score weight
    private static final double WEIGHT_HOLES = -0.46544;
    
    //bumpiness score weight
    private static final double WEIGHT_BUMPINESS = -0.24077;
    
    public Cpu()
    {
        super();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    /**
     * Set the number of rotations for placing our piece
     * @param rotations The total number of rotations needed to get to our goal
     */
    private void setTargetRotation(final int roationCount)
    {
        this.roationCount = roationCount;
    }
    
    /**
     * Get the number of rotations for placing our piece
     * @return The total number of rotations needed to get to our goal
     */
    private int getTargetRotation()
    {
        return this.roationCount;
    }
    
    /**
     * Set the target column for placing our piece
     * @param column The column we want to put our piece
     */
    private void setTargetColumn(final int column)
    {
        this.column = column;
    }
    
    /**
     * Get the target column for placing our piece
     * @return The column we want to put our piece
     */
    private int getTargetColumn()
    {
        return this.column;
    }
            
    /**
     * Do we have a destination?
     * @return true if we have the location of the piece we want to place, false otherwise
     */
    private boolean hasDestination()
    {
        return this.destination;
    }
    
    /**
     * Set flag if we have a destination
     * @param destination true if we have found a destination, false otherwise
     */
    private void setDestination(final boolean destination)
    {
        this.destination = destination;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update common elements
        super.updateBasic(engine);

        //make sure there is a current piece in play
        if (getPiece() != null)
        {
            //do we have the destination for the current piece
            if (!hasDestination())
            {
                //locate the best position
                calculateDestination();
            }
            else
            {
                //if we are not at our target rotation yet
                if (getPiece().getRotation() != getTargetRotation())
                {
                    //rotate piece
                    rotate();
                }
                else if (getPiece().getCol() < getTargetColumn())
                {
                    //if we are short of our destination move east
                    getPiece().increaseCol();
                }
                else if (getPiece().getCol() > getTargetColumn())
                {
                    //if we are past our destination move west
                    getPiece().decreaseCol();
                }
                else
                {
                    //automatically expire timer so piece drops, since we are now in place
                    getTimer().setRemaining(0);
                }
            }
        }
        else
        {
            //if there is no current piece, we will need to calculate a new destination when we get one
            setDestination(false);
        }
    }
    
    /**
     * Here we determine the best place to put the tetris piece.<br>
     * We will implement a scoring algorithm.<br>
     * This will locate the position with the highest score as our destination
     */
    private void calculateDestination() throws Exception
    {
        //get current location
        double originalCol = getPiece().getCol();
        double originalRow = getPiece().getRow();
                
        //store the score to beat
        double score = 0;
        
        //in cases the score may be below 0 make sure we at least assign for the first score
        boolean initialScore = true;
        
        //check each rotation
        for (int count = 0; count < Piece.TOTAL_ROTATIONS; count++)
        {
            //rotate piece clockwise each time
            getPiece().rotateClockwise();
            
            //check each location
            for (int col = 0; col < Board.COLS; col++)
            {
                for (int row = 0; row < Board.ROWS; row++)
                {
                    //set the starting point
                    getPiece().setCol(col);
                    getPiece().setRow(row);
                    
                    //if the piece is not in bounds here, continue
                    if (!getBoard().hasBounds(getPiece()))
                        continue;
                    
                    //check until we hit floor or another block
                    if (getPiece().hasFloor() || getBoard().hasBlock(getPiece()))
                    {
                        //if we hit a block move up 1 row
                        if (getBoard().hasBlock(getPiece()))
                            getPiece().decreaseRow();
                        
                        //if we still don't have bounds, continue
                        if (!getBoard().hasBounds(getPiece()))
                            continue;
                        
                        //if a block already exists at the location of the place
                        if (getBoard().hasBlock(getPiece()))
                            continue;
                        
                        //add piece to board
                        getBoard().addPiece(getPiece());
                        
                        //what is the score for placing the piece here
                        double tmpScore = 0;
                        
                        //calculate/add aggregate height score
                        tmpScore += (WEIGHT_AGGREGATE_HEIGHT * getBoard().getAggregateHeight());
                        
                        //calculate/add completed row(s) score
                        tmpScore += (WEIGHT_COMPLETED_LINES * getBoard().getCompletedRowCount());
                        
                        //calculate/add hole count score
                        tmpScore += (WEIGHT_HOLES * getBoard().getHoleCount());
                        
                        //calculate/add bumpiness score
                        tmpScore += (WEIGHT_BUMPINESS * getBoard().getBumpiness());

                        //if this score is better than our high score, or we need to set the score to beat
                        if (tmpScore > score || initialScore)
                        {
                            //we now have the initial score set
                            initialScore = false;
                            
                            //set the new high score
                            score = tmpScore;
                            
                            //set our target rotation
                            setTargetRotation(getPiece().getRotation());
                            
                            //set the column we want to place the piece
                            setTargetColumn(col);
                        }
                        
                        //now that we are done scoring we can remove the piece from the board
                        getBoard().removePiece(getPiece());
                        
                        //no need to check any further rows, exit loop to check the next column
                        break;
                    }
                }
            }
        }
        
        //now restore location back
        getPiece().setCol(originalCol);
        getPiece().setRow(originalRow);
        
        //flag that we have a destination
        setDestination(true);
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        super.render(graphics);
    }
}