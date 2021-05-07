/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.app;

import java.util.ArrayList;

/**
 *
 * @author davidpavlicko
 */
public class Statistics {
    
    private double registrationWaitings;
    private int enteredRegistration;
    private double registrationLengths;
    private double registrationQueueChangeTime;
    
    private double examinationWaitings;
    private int enteredExamination;
    private double examinationLengths;
    private double examinationQueueChangeTime;
    
    private double vaccinationWaitings;
    private int enteredVaccination;
    private double vaccinationLengths;
    private double vaccinationQueueChangeTime;
    
    private double waitingRoomFillings;
    private int enteredRoom;
    private double waitingRoomChangeTime;
    
    Statistics() {}
    
    public void registrationWaitingsAdd(double time) {
        this.registrationWaitings += time;
        this.enteredRegistration++;
    }
    
    public double getAverageRegistrationWaitingTime() {
        return this.registrationWaitings / this.enteredRegistration;
    }
    
    public void registrationLengthsAdd(double length) {
        this.registrationLengths += length;
    }
    
    public double getAverageRegistrationQueueLength(double currentTime) {
        return this.registrationLengths / currentTime;
    }
    
    public double getRegistrationQueueChangeTime(double currentTime) {
        return currentTime - this.registrationQueueChangeTime;
    }
    
    public void setRegistrationQueueChangeTime(double time) {
        this.registrationQueueChangeTime = time;
    }
    
    public void examinationWaitingsAdd(double time) {
        this.examinationWaitings += time;
        this.enteredExamination++;
    }
    
    public double getAverageExaminationWaitingTime() {
        return this.examinationWaitings / this.enteredExamination;
    }
    
    public void examinationLengthsAdd(double length) {
        this.examinationLengths += length;
    }
    
    public double getAverageExaminationQueueLength(double currentTime) {
        return this.examinationLengths / currentTime;
    }
    
    public double getExaminationQueueChangeTime(double currentTime) {
        return currentTime - this.examinationQueueChangeTime;
    }
    
    public void setExaminationQueueChangeTime(double time) {
        this.examinationQueueChangeTime = time;
    }
    
    public void vaccinationWaitingsAdd(double time) {
        this.vaccinationWaitings += time;
        this.enteredVaccination++;
    }
    
    public double getAverageVaccinationWaitingTime() {
        return this.vaccinationWaitings / this.enteredVaccination;
    }
    
    public void vaccinationLengthsAdd(double length) {
        this.vaccinationLengths += length;
    }
    
    public double getAverageVaccinationQueueLength(double currentTime) {
        return this.vaccinationLengths / currentTime;
    }
    
    public double getVaccinationQueueChangeTime(double currentTime) {
        return currentTime - this.vaccinationQueueChangeTime;
    }
    
    public void setVaccinationQueueChangeTime(double time) {
        this.vaccinationQueueChangeTime = time;
    }
    
    public void waitingRoomFillingsAdd(double length) {
        this.waitingRoomFillings += length;
    }
    
    public double getAverageWaitingRoomFilling(double currentTime) {
        return this.waitingRoomFillings / currentTime;
    }
    
    public double getWaitingRoomChangeTime(double currentTime) {
        return currentTime - this.waitingRoomChangeTime;
    }
    
    public void setWaitingRoomChangeTime(double time) {
        this.waitingRoomChangeTime = time;
    }
    
    public void reset() {
        this.registrationWaitings = 0.0;
        this.enteredRegistration = 0;
        this.registrationLengths = 0.0;
        this.registrationQueueChangeTime = 0.0;
        
        this.examinationWaitings = 0.0;
        this.enteredExamination = 0;
        this.examinationLengths = 0.0;
        this.examinationQueueChangeTime = 0.0;
        
        this.vaccinationWaitings = 0.0;
        this.enteredVaccination = 0;
        this.vaccinationLengths = 0.0;
        this.vaccinationQueueChangeTime = 0.0;
        
        this.waitingRoomFillings = 0.0;
        this.enteredRoom = 0;
        this.waitingRoomChangeTime = 0.0;
    }
}
