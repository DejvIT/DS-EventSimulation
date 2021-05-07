/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.app;

import cores.EventSimulation;
import cores.ISimDelegate;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import vaccinationcenter.agents.Person;
import vaccinationcenter.agents.Staff;
import vaccinationcenter.events.Arrival;
import vaccinationcenter.events.Examination;
import vaccinationcenter.events.Refresher;
import vaccinationcenter.events.Registration;
import vaccinationcenter.events.Vaccination;
import vaccinationcenter.events.WaitingRoom;

/**
 *
 * @author davidpavlicko
 */
public class VaccinationCenter extends EventSimulation {
    
    private final Generators generators;
    
    private int arrivals;
    private int departures;
    private final Queue<Person> registrationQueue;
    private final Queue<Person> examinationQueue;
    private final Queue<Person> vaccinationQueue;
    private final ArrayList<Staff> workers;
    private final ArrayList<Staff> doctors;
    private final ArrayList<Staff> nurses;
    private double skipped;
    private final int orderedPeople;
    
    private final Statistics statistics;
    private int waitingRoomFilling;
    
    private double averageRegistrationWaitings;
    private double averageRegistrationLengths;
    private double averageRegistrationEfficiency;
    private double averageExaminationWaitings;
    private double averageExaminationLengths;
    private double averageExaminationEfficiency;
    private double averageVaccinationWaitings;
    private double averageVaccinationLengths;
    private double averageVaccinationEfficiency;
    private double averageWaitingRoomFilling;
    private double averageWaitingRoomFillingPowered;
    
    private final boolean experiment;
    private int minDoctors;
    private int maxDoctors;
    private int refreshPerNth;
    
    public VaccinationCenter(int replications, int finish, Generators generators, int numWorkers, int numDoctors, int numNurses, int orders, boolean experiment, int minDoctors, int maxDoctors) {
        this.generators = generators;
        
        super.replications = replications;
        super.currentTime = 0;
        super.finishTime = finish;
        super.cooling = true;
        
        this.arrivals = 0;
        this.departures = 0;
        this.registrationQueue = new LinkedList<>();
        this.examinationQueue = new LinkedList<>();
        this.vaccinationQueue = new LinkedList<>();
        
        this.workers = new ArrayList<>();
        for (int i = 0; i < numWorkers; i++) {
            this.workers.add(new Staff());
        }
        this.doctors = new ArrayList<>();
        this.experiment = experiment;
        if (!this.experiment) {
            for (int i = 0; i < numDoctors; i++) {
                this.doctors.add(new Staff());
            }
        } else {
            this.minDoctors = minDoctors;
            this.maxDoctors = maxDoctors;
            for (int i = 0; i < this.minDoctors; i++) {
                this.doctors.add(new Staff());
            }
        }
        this.nurses = new ArrayList<>();
        for (int i = 0; i < numNurses; i++) {
            this.nurses.add(new Staff());
        }
        
        this.orderedPeople = orders;
        
        this.statistics = new Statistics();
    }

    @Override
    public void initiate() {
        
        this.skipped = this.generators.getDailySkipped();
        this.arrivals = 0;
        this.departures = 0;
        
        super.currentTime = 0;
        super.eventQueue = new PriorityQueue();
        
        for (int i = 0; i < this.workers.size(); i++) {
            this.workers.get(i).reset();
        }
        
        for (int i = 0; i < this.doctors.size(); i++) {
            this.doctors.get(i).reset();
        }
        if (getPassedReplications() > 0 && this.experiment && this.refreshPerNth > 0 && getPassedReplications() % this.refreshPerNth == 0 && this.doctors.size() < this.maxDoctors) {
            this.doctors.add(new Staff());
            this.averageExaminationWaitings = 0.0;
            this.averageExaminationLengths = 0.0;
            this.averageExaminationEfficiency = 0.0;
        }
        
        for (int i = 0; i < this.nurses.size(); i++) {
            this.nurses.get(i).reset();
        }
        
        this.statistics.reset();
        this.waitingRoomFilling = 0;
        
        planArrival();
        planRefresher();
    }
    
