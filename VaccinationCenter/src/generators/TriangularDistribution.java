/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author davidpavlicko
 */
public class TriangularDistribution {
    
    private final int min;
    private final int max;
    private final int mode;
    private final Random random;
    
    public TriangularDistribution(int min, int max, int mode, int seed) {
        this.min = min;
        this.max = max;
        this.mode = mode;
        this.random = new Random(seed);
    }
    
    public TriangularDistribution(int min, int max, int mode) {
        this.min = min;
        this.max = max;
        this.mode = mode;
        this.random = new Random();
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    public int getMode() {
        return this.mode;
    }
    
    /*
    Source EN wikipedia
    */
    public double sample() {
        double F = (double) (this.mode - this.min) / (this.max - this.min);
        double rand = this.random.nextDouble();
        if (rand < F) {
            return this.min + Math.sqrt(rand * (this.max - this.min) * (this.mode - this.min));
        } else {
            return this.max - Math.sqrt((1 - rand) * (this.max - this.min) * (this.max - this.mode));
        }
    }
    
    /*
    Source w3schools
    */
    public void writeTestValues(int values) {
        try {
            FileWriter myWriter = new FileWriter("triangular_distribution_numbers.txt");
            for (int i = 0; i < values; i++) {
                myWriter.write(sample() + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
