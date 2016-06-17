package model;

/**
 * Model to manage game level
 * @author LAFAYE DE MICHEAUX Yoan - LHOPITAL Sacha
 */
public class Level {
    
    public static Level EASY = new Level("EASY",0,5,5,5);
    public static Level MEDIUM = new Level("MEDIUM",1,15,15,40);
    public static Level HARD = new Level("HARD",2,25,25,100);
    
    public String name;
    public int id;
    public int height;
    public int width;
    public int nbMines;
    public int time;
    
    /**
     * Constuctor
     * @param name Name of the level
     * @param id Id of the level (to retreive score)
     * @param width Width of the level
     * @param height Height of the level
     * @param nbMines Number of mines of the level
     */
    public Level(String name,int id, int width, int height, int nbMines){
        this.id = id;
        this.name = name;
        this.height = height;
        this.width = width;
        this.nbMines = nbMines;
    }
}
