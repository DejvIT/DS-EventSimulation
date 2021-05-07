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
public class Vaccination extends MyEvent {
    
    private final Staff nurse;
    private final double startTime;
    
    public Vaccination(VaccinationCenter simulation, double start, double time, Person person, Staff nurse) {
        super.simulation = simulation;
        this.startTime = start;
        super.time = time;
        super.person = person;
        this.nurse = nurse;
    }
    
    @Override
    public void execute() {
        
        super.getSimulation().setCurrentTime(super.time);
        this.nurse.setIsBusy(false);
        this.nurse.addWorkingTime(super.time - this.startTime);

        ArrayList<Staff> available = super.getSimulation().getAvailableNurses();
        if (super.getSimulation().getVaccinationQueueSize() > 0 && available.size() > 0) {
            Staff staff = available.get(super.getSimulation().getGenerators().getRandomStaff(available.size()));
            super.getSimulation().getStatistics().vaccinationLengthsAdd(super.getSimulation().getVaccinationQueueSize() * (super.getSimulation().getStatistics().getVaccinationQueueChangeTime(super.time)));
            super.getSimulation().getStatistics().setVaccinationQueueChangeTime(super.time);
            Person queuePerson = super.getSimulation().vaccinationQueuePoll();
            super.getSimulation().getStatistics().vaccinationWaitingsAdd(super.time - queuePerson.getStartWaitingTimeVaccination());
            staff.setIsBusy(true);
            super.getSimulation().planVaccination(queuePerson, staff);
        }
        
        super.getSimulation().planWaitingRoom(super.person);
        
    }
    
}
