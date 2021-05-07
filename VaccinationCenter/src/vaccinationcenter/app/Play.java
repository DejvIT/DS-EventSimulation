/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.app;

import generators.ExponentialDistribution;
import generators.TriangularDistribution;
import vaccinationcenter.gui.GUI;


/**
 *
 * @author davidpavlicko
 */
public class Play {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
//        ExponentialDistribution exp = new ExponentialDistribution((double)1/260);
//        exp.writeTestValues(10000000);
//        TriangularDistribution trian = new TriangularDistribution(20, 100, 75);
//        trian.writeTestValues(10000);
        
        GUI g = new GUI();
        g.setVisible(true); 
    }
    
}
