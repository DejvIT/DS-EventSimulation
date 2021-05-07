/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.events;

import cores.ISimDelegate;
import vaccinationcenter.app.VaccinationCenter;

/**
 *
 * @author davidpavlicko
 */
public class Refresher extends MyEvent {

    public Refresher(VaccinationCenter simulation, double time) {
        super.time = time;
        super.simulation = simulation;
    }

    @Override
    public void execute() {
        
        super.simulation.setCurrentTime(super.time);
        
        if (!this.getSimulation().isSpeedRun()) {
            for (ISimDelegate delegate : this.getSimulation().getDelegates()) {
                delegate.afterEvent();
                try {
                    Thread.sleep((long) (1000 / Math.pow(this.getSimulation().getSpeed(), 2)));
                } catch (InterruptedException e) {
                    System.out.println("Exception: " + e);
                }
            }
        }
        
        super.getSimulation().planRefresher();
    }
    
}
