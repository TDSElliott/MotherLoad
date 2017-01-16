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
import io.github.tdselliott.ml.control.LandControl;
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
        //Player.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("PlayerLeft.png")), true);
        Player.getMainViewComponent().setView(new Rectangle(60, 60, Color.BLACK), true);

        //Components
        Player.addComponent(new CollidableComponent(true));

        //Control
        Player.addControl(new PlayerControl(x, y));

        //
        return Player;
    }

    public static Entity newGround(double x, double y, int arrX, int arrY, int Tier) {
        GameEntity ground = new GameEntity(); //Creates a new grounf GameEntity
        ground.getTypeComponent().setValue(EntityType.GROUND); //Sets it to the GROUND EntityType
        ground.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("Ground" + Tier + ".png")), true); //Sets the image
        ground.getPositionComponent().setValue(x, y);

        //Control
        ground.addControl(new LandControl(Tier));

        return ground;
    }
}
