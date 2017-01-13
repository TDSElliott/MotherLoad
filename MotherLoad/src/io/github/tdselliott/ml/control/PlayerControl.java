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
import com.almasb.fxgl.entity.component.PositionComponent;

/**
 *
 * @author macko
 */
public class PlayerControl extends AbstractControl {
  
    protected PositionComponent position;

    private double positionX = 0;
    private double positionY = 0;
    private double velocityX = 0;
    private double velocityY = 0;
    private double accelerationX = .01;
    private double accelerationY = .01;

    private double velocityCapX = 5;
    private double velocityCapY = 1;
    private double velocityDecay = .03;

    boolean hKeyDown = false;

    public PlayerControl() {

    }

    public PlayerControl(double x, double y) {
        positionX = x;
        positionY = y;
    }

    @Override
    public void onAdded(Entity entity) {
        position = entity.getComponentUnsafe(PositionComponent.class);
    }

    @Override
    public void onUpdate(Entity entity, double d) {
        updatePosition();
        velocityDecay();
    }

    private void updatePosition() {
        positionX += velocityX;
        positionY += velocityY;
        position.setValue(positionX, positionY);
//        velocityY += 9.8;
    }

    public void velocityDecay() {

        if (velocityX != 0 && !hKeyDown) {
            if (velocityX > velocityDecay) {
                velocityX -= velocityDecay;
            } else if (velocityX < -velocityDecay) {
                velocityX += velocityDecay;
            } else {
                velocityX = 0;
            }
        }
        hKeyDown = false;
    }

    public void moveUp() {
        velocityY -= accelerationY;
    }

    public void moveHorizontal(boolean left) {
        hKeyDown = true;
        if (Math.abs(velocityX) < velocityCapX) {
            if (left) {
                velocityX -= accelerationX;
            } else {
                velocityX += accelerationX;
            }
        }
    }

    public void moveDown() {
        velocityY += accelerationY;//temp to be replaced with gravity
    }

}
