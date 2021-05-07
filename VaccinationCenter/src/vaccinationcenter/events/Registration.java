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
public class Registration extends MyEvent {
    
    private final Staff worker;
    private final double startTime;
    
    public Registration(VaccinationCenter simulation, double start, double time, Person person, Staff worker) {
        super.simulation = simulation;
        this.startTime = start;
        super.time = time;
        super.person = person;
        this.worker = worker;
    }
    
    @Override
    public void execute() {
        
        super.simulation.setCurrentTime(super.time);
        this.worker.setIsBusy(false);
        this.worker.addWorkingTime(super.time - this.startTime);
        
        ArrayList<Staff> available = super.getSimulation().getAvailableWorkers();
        if (super.getSimulation().getRegistrationQueueSize() > 0 && available.size() > 0) {
            Staff staff = available.get(super.getSimulation().getGenerators().getRandomStaff(available.size()));
            super.getSimulation().getStatistics().registrationLengthsAdd(super.getSimulation().getRegistrationQueueSize() * (super.getSimulation().getStatistics().getRegistrationQueueChangeTime(super.time)));
            super.getSimulation().getStatistics().setRegistrationQueueChangeTime(super.time);
            Person queuePerson = super.getSimulation().registrationQueuePoll();
            super.getSimulation().getStatistics().registrationWaitingsAdd(super.time - queuePerson.getStartWaitingTimeRegistration());
            staff.setIsBusy(true);
            super.getSimulation().planRegistration(queuePerson, staff);
        }
        
        available = super.getSimulation().getAvailableDoctors();
        if (super.getSimulation().getExaminationQueueSize() == 0 && available.size() > 0) {
            super.getSimulation().getStatistics().examinationWaitingsAdd(0);
            Staff staff = available.get(super.getSimulation().getGenerators().getRandomStaff(available.size()));
            staff.setIsBusy(true);
            super.getSimulation().planExamination(super.person, staff);
        } else {
            super.person.setStartWaitingTimeExamination(super.time);
            super.getSimulation().examinationQueueAdd(super.person);
        }
    }
    
}
