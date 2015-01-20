package com.gamesbykevin.tetris.player;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.tetris.board.Board;
import com.gamesbykevin.tetris.board.piece.Block;
import com.gamesbykevin.tetris.engine.Engine;
import com.gamesbykevin.tetris.menu.CustomMenu;
import com.gamesbykevin.tetris.resources.GameAudio;
import com.gamesbykevin.tetris.shared.IElement;
import com.gamesbykevin.tetris.shared.Shared;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * This object will contain the players in the game
 * @author GOD
 */
public final class Players implements Disposable, IElement
{
    //the list of players in the game
    List<Player> players;
    
    //places to position the player
    private static final int PLAYER_START_X = 0;
    private static final int PLAYER_START_Y = 0;
    
    //offset coordinates for 2d player board
    private static final int PLAYER_START_X_2D = 25;
    private static final int PLAYER_START_Y_2D = 35;

    //offset coordinates for isometric board
    private static final int PLAYER_START_X_ISO = 200;
    private static final int PLAYER_START_Y_ISO = 25;
    
    //the reward/penalty (for tug of war mode)
    private static final int TUG_OF_WAR_REWARD = 1;
    private static final int TUG_OF_WAR_PENALTY = -3;
    
    //the maximum number of players allowed in the game
    private static final int PLAYER_LIMIT = 2;
    
    //the amount to increase the piece drop delay for every level increase
    private static final double SPEED_INCREASE_RATIO = .05;
    
    //how many lines do we have to complete before we progress to the next level
    private static final int LINES_PER_LEVEL = 10;
    
    //don't speed up the block drop delay after this level, so the game can still be playable
    private static final int LEVEL_SPEED_UP_LIMIT = 15;
    
    //different delays for each difficulty
    private static final long LINE_DROP_DELAY_VERY_EASY = Timers.toNanoSeconds(600L);
    private static final long LINE_DROP_DELAY_EASY      = Timers.toNanoSeconds(333L);
    private static final long LINE_DROP_DELAY_MEDIUM    = Timers.toNanoSeconds(100L);
    private static final long LINE_DROP_DELAY_HARD      = Timers.toNanoSeconds(75L);
    private static final long LINE_DROP_DELAY_VERY_HARD = Timers.toNanoSeconds(25L);
    
    //is the game complete
    private boolean complete = false;
    
    public Players(final boolean multiple, final int modeIndex, final int difficultyIndex, final Font font) throws Exception
    {
        //create empty list of players
        this.players = new ArrayList<>();
        
        //objecs representing human/cpu
        Player human, cpu;
        
        //if multiple players
        if (multiple)
        {
            //create human player
            human = new Human(modeIndex);
            human.setX(PLAYER_START_X);
            human.setY(PLAYER_START_Y);
            human.getStats().setFont(font);
            add(human);
            
            //create cpu opponent
            cpu = new Cpu(modeIndex);
            cpu.setX(Shared.ORIGINAL_WIDTH / 2);
            cpu.setY(PLAYER_START_Y);
            cpu.getStats().setFont(font);
            
            //set time delay for each difficulty
            switch (difficultyIndex)
            {
                case CustomMenu.GAME_DIFFICULTY_VERY_EASY:
                    cpu.getTimer().setReset(LINE_DROP_DELAY_VERY_EASY);
                    break;
                    
                case CustomMenu.GAME_DIFFICULTY_EASY:
                    cpu.getTimer().setReset(LINE_DROP_DELAY_EASY);
                    break;
                    
                case CustomMenu.GAME_DIFFICULTY_MEDIUM:
                    cpu.getTimer().setReset(LINE_DROP_DELAY_MEDIUM);
                    break;
                    
                case CustomMenu.GAME_DIFFICULTY_HARD:
                    cpu.getTimer().setReset(LINE_DROP_DELAY_HARD);
                    break;
                    
                case CustomMenu.GAME_DIFFICULTY_VERY_HARD:
                    cpu.getTimer().setReset(LINE_DROP_DELAY_VERY_HARD);
                    break;
                    
                default:
                    throw new Exception("Difficulty not found = " + difficultyIndex);
            }
            
            //reset
            cpu.getTimer().reset();
            add(cpu);
        }
        else
        {
            //create human player
            human = new Human(modeIndex);
            human.setX((Shared.ORIGINAL_WIDTH / 2) - (((Board.COLS + 1) * Block.WIDTH) / 2) - PLAYER_START_X_2D);
            human.setY(PLAYER_START_Y);
            human.getStats().setFont(font);
            add(human);
        }
    }
    
