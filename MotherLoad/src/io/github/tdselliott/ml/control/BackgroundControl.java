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

    /**
     * sets up if its a background image
     * @param background isbackground image?
     */
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

        /**
         * sets position
         * @param x x
         * @param y y
         */
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
