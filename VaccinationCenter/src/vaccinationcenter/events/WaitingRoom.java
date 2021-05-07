/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.events;

import vaccinationcenter.agents.Person;
import vaccinationcenter.app.VaccinationCenter;

/**
 *
 * @author davidpavlicko
 */
public class WaitingRoom extends MyEvent {
    
    public WaitingRoom(VaccinationCenter simulation, double time, Person person) {
        super.time = time;
        super.simulation = simulation;
        super.person = person;
    }
    
    @Override
    public void execute() {
        
        super.simulation.setCurrentTime(super.time);
        super.getSimulation().addDeparture();
    }
    
}