    /**
     * Is the game complete
     * @param complete true = yes, false = no
     */
    public void setComplete(final boolean complete)
    {
        this.complete = complete;
    }
    
    /**
     * Is the game finished?
     * @return true = yes, false = no
     */
    public boolean isComplete()
    {
        return this.complete;
    }
    
    /**
     * Set the render selection for our players, all will be same
     * @param renderIndex The desired render
     * @throws Exception if the render type is not setup here
     */
    public void setRenderIndex(final int renderIndex) throws Exception
    {
        for (int i = 0; i < players.size(); i++)
        {
            Player player = players.get(i);
            
            player.setRenderIndex(renderIndex);
            
            //now position board according to render setting
            switch (renderIndex)
            {
                case CustomMenu.RENDER_2D:
                case CustomMenu.RENDER_ISOMETRIC_2:
                case CustomMenu.RENDER_ISOMETRIC_3:
                    player.getBoard().setLocation(player.getX() + PLAYER_START_X_2D, player.getY() + PLAYER_START_Y_2D);
                    break;
                    
                case CustomMenu.RENDER_ISOMETRIC_1:
                    player.getBoard().setLocation(player.getX() + PLAYER_START_X_ISO, player.getY() + PLAYER_START_Y_ISO);
                    break;
                    
                default:
                    throw new Exception("Render index is not setup = " + renderIndex);
            }
            
            //update polygon coordinates
            player.getBoard().assignBackground();
        }
    }
    
    /**
     * Reset all players, board(s), etc....
     */
    public void reset() throws Exception
    {
        for (int i = 0; i < players.size(); i++)
        {
            players.get(i).reset();
        }
    }
    
    private Player getCpu() throws Exception
    {
        for (int i = 0; i < players.size(); i++)
        {
            Player player = players.get(i);
            
            if (!player.isHuman())
                return player;
        }
        
        //not found so throw exception
        throw new Exception("Cpu player is not found");
    }
    
    private Player getHuman() throws Exception
    {
        for (int i = 0; i < players.size(); i++)
        {
            Player player = players.get(i);
            
            if (player.isHuman())
                return player;
        }
        
        //not found so throw exception
        throw new Exception("Human player is not found");
    }
    
    private void add(final Player player) throws Exception
    {
        //if we already have the limit, throw exception
        if (players.size() >= PLAYER_LIMIT)
            throw new Exception("Can't have more than 2 players");
        
        this.players.add(player);
    }
    
    /**
     * Check if the game has ended and determine the winner
     */
    private void checkGameover() throws Exception
    {
        for (int i = 0; i < players.size(); i++)
        {
            //get the current player
            Player player = players.get(i);
            
            switch (player.getModeIndex())
            {
                case CustomMenu.GAME_MODE_INFINITE:
                    
                    //if the board is filled
                    if (player.hasGameover())
                    {
                        //if infinite mode, we just reset
                        player.reset();
                    }
                    break;
                    
                case CustomMenu.GAME_MODE_NORMAL:
                    
                    //if board has crashed
                    if (player.hasGameover())
                    {
                        //create win image
                        player.getStats().renderResultImage(false);
                        
                        //mark game as finished
                        setComplete(true);
                    }
                    break;
                    
                case CustomMenu.GAME_MODE_TIMED:
                    
                    //check if the player crashed the board
                    if (player.hasGameover())
                    {
                        //get the opponent, 
                        Player opponent = (player.isHuman()) ? getCpu() : getHuman();
                        
                        //we lose because we crashed the board
                        player.getStats().renderResultImage(false);

                        //they win
                        opponent.getStats().renderResultImage(true);
                        
                        //mark game completed
                        setComplete(true);
                        
                        //exit method
                        return;
                    }
                    else if (player.getStats().getGameTimer().hasTimePassed())
                    {
                        //get the opponent
                        Player opponent = (player.isHuman()) ? getCpu() : getHuman();
                        
                        //if the player has more lines than opponent
                        if (player.getBoard().getLines() > opponent.getBoard().getLines())
                        {
                            //we win
                            player.getStats().renderResultImage(true);
                            
                            //opponent loses
                            opponent.getStats().renderResultImage(false);
                            
                            //flag as lose
                            opponent.setGameover(true);
                        }
                        else
                        {
                            //flag player as game over
                            player.setGameover(true);
                            
                            //we lose
                            player.getStats().renderResultImage(false);
                            
                            //opponent wins
                            opponent.getStats().renderResultImage(true);
                        }
                        
                        //mark game as finished
                        setComplete(true);
                        
                        //exit method
                        return;
                    }
                    break;
                    
                case CustomMenu.GAME_MODE_TUG_OF_WAR:
                    
                    //check if the player crashed the board, or no longer has health
                    if (player.hasGameover() || player.getStats().getHealth() <= 0)
                    {
                        //flag player as game over
                        player.setGameover(true);
                        
                        //get the opponent, 
                        Player opponent = (player.isHuman()) ? getCpu() : getHuman();
                        
                        //we lose because we crashed the board
                        player.getStats().renderResultImage(false);

                        //they win
                        opponent.getStats().renderResultImage(true);
                        
                        //mark game completed
                        setComplete(true);
                        
                        //exit method
                        return;
                    }
                    break;
            }
        }
    }
    
