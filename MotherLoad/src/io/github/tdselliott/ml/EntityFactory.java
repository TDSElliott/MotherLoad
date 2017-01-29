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
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.component.CollidableComponent;
import io.github.tdselliott.ml.control.BackgroundControl;
import io.github.tdselliott.ml.control.LandControl;
import io.github.tdselliott.ml.control.PlayerControl;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Victor
 * @author Tyler
 */
public class EntityFactory {

    private static AssetLoader assetLoader;

    static {
        assetLoader = FXGL.getService(ServiceType.ASSET_LOADER);
    }

    /**
     * Creates a player, sets it to the PLAYER entitytype, adds the player at the specified coordinates, adds the collidablecomponent, and sets the control.
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @return - The player GameEntity
     */
    public static Entity newPlayer(double x, double y) {
        //Creates a new game entity called Player
        GameEntity Player = new GameEntity();


        //
        Player.getTypeComponent().setValue(EntityType.PLAYER);
        Player.getPositionComponent().setValue(x, y);
        Player.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("NewPlayer.png")), true);
        //Player.getMainViewComponent().setView(new Rectangle(50, 50, Color.BLACK), true); Placeholder


        //Components
        Player.addComponent(new CollidableComponent(true));

        //Control
        Player.addControl(new PlayerControl(x, y));
        
        return Player;
    }

    /**
     * Creates the ground (Including the ores).
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @param arrX - The x spot in the ground array.
     * @param arrY - The y spot in the ground array
     * @param Tier - The ground tier number
     * @return - The ground GameEntity
     */
    public static Entity newGround(double x, double y, int arrX, int arrY, int Tier) {
        GameEntity ground = new GameEntity(); //Creates a new ground GameEntity
        ground.getTypeComponent().setValue(EntityType.GROUND); //Sets it to the GROUND EntityType
        ground.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("Ground" + Tier + ".png", 64, 64)), false); //Sets the image

        ground.getPositionComponent().setValue(x, y);
        ground.getMainViewComponent().setRenderLayer(RenderLayer.BACKGROUND);


        //Control
        ground.addControl(new LandControl(Tier));

        return ground;
    }


    public static Entity newFullScreenImage(boolean isBackground) {
        GameEntity image = new GameEntity(); //Creates a new grounf GameEntity
        if (isBackground) {
            image.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("UG_Background.png", 1000, 800)), false);
            image.getMainViewComponent().setRenderLayer(RenderLayer.TOP);
        } else {
            image.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("Darkness.png", 800, 700)), false);
            image.getMainViewComponent().setRenderLayer(RenderLayer.BACKGROUND);
        }
        image.addControl(new BackgroundControl(isBackground));
        return image;
    }


    /**
     * Creates the fuelShop.
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @return - The fuelShop GameEntity
     */

    public static Entity newFuelShop(int x, int y) {
        GameEntity fuelShop = new GameEntity();
        // This sets physics to enabled, it allows the collision detection to work
        fuelShop.addComponent(new CollidableComponent(true));
        fuelShop.getTypeComponent().setValue(EntityType.FUELSHOP);

        fuelShop.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("fuelshop.png", 256, 256)), true); //Sets the image
        
        fuelShop.getPositionComponent().setValue(x, y);
        return fuelShop;
    }
    
    /**
     * Creates the oreShop. 
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @return - The oreShop GameEntity
     */

    public static Entity newOreShop(int x, int y) {
        GameEntity oreShop = new GameEntity();
        // This sets physics to enabled, it allows the collision detection to work
        oreShop.addComponent(new CollidableComponent(true));
        oreShop.getTypeComponent().setValue(EntityType.ORESHOP);

        oreShop.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("oreshop.png", 256, 256)), true); //Sets the image
        
        oreShop.getPositionComponent().setValue(x, y);
        return oreShop;
    }
    
    /**
     * Creates the repairShop.
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @return - The repairShop GameEntity
     */

    public static Entity newRepairShop(int x, int y) {
        GameEntity repairShop = new GameEntity();
        // This sets physics to enabled, it allows the collision detection to work
        repairShop.addComponent(new CollidableComponent(true));
        repairShop.getTypeComponent().setValue(EntityType.REPAIRSHOP);
        repairShop.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("repairShop.png", 256, 256)), true);

        repairShop.getPositionComponent().setValue(x, y);
        return repairShop;
    }

    
    /**
     * Creates the upgradeShop.
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @return - The repairShop GameEntity.
     */

    public static Entity newUpgradeShop(int x, int y) {
        GameEntity upgradeShop = new GameEntity();
        // This sets physics to enabled, it allows the collision detection to work
        upgradeShop.addComponent(new CollidableComponent(true));
        upgradeShop.getTypeComponent().setValue(EntityType.UPGRADESHOP);
        upgradeShop.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("upgrade.png", 256, 256)), true);

        upgradeShop.getPositionComponent().setValue(x, y);
        return upgradeShop;
    }
}
