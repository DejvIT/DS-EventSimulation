/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generators;

import java.util.Random;

/**
 * This class only returns a new seed value which can be used for a new Random init() function
 * @author davidpavlicko
 */
public class Seeder {
    
    private final Random seeder;
    
    public Seeder(int seed) {
        this.seeder = new Random(seed);
    }
    
    public Seeder() {
        this.seeder = new Random();
    }
    
    public int sample() {
        return this.seeder.nextInt();
    }
    
}