    @Override
    public void dispose()
    {
        for (int i = 0; i < players.size(); i++)
        {
            players.get(i).dispose();
            players.set(i, null);
        }
        
        players.clear();
        players = null;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //if the game has completed, no need to continue
        if (isComplete())
            return;

        //check if the game is over depending on game mode
        checkGameover();
        
        //if the game is over play sound effect
        if (isComplete())
        {
            //stop all sound
            engine.getResources().stopAllSound();
            
            if (getHuman().hasGameover())
            {
                engine.getResources().playGameAudio(GameAudio.Keys.Lose);
            }
            else
            {
                engine.getResources().playGameAudio(GameAudio.Keys.Win);
            }
            
            //game is over, don't continue
            return;
        }        
        
        for (int i = 0; i < players.size(); i++)
        {
            //get the current player
            Player player = players.get(i);
            
            //skip if this player has a crashed board
            if (player.hasGameover())
                continue;
            
            //if  there is a completed line, only update basic
            if (player.getBoard().hasComplete())
            {
                //get the number of completed lines
                final int lines = player.getBoard().getCompletedRowCount();

                //update the basic elements
                player.updateBasic(engine);

                //if the finished line is complete
                if (!player.getBoard().hasComplete())
                {
                    //only speed up the piece drop rate for the human
                    if (player.isHuman())
                    {
                        //calculate the current level
                        int level = (int)(player.getBoard().getLines() / LINES_PER_LEVEL);

                        //cap the level so the pieces can only drop so fast
                        if (level > LEVEL_SPEED_UP_LIMIT)
                            level = LEVEL_SPEED_UP_LIMIT;

                        //update drop piece timer according to level
                        final long reset = (long)(Player.DEFAULT_PIECE_DROP_DELAY - (Player.DEFAULT_PIECE_DROP_DELAY * (SPEED_INCREASE_RATIO * level)));

                        //set new reset time and reset timer
                        player.getTimer().setReset(reset);
                        player.getTimer().reset();
                    }

                    switch (player.getModeIndex())
                    {
                        //if infinite mode, reward/penalize
                        case CustomMenu.GAME_MODE_TUG_OF_WAR:

                            //update the players health
                            player.getStats().updateHealth(lines * TUG_OF_WAR_REWARD);
                            player.getStats().renderHealthImage();

                            if (player.isHuman())
                            {
                                //penalize the other player
                                getCpu().getStats().updateHealth(lines * TUG_OF_WAR_PENALTY);
                                getCpu().getStats().renderHealthImage();
                            }
                            else
                            {
                                //penalize the other player
                                getHuman().getStats().updateHealth(lines * TUG_OF_WAR_PENALTY);
                                getHuman().getStats().renderHealthImage();
                            }
                            break;
                    }
                }
            }
            else
            {
                player.update(engine);
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        for (int i = 0; i < players.size(); i++)
        {
            players.get(i).render(graphics);
        }
    }
}