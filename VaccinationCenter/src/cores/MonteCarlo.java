/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cores;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author davidpavlicko
 */
public abstract class MonteCarlo {
    
    protected int replications;
    private int passedReplications;
    protected boolean pause;
    protected boolean stop;
    protected final List<ISimDelegate> delegates = new ArrayList();
    
    public abstract void replication();
    
    public void simulate() {
        this.pause = false;
        this.stop = false;
        
        for (int i = 0; i < this.replications; i++) {
            
            if (!this.stop) {
                replication();
                this.passedReplications++;
                for (ISimDelegate delegate : this.delegates) {
                    delegate.afterReplication();
                }
            } else {
                return;
            }
        }
        
        for (ISimDelegate delegate : this.delegates) {
            delegate.afterSimulation();
        }
        
    }

    public int getReplications() {
        return this.replications;
    }

    public int getPassedReplications() {
        return this.passedReplications;
    }
    
    public void registerDelegate(ISimDelegate delegate) {
        this.delegates.add(delegate);
    }

    public Iterable<ISimDelegate> getDelegates() {
        return this.delegates;
    }
    
    public void setPaused(boolean pause) {
        this.pause = pause;
    }
    
    public void setStopped(boolean stop) {
        this.stop = stop;
    }
    
    public boolean getPaused() {
        return this.pause;
    }
    
    public boolean getStopped() {
        return this.stop;
    }
    
}
