package model;

import java.util.Observable;

/**
 * Model to manage the game
 * @author LAFAYE DE MICHEAUX Yoan - LHOPITAL Sacha
 */
public class Game extends Observable {
    
    private Grid baseGrid;
    public Level level;
    public GameTime timer;
        
    private int minesLeft;
    private int hiddenTile;
    private boolean endOfGame;
    
    /**
     * Initial constructor
     * @param level 
     */
    public Game(Level level){
        this.level = level;
        this.timer = new GameTime();
        init(level);
    }
    
    /**
     * Restart the game
     * @param level 
     */
    public void restart(Level level){
        this.level = level;
        this.timer.stop();//the timer is automaticly restartted at the next right click
        init(level); 
    }
    
    /**
     * Init the game
     * @param level 
     */
    private void init(Level level){
        this.baseGrid = new Grid(level.width, level.height, level.nbMines);
        this.minesLeft = level.nbMines;
        this.hiddenTile = level.width * level.height;
        this.endOfGame = false;
        
        Score.load();
    }
    
    /***
     * Update the state of the game after a right click on a tile.
     * @param posWidth
     * @param posHeight
     */
    public void rigthClick(int posWidth, int posHeight){      

        if(endOfGame){
            return;
        }

        Tile clickedTile = baseGrid.getTile(posWidth, posHeight);
        if(clickedTile == null){
            //Tile not found do nothing
            return;
        }
        
        switch(clickedTile.getState()){
            case Tile.STATE_SHOWN:
                //do nothing tile is already shown
                break;
            case Tile.STATE_FLAG:
                clickedTile.setState(Tile.STATE_HIDDEN);
                minesLeft++;
                hiddenTile++;
                break;
            case Tile.STATE_HIDDEN:
                clickedTile.setState(Tile.STATE_FLAG);
                minesLeft--;
                hiddenTile--;
                break;
            default:
                //state unknown, do nothing
        }
        setChanged();
        notifyObservers();
    }
    
    /***
     * Update the state of the tile after a right click
     * It also start the timer on the first click
     * @param posWidth
     * @param posHeight
     */
    public void leftClick(int posWidth, int posHeight){

        if(endOfGame){
            return;
        }

        Tile clickedTile = baseGrid.getTile(posWidth, posHeight);
        if(clickedTile == null){
            //Tile not found do nothing
            return;
        }
        
        if(clickedTile.getState() != Tile.STATE_HIDDEN){
            //The tile can be shown only if it is hidden
            return;
        }
                
        int nbReveledTile = clickedTile.reveal();
        
        if(nbReveledTile == -1) {
            endOfGame = true;
        } else {
            hiddenTile -= nbReveledTile;
        }
        
        
        if(endOfGame){
            //reveal all tiles
            for(int i = 0 ; i < level.width; i++){
                for(int j = 0 ; j < level.height; j++){
                    Tile t = baseGrid.getTile(i, j);
                    if(t.getValue()!=Tile.MINE){
                        continue;
                    }
                    if(t!=null){
                        t.reveal();
                    }
                }
            }
        }
        
        //update endOfGame
        if(hiddenTile == minesLeft){
            endOfGame = true;
        }
        
        this.timer.start();
        setChanged();
        notifyObservers();
        
        
    }
    
    /***
     * Return the actual game grid.
     * @return baseGrid
     */
    public Grid getBaseGrid() {
        return baseGrid;
    }
    
    /***
     * Return how many mines left
     * @return minesLeft
     */
    public int getMinesLeft() {
        return minesLeft;
    }
    
    /***
     * Check the end of the game and stop the timer if it is true
     * @return true if game is over. False else.
     */
    public boolean isEndOfGame() {
        if(endOfGame){
            timer.stop();
        }
        return endOfGame;
    }
    
    /***
     * Check if the player has won or lose.
     * @return true if the player has won. False else.
     */
    public boolean playerWin() {
        return isEndOfGame() && hiddenTile == minesLeft;
    }
}
