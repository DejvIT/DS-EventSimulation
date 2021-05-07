/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cores;

import java.util.PriorityQueue;

/**
 *
 * @author davidpavlicko
 */
public abstract class EventSimulation extends MonteCarlo {
    
    protected double currentTime;
    protected PriorityQueue<Event> eventQueue;
    protected double finishTime;
    protected boolean cooling;
    protected boolean speedRun;
    protected double speed = 1.0;
    
    public abstract void initiate();
    public abstract void setCurrentTime(double time);
   
    @Override
    public void replication() {
        
        initiate();
        while ((this.eventQueue.size() > 0 && this.currentTime < this.finishTime) || (this.cooling && this.eventQueue.size() > 0)) {
            
            while (this.pause) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    System.out.println("Exception: " + e);
                }
            }
            
            if (!this.stop) {
                Event event = this.eventQueue.poll();
                event.execute();
            } else {
                return;
            }
            
        }
        
    }
    
    public void setSpeedRun(boolean speedRun) {
        this.speedRun = speedRun;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    public boolean isSpeedRun() {
        return this.speedRun;
    }
    
    public double getSpeed() {
        return this.speed;
    }
    
    public double getTime() {
        return this.currentTime;
    }
    
    public double getFinishTime() {
        return this.finishTime;
    }
    
}
