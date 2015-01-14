package com.gamesbykevin.tetris.player;

import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.tetris.board.Board;
import com.gamesbykevin.tetris.board.piece.Piece;

import com.gamesbykevin.tetris.engine.Engine;

import java.awt.Graphics;

/**
 * CPU opponent containing AI logic
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
    
    //the score for each line
    private static final double BONUS_LINE = 50;
    
    //the score for each block that is directly place above another
    private static final double BONUS_BLOCK_COVER = 10;
    
    //the penalty for creating a hole
    private static final double PENALTY_EMPTY_BLOCK = -100;
    
    //the penalty for the block height
    private static final double PENALTY_BLOCK_HEIGHT = - 5;
    
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
                if (getRotations() != getTargetRotation())
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
            //rotate piece
            getPiece().rotateClockwise();
            
            //check each column
            for (int col = 0; col < Board.COLS; col++)
            {
                //set the starting point
                getPiece().setCol(col);
                getPiece().setRow(0);
                
                //if we are out of bounds or a block already exists in this place check next location
                if (!getBoard().hasBounds(getPiece()) || getBoard().hasBlock(getPiece()))
                    continue;
                
                //now check until we find the floor
                for (int row = 0; row < Board.ROWS; row++)
                {
                    //set the new row to check
                    getPiece().setRow(row);
                    
                    //check until we hit floor or another block
                    if (!getBoard().hasBounds(getPiece()) || getBoard().hasBlock(getPiece()))
                    {
                        //now move the piece back up 1 row and lets score the position
                        getPiece().setRow(row - 1);
                        
                        //add piece to board
                        getBoard().addPiece(getPiece());
                        
                        //what is the score for placing the piece here
                        double tmpScore = 0;
                        
                        /**
                         * Scoring logic
                         * 1. Add a bonus depending on number of lines completed
                         * 2. Add a bonus for each block that has a block directly below it
                         * 3. Add a penalty for each block that does not have a block directly below it 
                         *    and multiply that penalty by the total number of empty blocks below
                         * 4. Add a penalty for the height of each block
                         * 5. ????????????????????
                         */
                        //get the number of completed lines here
                        final int completedLines = getBoard().getCompletedRowCount();
                        
                        //add score for completed lines
                        tmpScore += (completedLines * BONUS_LINE);
                        
                        //count number of blocks directly below the piece
                        final int coveredBlocks = getBoard().getBlockCount(true, getPiece());
                        
                        //add score for covered blocks
                        tmpScore += (coveredBlocks * BONUS_BLOCK_COVER);
                        
                        //count number of empty spaces directly below the piece
                        final int emptyBlocks = getBoard().getBlockCount(false, getPiece());
                        
                        //add penalty for holes created
                        tmpScore += (emptyBlocks * PENALTY_EMPTY_BLOCK);
                        
                        //get the total height of this piece
                        final int totalHeight = getPiece().getTotalHeight();
                        
                        //add penalty for block height
                        tmpScore += (totalHeight * PENALTY_BLOCK_HEIGHT);
                                
                        //now that we are done scoring we can remove the piece from the board
                        getBoard().removePiece(getPiece());
                        
                        //if this score is better than our high score, or we need to set the score to beat
                        if (tmpScore > score || initialScore)
                        {
                            //we now have the initial score set
                            initialScore = false;
                            
                            //set the new high score
                            score = tmpScore;
                            
                            //set our target rotation
                            setTargetRotation(count);
                            
                            //set the column we want to place the piece
                            setTargetColumn(col);
                        }
                        
                        //no need to check any further rows, exit for loop
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