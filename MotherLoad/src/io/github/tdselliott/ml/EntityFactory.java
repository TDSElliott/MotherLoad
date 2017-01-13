/*
 * The MIT License
 *
 * Copyright 2016 Mackenzie G.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.tdselliott.ml;

import com.almasb.ents.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.ServiceType;
import com.almasb.fxgl.asset.AssetLoader;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.component.CollidableComponent;
import io.github.tdselliott.ml.control.PlayerControl;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Tyler
 */
public class EntityFactory {

    private static AssetLoader assetLoader;

    static {
        assetLoader = FXGL.getService(ServiceType.ASSET_LOADER);
    }

    public static Entity newPlayer(double x, double y) {
        //
        GameEntity Player = new GameEntity();

        //
        Player.getTypeComponent().setValue(EntityType.PLAYER);
        Player.getPositionComponent().setValue(x, y);
        Player.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("PlayerLeft.png")), true);
        //Player.getMainViewComponent().setView(new Rectangle(5, 5, Color.BLACK), true);

        //Components
        Player.addComponent(new CollidableComponent(true));

        //Control
        Player.addControl(new PlayerControl(x, y));

        //
        return Player;
    }
    
    public static Entity newGroundTest(double x, double y) {
        GameEntity ground = new GameEntity(); //Creates a new GameEntity called "bullet"
        ground.getTypeComponent().setValue(EntityType.GROUND); //Sets it to the "BULLET" EnitityType
        ground.getPositionComponent().setValue(x + 20, y + 20); //Adds the bullet at the given coordinates.
        ground.getMainViewComponent().setView(new Circle(10, 10, 10, Color.RED), true); //Makes the bullet a small red circle
        ground.addComponent(new CollidableComponent(true)); //Makes it so that the bullet can collide with other GameEntites
//        ground.addControl(new BulletControl(a)); //Sets the control to the BulletControl class
        
        return ground;
    }

}
