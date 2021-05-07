/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.agents;


/**
 *
 * @author davidpavlicko
 */
public class Staff {
    
    protected boolean isBusy;
    private double workingTime;
    
    public Staff() {}

    public boolean getIsBusy() {
        return this.isBusy;
    }

    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }
    
    public void addWorkingTime(double time) {
        this.workingTime += time;
    }
    
    public double getWorkingTime() {
        return this.workingTime;
    }
    
    public void reset() {
        this.isBusy = false;
        this.workingTime = 0.0;
    }
    
}
