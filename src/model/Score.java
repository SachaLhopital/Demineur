package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * Model to manage game Score
 * Calculate the gamer score at te end of a game
 * + 10 for each properly placed flag
 * + 1 for each revealed tile
 * - 5 for each flag at the wrong place
 * @author LAFAYE DE MICHEAUX Yoan - LHOPITAL Sacha
 */
public class Score implements Serializable {
    
    public static String scorePath = System.getProperty("user.dir")+"\\Save.sav";
    public static ScoreSave [] bestScore;

    static{
        bestScore = new ScoreSave[3];
        for(int i = 0 ; i < bestScore.length; i++){
            bestScore[i] = new ScoreSave();
        }
    }
    
    private static class ScoreSave implements Serializable{
        public int score;
        public int time;
    }
    
    /**
     * Return the score of the game
     * @param game
     * @return int Score value
     */
    public static int getScore(Game game) {
        int score = 0;
        
        for(int i = 0; i < game.level.width; i++){
            for(int j = 0; j < game.level.height; j++) {
                
                Tile t = game.getBaseGrid().getTile(i, j);
                
                switch (t.getState()) {
                    
                    case Tile.STATE_FLAG :
                        if(t.getValue() == Tile.MINE) {
                            score += 10;
                        } else {
                            score -= 5;
                        }
                        break;
                        
                    case Tile.STATE_HIDDEN :
                        //do nothing
                        break;
                        
                    case Tile.STATE_SHOWN :
                        if(t.getValue() == Tile.MINE) {
                            //do nothing
                            //the player loose
                        } else {
                            score++;
                        }
                        break;
                        
                    default :
                }
            }
        }
        
        if(score > bestScore[game.level.id].score ){
            bestScore[game.level.id].score = score;
            bestScore[game.level.id].time = game.timer.getTime();
        } else if( score == bestScore[game.level.id].score  && game.timer.getTime() < bestScore[game.level.id].time )  {
            bestScore[game.level.id].time = game.timer.getTime();
        }    
        return score;
    }
    
    /**
     * Save the best scores in a file
     */
    public static void save(){
        try {
            //clean file
            File f = new File(scorePath);
            if(f.exists()){
                f.delete();
            }
            FileOutputStream fout = new FileOutputStream(scorePath);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(bestScore);
            fout.close();
        } catch (IOException ex) {
            System.out.println("Erreur lors de l'enregistrement des scores.");
        }
    }
    
    /**
     * Load the best scores from the file
     */
    public static void load(){
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(scorePath);
            ObjectInputStream ois = new ObjectInputStream(fin);
            Object obj = ois.readObject();
            if(obj instanceof ScoreSave[]){
                bestScore = (ScoreSave [])obj;
            }else{
                save();
            }           
            fin.close();
        } catch (FileNotFoundException ex) {
            //System.out.println("File not found");
        } catch (IOException ex) {
            //System.out.println("IO error");
        } catch (ClassNotFoundException ex) {
            //System.out.println("Class not found");
        }
    }
    
    /**
     * Return the best score
     * @param level
     * @return int Best score linked to the level
     */
    public static int getBestScore(Level level){
        return bestScore[level.id].score;
    }
    
    /**
     * Return the best time for a score
     * @param level
     * @return int Best time in seconds
     */
    public static int getBestTime(Level level){
        return bestScore[level.id].time;
    }
    
}
