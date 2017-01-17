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
import javafx.geometry.Point2D;

/**
 *
 * @author Mack
 */
public class LandControl extends AbstractControl {

    public int Tier;
    
    public LandControl(int x) {
        Tier = x;
        System.out.println(Tier);
    }
    
    @Override
    public void onUpdate(Entity entity, double d) {
        
    }
    
}
