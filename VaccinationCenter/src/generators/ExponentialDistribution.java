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
public class ExponentialDistribution {
    
    private final Random random;
    private final double lambda;
    
    public ExponentialDistribution(double lambda, int seed) {
        this.lambda = lambda;
        this.random = new Random(seed);
    }
    
    public ExponentialDistribution(double lambda) {
        this.lambda = lambda;
        this.random = new Random();
    }

    public double getLambda() {
        return this.lambda;
    }
    
    /*
    Source EN wikipedia
    */
    public double sample() {
        return Math.log(1 - this.random.nextDouble())/(- this.lambda);
    }
    
    public void writeTestValues(int values) {
        try {
            FileWriter myWriter = new FileWriter("exponential_distribution_numbers.txt");
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
