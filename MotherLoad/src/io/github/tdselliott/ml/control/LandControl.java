/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.tdselliott.ml.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.entity.component.PositionComponent;
import java.util.ArrayList;

/**
 *
 * @author Mack
 */
public class LandControl extends AbstractControl {

    protected PositionComponent position;
    
    @Override
    public void onAdded(Entity entity) {
        position = entity.getComponentUnsafe(PositionComponent.class);
    }
    
    @Override
    public void onUpdate(Entity entity, double d) {
        
    }
    
    public void move(double x, double y) {
        
    }
    
}