    @Override
    public void setCurrentTime(double time) {
        if (time > super.currentTime) {
            super.currentTime = time;
        }
    }
    
    public void planArrival() {
        double planAtTime = super.currentTime;
        boolean planArrival = true;
        
        while (planArrival) {
            double hasArrived = this.generators.getArrived();
            if (hasArrived < this.skipped) {
                planAtTime += ((double) super.finishTime / this.orderedPeople);
            } else {
                if (super.eventQueue.isEmpty()) {
                    super.eventQueue.add(new Arrival(this, planAtTime));
                } else if (super.currentTime < (super.finishTime - ((double) super.finishTime / this.orderedPeople))) {
                    super.eventQueue.add(new Arrival(this, planAtTime + ((double) super.finishTime / this.orderedPeople)));
                }
                planArrival = false;
            }
        }
    }
    
    public void planRegistration(Person person, Staff worker) {
        super.eventQueue.add(new Registration(this, super.currentTime, this.generators.getRegistration() + super.currentTime, person, worker));
    }
    
    public void planExamination(Person person, Staff doctor) {
        super.eventQueue.add(new Examination(this, super.currentTime, this.generators.getExamination() + super.currentTime, person, doctor));
    }
    
    public void planVaccination(Person person, Staff nurse) {
        super.eventQueue.add(new Vaccination(this, super.currentTime, this.generators.getVaccination() + super.currentTime, person, nurse));
    }
    
    public void planWaitingRoom(Person person) {
        this.statistics.waitingRoomFillingsAdd(this.waitingRoomFilling * (this.statistics.getWaitingRoomChangeTime(super.currentTime)));
        super.eventQueue.add(new WaitingRoom(this, this.generators.getWaiting() + super.currentTime, person));
        this.waitingRoomFilling++;
        this.statistics.setWaitingRoomChangeTime(super.currentTime);
    }
    
    public void planRefresher() {
        if (!super.eventQueue.isEmpty() && !super.speedRun) {
            super.eventQueue.add(new Refresher(this, super.currentTime + this.speed));
        }
    }
    
    public int getRegistrationQueueSize() {
        return this.registrationQueue.size();
    }
    
    public void registrationQueueAdd(Person person) {
        this.statistics.registrationLengthsAdd(this.registrationQueue.size() * (this.statistics.getRegistrationQueueChangeTime(super.currentTime)));
        this.registrationQueue.add(person);
        this.statistics.setRegistrationQueueChangeTime(super.currentTime);
    }
    
    public Person registrationQueuePoll() {
        return this.registrationQueue.poll();
    }
    
    public int getExaminationQueueSize() {
        return this.examinationQueue.size();
    }
    
    public void examinationQueueAdd(Person person) {
        this.statistics.examinationLengthsAdd(this.examinationQueue.size() * (this.statistics.getExaminationQueueChangeTime(super.currentTime)));
        this.examinationQueue.add(person);
        this.statistics.setExaminationQueueChangeTime(super.currentTime);
    }
    
    public Person examinationQueuePoll() {
        return this.examinationQueue.poll();
    }
    
    public int getVaccinationQueueSize() {
        return this.vaccinationQueue.size();
    }
    
    public void vaccinationQueueAdd(Person person) {
        this.statistics.vaccinationLengthsAdd(this.vaccinationQueue.size() * (this.statistics.getVaccinationQueueChangeTime(super.currentTime)));
        this.vaccinationQueue.add(person);
        this.statistics.setVaccinationQueueChangeTime(super.currentTime);
    }
    
    public Person vaccinationQueuePoll() {
        return this.vaccinationQueue.poll();
    }
    
    public void addArrival() {
        this.arrivals++;
    }
    
    public void addDeparture() {
        this.statistics.waitingRoomFillingsAdd(this.waitingRoomFilling * (this.statistics.getWaitingRoomChangeTime(super.currentTime)));
        this.waitingRoomFilling--;
        this.statistics.setWaitingRoomChangeTime(super.currentTime);
        this.departures++;
    }
    
