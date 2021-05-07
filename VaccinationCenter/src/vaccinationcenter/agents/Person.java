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
public class Person {
    
    private double startWaitingTimeRegistration;
    private double startWaitingTimeExamination;
    private double startWaitingTimeVaccination;
    
    public Person() {}

    public double getStartWaitingTimeRegistration() {
        return this.startWaitingTimeRegistration;
    }

    public double getStartWaitingTimeExamination() {
        return this.startWaitingTimeExamination;
    }

    public double getStartWaitingTimeVaccination() {
        return this.startWaitingTimeVaccination;
    }

    public void setStartWaitingTimeRegistration(double startWaitingTimeRegistration) {
        this.startWaitingTimeRegistration = startWaitingTimeRegistration;
    }

    public void setStartWaitingTimeExamination(double startWaitingTimeExamination) {
        this.startWaitingTimeExamination = startWaitingTimeExamination;
    }

    public void setStartWaitingTimeVaccination(double startWaitingTimeVaccination) {
        this.startWaitingTimeVaccination = startWaitingTimeVaccination;
    }
    
}
