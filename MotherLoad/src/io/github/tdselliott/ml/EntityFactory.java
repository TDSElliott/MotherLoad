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
        Player.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("PlayerLeft.png")), true); //Player image is 56x44
        //Player.getMainViewComponent().setView(new Rectangle(60, 60, Color.BLACK), true);

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
        ground.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("Ground" + Tier + ".png", 64, 64)), false); //Sets the image
        ground.getPositionComponent().setValue(x, y);

        //Control
        ground.addControl(new LandControl(Tier));

        return ground;
    }

    public static Entity newFuelShop(int x, int y) {
        GameEntity fuelShop = new GameEntity();
        // This sets physics to enabled, it allows the collision detection to work
        fuelShop.addComponent(new CollidableComponent(true));
        fuelShop.getTypeComponent().setValue(EntityType.FUELSHOP);
        fuelShop.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("fuelshop.png", 256, 256)), true);
        
        fuelShop.getPositionComponent().setValue(x, y);
        return fuelShop;
    }
    
    public static Entity newOreShop(int x, int y) {
        GameEntity oreShop = new GameEntity();
        // This sets physics to enabled, it allows the collision detection to work
        oreShop.addComponent(new CollidableComponent(true));
        oreShop.getTypeComponent().setValue(EntityType.ORESHOP);
        oreShop.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("oreshop.png", 256, 256)), true);
        
        oreShop.getPositionComponent().setValue(x, y);
        return oreShop;
    }
    
    public static Entity newRepairShop(int x, int y) {
        GameEntity repairShop = new GameEntity();
        // This sets physics to enabled, it allows the collision detection to work
        repairShop.addComponent(new CollidableComponent(true));
        repairShop.getTypeComponent().setValue(EntityType.REPAIRSHOP);
        repairShop.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("repairShop.png", 256, 256)), true);
        
        repairShop.getPositionComponent().setValue(x, y);
        return repairShop;
    }
    
    public static Entity newUpgradeShop(int x, int y) {
        GameEntity upgradeShop = new GameEntity();
        // This sets physics to enabled, it allows the collision detection to work
        upgradeShop.addComponent(new CollidableComponent(true));
        upgradeShop.getTypeComponent().setValue(EntityType.UPGRADE);
        upgradeShop.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("upgrade.png", 256, 256)), true);
        
        upgradeShop.getPositionComponent().setValue(x, y);
        return upgradeShop;
    }
}
