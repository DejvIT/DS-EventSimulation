/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cores;

/**
 *
 * @author davidpavlicko
 */
public abstract class Event implements Comparable<Event> {
    
    protected EventSimulation simulation;
    protected double time;
    
    public abstract void execute();
    public abstract EventSimulation getSimulation();
    
    @Override
    public int compareTo(Event event) {
        if (this.time < event.getTime()) {
            return -1;
        } else if (this.time > event.getTime()) {
            return 1;
        } else {
            return 0;
        }
    }
    
    public double getTime() {
        return this.time;
    }
}