    public ArrayList<Staff> getAvailableWorkers() {
        ArrayList<Staff> available = new ArrayList<>();
        for (Staff worker : this.workers) {
            if (!worker.getIsBusy()) {
                available.add(worker);
            }
        }
        return available;
    }
    
    public ArrayList<Staff> getAvailableDoctors() {
        ArrayList<Staff> available = new ArrayList<>();
        for (Staff worker : this.doctors) {
            if (!worker.getIsBusy()) {
                available.add(worker);
            }
        }
        return available;
    }
    
    public ArrayList<Staff> getAvailableNurses() {
        ArrayList<Staff> available = new ArrayList<>();
        for (Staff worker : this.nurses) {
            if (!worker.getIsBusy()) {
                available.add(worker);
            }
        }
        return available;
    }
    
    public Statistics getStatistics() {
        return this.statistics;
    }
    
    public Generators getGenerators() {
        return this.generators;
    }

    public void updateStatistics() {
        
        this.averageRegistrationWaitings += this.statistics.getAverageRegistrationWaitingTime();
        this.averageRegistrationLengths += this.statistics.getAverageRegistrationQueueLength(super.currentTime);
        this.averageExaminationWaitings += this.statistics.getAverageExaminationWaitingTime();
        this.averageExaminationLengths += this.statistics.getAverageExaminationQueueLength(super.currentTime);
        this.averageVaccinationWaitings += this.statistics.getAverageVaccinationWaitingTime();
        this.averageVaccinationLengths += this.statistics.getAverageVaccinationQueueLength(super.currentTime);
        
        double efficiency = 0.0;
        for (int i = 0; i < this.workers.size(); i++) {
            efficiency += ((this.workers.get(i).getWorkingTime() / super.currentTime) * 100);
        }
        this.averageRegistrationEfficiency += (efficiency / this.workers.size());
        
        efficiency = 0.0;
        for (int i = 0; i < this.doctors.size(); i++) {
            efficiency += ((this.doctors.get(i).getWorkingTime() / super.currentTime) * 100);
        }
        this.averageExaminationEfficiency += (efficiency / this.doctors.size());
        
        efficiency = 0.0;
        for (int i = 0; i < this.nurses.size(); i++) {
            efficiency += ((this.nurses.get(i).getWorkingTime() / super.currentTime) * 100);
        }
        this.averageVaccinationEfficiency += (efficiency / this.nurses.size());
        
        double replicationWaitingRoomFilling = this.statistics.getAverageWaitingRoomFilling(super.currentTime);
        this.averageWaitingRoomFilling += replicationWaitingRoomFilling;
        this.averageWaitingRoomFillingPowered += Math.pow(replicationWaitingRoomFilling, 2);
    }
    
    public double getAverageRegistrationWaitingTime() {
        return this.averageRegistrationWaitings / getPassedReplications();
    }
    
    public double getAverageRegistrationQueueLength() {
        return this.averageRegistrationLengths / getPassedReplications();
    }
    
    public double getAverageRegistrationEfficiency() {
        return this.averageRegistrationEfficiency / getPassedReplications();
    }
    
    public double getAverageExaminationWaitingTime() {
        return this.averageExaminationWaitings / getPassedReplications();
    }
    
    public double getAverageExaminationQueueLength(int showNth) {
        if (this.experiment) {
            return this.averageExaminationLengths / showNth;
        } else {
            return this.averageExaminationLengths / getPassedReplications();
        }
    }
    
    public double getAverageExaminationEfficiency() {
        return this.averageExaminationEfficiency / getPassedReplications();
    }
    
    public double getAverageVaccinationWaitingTime() {
        return this.averageVaccinationWaitings / getPassedReplications();
    }
    
    public double getAverageVaccinationQueueLength() {
        return this.averageVaccinationLengths / getPassedReplications();
    }
    
    public double getAverageVaccinationEfficiency() {
        return this.averageVaccinationEfficiency / getPassedReplications();
    }
    
