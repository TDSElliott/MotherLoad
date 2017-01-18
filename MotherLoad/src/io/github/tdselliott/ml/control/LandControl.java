/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.tdselliott.ml.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;

/**
 *
 * @author Mack
 */
public class LandControl extends AbstractControl {

    public int Tier;
    public boolean Mined = false;

    static {
    }

    @Override
    public void onAdded(Entity entity) {
    }
    
    public LandControl(int x) {
        Tier = x;
    }

    @Override
    public void onUpdate(Entity entity, double d) {
    }

}
