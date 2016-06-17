package model;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Model to manage the Time
 * @author LAFAYE DE MICHEAUX Yoan - LHOPITAL Sacha
 */
public class GameTime extends Observable{
    
    public Timer timer;
    
    private int timePast;
    private boolean isStarted;
    
    /**
     * Constructor
     */
    public GameTime(){
        this.timePast = 0;
        this.isStarted = false;
    }
    
    /**
     * Restart the game timer
     */
    public void restart(){
        isStarted = false;
        this.start();
    }
    
    /**
     * Start the timer
     */
    public void start(){
        if(isStarted == true){
            return;
        }
        isStarted = true;
        this.timePast = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                timePast ++;
                setChanged();
                notifyObservers();
            }
        }, 0, 1000);
    }

    /**
     * Stop the timer
     */
    public void stop(){
        if(isStarted){
            timer.cancel();
            timer.purge();
        }
        isStarted = false;
    }
    
    /**
     * Return the time past since it started
     * @return int Time past since it was started
     */
    public int getTime(){
        return timePast;
    }
}
