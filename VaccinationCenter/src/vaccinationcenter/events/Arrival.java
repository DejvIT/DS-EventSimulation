/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.events;

import java.util.ArrayList;
import vaccinationcenter.agents.Person;
import vaccinationcenter.agents.Staff;
import vaccinationcenter.app.VaccinationCenter;

/**
 *
 * @author davidpavlicko
 */
public class Arrival extends MyEvent {
    
    public Arrival(VaccinationCenter simulation, double time) {
        super.time = time;
        super.simulation = simulation;
        super.person = new Person();
    }

    @Override
    public void execute() {
        
        super.simulation.setCurrentTime(super.time);
        super.getSimulation().addArrival();
        
        ArrayList<Staff> available = super.getSimulation().getAvailableWorkers();
        if (super.getSimulation().getRegistrationQueueSize() == 0 && available.size() > 0) {
            super.getSimulation().getStatistics().registrationWaitingsAdd(0);
            Staff staff = available.get(super.getSimulation().getGenerators().getRandomStaff(available.size()));
            staff.setIsBusy(true);
            super.getSimulation().planRegistration(super.person, staff);
        } else {
            super.person.setStartWaitingTimeRegistration(super.time);
            super.getSimulation().registrationQueueAdd(super.person);
        }
        
        super.getSimulation().planArrival();
    }
    
}