    public double getAverageWaitingRoomFilling() {
        return this.averageWaitingRoomFilling / getPassedReplications();
    }
    
    public double getWaitingRoomDeviation() {
        double s = Math.sqrt((1 / this.averageWaitingRoomFillingPowered) - Math.pow(1 / this.averageWaitingRoomFilling, 2));
        return s;
    }
    
    public String getConfidenceInterval() {
        int size = getPassedReplications();
        if (size > 30) {
            double _x = getAverageWaitingRoomFilling();
            double s = getWaitingRoomDeviation();
            return "<" + String.format("%.6f", (_x - ((s * 1.96) / Math.sqrt(size)))) + " ; " + String.format("%.6f", (_x + ((s * 1.96) / Math.sqrt(size)))) + ">";
        } else {
            return "< Nan ; Nan >";
        }
    }
    
    public double getWorkersEfficiencyInTime(double time) {
        double efficiency = 0.0;
        for (int i = 0; i < this.workers.size(); i++) {
            efficiency += ((this.workers.get(i).getWorkingTime() / time) * 100);
        }
        return efficiency / this.workers.size();
    }
    
    public double getDoctorsEfficiencyInTime(double time) {
        double efficiency = 0.0;
        for (int i = 0; i < this.doctors.size(); i++) {
            efficiency += ((this.doctors.get(i).getWorkingTime() / time) * 100);
        }
        return efficiency / this.doctors.size();
    }
    
    public double getNursesEfficiencyInTime(double time) {
        double efficiency = 0.0;
        for (int i = 0; i < this.nurses.size(); i++) {
            efficiency += ((this.nurses.get(i).getWorkingTime() / time) * 100);
        }
        return efficiency / this.nurses.size();
    }
    
    public String getWorkersStatus() {
        String status = "";
        for (int i = 0; i < this.workers.size(); i++) {
            status += (i+1) + ". Worker: " + (this.workers.get(i).getIsBusy() ? "X" : "O") + "\n";
        }
        return status;
    }
    
    public String getDoctorsStatus() {
        String status = "";
        for (int i = 0; i < this.doctors.size(); i++) {
            status += (i+1) + ". Doctor: " + (this.doctors.get(i).getIsBusy() ? "X" : "O") + "\n";
        }
        return status;
    }
    
    public String getNursesStatus() {
        String status = "";
        for (int i = 0; i < this.nurses.size(); i++) {
            status += (i+1) + ". Nurse: " + (this.nurses.get(i).getIsBusy() ? "X" : "O") + "\n";
        }
        return status;
    }

    public int getWaitingRoomFilling() {
        return this.waitingRoomFilling;
    }

    public int getArrivals() {
        return this.arrivals;
    }

    public int getDepartures() {
        return this.departures;
    }
    
    public String getWorkersEfficiency() {
        String efficiency = "";
        for (int i = 0; i < this.workers.size(); i++) {
            efficiency += (i+1) + ". Worker: " + String.format("%.4f", ((this.workers.get(i).getWorkingTime() / super.currentTime) * 100)) + " %\n";
        }
        return efficiency;
    }
    
    public String getDoctorsEfficiency() {
        String efficiency = "";
        for (int i = 0; i < this.doctors.size(); i++) {
            efficiency += (i+1) + ". Doctor: " + String.format("%.4f", ((this.doctors.get(i).getWorkingTime() / super.currentTime) * 100)) + " %\n";
        }
        return efficiency;
    }
    
    public String getNursesEfficiency() {
        String efficiency = "";
        for (int i = 0; i < this.nurses.size(); i++) {
            efficiency += (i+1) + ". Nurse: " + String.format("%.4f", ((this.nurses.get(i).getWorkingTime() / super.currentTime) * 100)) + " %\n";
        }
        return efficiency;
    }
    
    public void setRefreshPerNth(int value) {
        this.refreshPerNth = value;
    }
    
    public int getDoctorsSize() {
        return this.doctors.size();
    }
    
    public boolean isExperiment() {
        return this.experiment;
    }
    
}
