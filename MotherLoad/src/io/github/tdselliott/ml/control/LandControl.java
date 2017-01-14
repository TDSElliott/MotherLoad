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

    public static Point2D landPos;
    
    private Point2D positionXY;
    
    public int arrayXValue;
    public int arrayYValue;
    
    public LandControl(double x, double y, int arrX, int arrY) {
        arrayXValue = arrX;
        arrayYValue = arrY;
        positionXY = new Point2D(x,y);
    }
    
    protected PositionComponent position;
    
    @Override
    public void onAdded(Entity entity) {
        position = entity.getComponentUnsafe(PositionComponent.class);
    }
    
    @Override
    public void onUpdate(Entity entity, double d) {
        
    }
    
    public void setLocation(Point2D pos) {
        position.setValue(pos);
    }
    
    public void moveLocation(Point2D pos) {
        position.setValue(position.getValue().add(pos));
    }
    
    public void updatePlayerSide(Point2D playerPos){
        
    }
}
