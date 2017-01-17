/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.tdselliott.ml.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.ServiceType;
import com.almasb.fxgl.asset.AssetLoader;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.component.MainViewComponent;
import com.almasb.fxgl.entity.component.PositionComponent;
import java.util.ArrayList;
import javafx.geometry.Point2D;

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
