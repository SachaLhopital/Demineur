package model;

import java.util.LinkedList;

/**
 * Model pour manage the tiles
 * @author LAFAYE DE MICHEAUX Yoan - LHOPITAL Sacha
 */
public class Tile {
    
    //Values of the tiles
    public final static int MINE = -1;
    public final static int EMPTY = 0;
    //State of the tiles
    public final static int STATE_HIDDEN = 0;
    public final static int STATE_SHOWN = 1;
    public final static int STATE_FLAG = 2;
    
    private final int value;
    
    private int state;
    
    private final LinkedList<Tile> voisins;
     
    /**
     * Constructor
     * @param value Value of the tile
     */
    public Tile(int value){
        this.value = value;
        this.state = STATE_HIDDEN;
        voisins = new LinkedList<>();
    }
    
    /***
     * Return the value of the tile
     * @return value
     */
    public int getValue(){
        return this.value;
    }
    
    /***
     * Return the state of the tile
     * @return state
     */
    public int getState(){
        return this.state;
    }
    
    /***
     * Update the State of the tile
     * @param state 
     */
    public void setState(int state){
        this.state = state;
    }
    
    /***
     * Add a neighbour
     * @param neighbour 
     */
    public void addNeighbour(Tile neighbour){
        voisins.add(neighbour);
    }
    
    /***
     * Reveal the Tile and his neighbour
     * @return number of revealed tiles
     */
    public int reveal(){
        
        int nbReveledTile = 0;
        
        if(this.state == Tile.STATE_HIDDEN){
        
            this.state = Tile.STATE_SHOWN;
            nbReveledTile++;

            if(this.value == EMPTY){
                for(Tile voisin:voisins){
                    nbReveledTile += voisin.reveal();
                }
            } 
            if(this.value == MINE) {
                nbReveledTile = -1;
            }
        }        
        return nbReveledTile;
    }
}
