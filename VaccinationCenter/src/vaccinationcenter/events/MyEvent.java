/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.events;

import cores.Event;
import vaccinationcenter.agents.Person;
import vaccinationcenter.app.VaccinationCenter;

/**
 *
 * @author davidpavlicko
 */
public abstract class MyEvent extends Event {
    
    protected Person person;
    
    @Override
    public VaccinationCenter getSimulation() {
        return (VaccinationCenter) super.simulation;
    }
}
