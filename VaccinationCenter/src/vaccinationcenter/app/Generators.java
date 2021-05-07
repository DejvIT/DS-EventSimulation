/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.app;

import generators.ExponentialDistribution;
import generators.Seeder;
import generators.TriangularDistribution;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author davidpavlicko
 */
public class Generators {
    
    private final Seeder seeder;
    private final Random registration;
    private final ExponentialDistribution medicalExamination;
    private final TriangularDistribution vaccination;
    private final Random waiting;
    private final Random dailySkipped;
    private final Random arrived;
    private double expectedArrivals;
    private final ArrayList<Random> randomPickers;
    
    public Generators(int seed, int amount) {
        this.seeder = new Seeder(seed);
        this.registration = new Random(this.seeder.sample());
        this.medicalExamination = new ExponentialDistribution((double)1/260, this.seeder.sample());
        this.vaccination = new TriangularDistribution(20, 100, 75, this.seeder.sample());
        this.waiting = new Random(this.seeder.sample());
        this.dailySkipped = new Random(this.seeder.sample());
        this.arrived = new Random(this.seeder.sample());
        
        this.randomPickers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            this.randomPickers.add(new Random(this.seeder.sample()));
        }
    }
    
    // Oprava 6.4.2021 po cviceni "Shame on me!"
    public double getRegistration() {
        return 140 + (220 - 140) * this.registration.nextDouble();
    }
    
    public double getExamination() {
        return this.medicalExamination.sample();
    }
    
    public double getVaccination() {
        return this.vaccination.sample();
    }
    
    public double getWaiting() {
        if (this.waiting.nextDouble() < 0.95) {
            return 15.0 * 60;
        } else {
            return 30.0 * 60;
        }
    }
    
    public double getDailySkipped() {
        int max = (int) Math.round(this.expectedArrivals * (25/540.0));
        int min = (int) Math.round(this.expectedArrivals * (5/540.0));
        return (this.dailySkipped.nextInt(max - min) + min) / (this.expectedArrivals);
    }
    
    public double getArrived() {
        return this.arrived.nextDouble();
    }

    public void setExpectedArrivals(double expectedArrivals) {
        this.expectedArrivals = expectedArrivals;
    }

    public int getRandomStaff(int interval) {
        return this.randomPickers.get(interval - 1).nextInt(interval);
    }
    
}
