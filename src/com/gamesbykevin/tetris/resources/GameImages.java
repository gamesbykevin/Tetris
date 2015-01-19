package com.gamesbykevin.tetris.resources;

import com.gamesbykevin.framework.resources.*;

/**
 * All game images
 * @author GOD
 */
public final class GameImages extends ImageManager
{
    //description for progress bar
    private static final String DESCRIPTION = "Loading Image Resources";
    
    /**
     * These are the keys used to access the resources and need to match the id in the xml file
     */
    public enum Keys
    {
        Background1,Background2, Background3,Background4,
        Background5,Background6, Background7,Background8,
    }
    
    public GameImages() throws Exception
    {
        super(Resources.XML_CONFIG_GAME_IMAGE);
        
        //the description that will be displayed for the progress bar
        super.setProgressDescription(DESCRIPTION);
        
        if (Keys.values().length < 1)
            super.increase();
    }
}