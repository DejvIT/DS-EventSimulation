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
public class Examination extends MyEvent {
    
    private final Staff doctor;
    private final double startTime;
    
    public Examination(VaccinationCenter simulation, double start, double time, Person person, Staff doctor) {
        super.simulation = simulation;
        this.startTime = start;
        super.time = time;
        super.person = person;
        this.doctor = doctor;
    }
    
    @Override
    public void execute() {
        
        super.simulation.setCurrentTime(super.time);
        this.doctor.setIsBusy(false);
        this.doctor.addWorkingTime(super.time - this.startTime);
        
        ArrayList<Staff> available = super.getSimulation().getAvailableDoctors();
        if (super.getSimulation().getExaminationQueueSize() > 0 && available.size() > 0) {
            Staff staff = available.get(super.getSimulation().getGenerators().getRandomStaff(available.size()));
            super.getSimulation().getStatistics().examinationLengthsAdd(super.getSimulation().getExaminationQueueSize() * (super.getSimulation().getStatistics().getExaminationQueueChangeTime(super.time)));
            super.getSimulation().getStatistics().setExaminationQueueChangeTime(super.time);
            Person queuePerson = super.getSimulation().examinationQueuePoll();
            super.getSimulation().getStatistics().examinationWaitingsAdd(super.time - queuePerson.getStartWaitingTimeExamination());
            staff.setIsBusy(true);
            super.getSimulation().planExamination(queuePerson, staff);
        }
        
        available = super.getSimulation().getAvailableNurses();
        if (super.getSimulation().getVaccinationQueueSize() == 0 && available.size() > 0) {
            super.getSimulation().getStatistics().vaccinationWaitingsAdd(0);
            Staff staff = available.get(super.getSimulation().getGenerators().getRandomStaff(available.size()));
            staff.setIsBusy(true);
            super.getSimulation().planVaccination(super.person, staff);
        } else {
            super.person.setStartWaitingTimeVaccination(super.time);
            super.getSimulation().vaccinationQueueAdd(super.person);
        }
        
    }
    
}
