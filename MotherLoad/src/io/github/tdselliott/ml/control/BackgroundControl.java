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
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.component.MainViewComponent;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.fxgl.entity.component.RotationComponent;
import com.almasb.fxgl.texture.Texture;
import io.github.tdselliott.ml.MotherLoadApp;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;

/**
 *
 * @author Mack
 */
public class BackgroundControl extends AbstractControl {

    protected PositionComponent position;
    protected MainViewComponent image;
    protected RotationComponent rotation;

    private double screenSizeMulitply = 1;
    public double screenSizeMin = 1;
    private int screenSizeX = 1600;
    private int screenSizeY = 1400;
    private int offsetX = 0;
    private int offsetY = 0;
    private int depth = 0;

    Texture scaledImage;
    private boolean isBackground;
    private boolean radarEnabled = false;

    private static AssetLoader assetLoader;

    MotherLoadApp app = (MotherLoadApp) FXGL.getApp();

    static {
        assetLoader = FXGL.getService(ServiceType.ASSET_LOADER);
    }

    public BackgroundControl(boolean background) {
        isBackground = background;
        scaledImage = assetLoader.loadTexture("Darkness.png", screenSizeX, screenSizeY);
    }

    @Override
    public void onAdded(Entity entity) {
        position = entity.getComponentUnsafe(PositionComponent.class);
        image = entity.getComponentUnsafe(MainViewComponent.class);
        rotation = entity.getComponentUnsafe(RotationComponent.class);
    }

    @Override
    public void onUpdate(Entity entity, double d) {
        if (!isBackground) {
            image.setRenderLayer(RenderLayer.TOP);
            if (radarEnabled) {
                rotation.rotateBy(2);
            }
            int posY = (int) Math.floor((400 + app.CtrPlayer.rtnPosition().getY()) / 64);

            if (posY >= 10) {
                screenSizeMulitply = (1 / (.02 * posY)) + 1;
            } else {
                screenSizeMulitply = 4;
            }

            if ((int) (screenSizeMulitply * 1600) != screenSizeX) {
                screenSizeX = (int) (screenSizeMulitply * 1600);
                screenSizeY = (int) (screenSizeMulitply * 1400);
                scaledImage.setScaleX(screenSizeMulitply);
                scaledImage.setScaleY(screenSizeMulitply);
                image.setView(scaledImage);
            }
        } else {
            image.setRenderLayer(RenderLayer.BACKGROUND);
        }
    }

    /**
     * enables radar
     */
    public void enableRadar() {
        scaledImage = assetLoader.loadTexture("Darkness_Radar.png", screenSizeX, screenSizeY);
        image.setView(scaledImage);
    }
        public void enableRadarSpin() {
        radarEnabled = true;
    }

    public void setPosition(double x, double y) {
        if (isBackground) {
            if (y < 400) {
                position.setValue(x, 400);
            } else {
                position.setValue(x, y);
            }
        } else {
            position.setValue(x - 375, y - 325);
        }

    }

}
