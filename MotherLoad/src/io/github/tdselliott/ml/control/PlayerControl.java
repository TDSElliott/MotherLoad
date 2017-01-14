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
import javafx.geometry.Point2D;

/**
 *
 * @author Mackenzie Guy
 */
public class PlayerControl extends AbstractControl {

    protected PositionComponent position;

    private Point2D positionXY;
    private double velocityX = 0;
    private double velocityY = 0;
    private double accelerationX = .06;
    private double accelerationY = .12;

    private Point2D imageOffSet = new Point2D(25, 25);
    private int imageWidth = 60;
    private int imageHight = 60;

    private double gravity = 0.06;

    private double velocityCapX = 5;
    private double velocityCapY = 5;
    private double velocityDecay = .1;

    private boolean isInMenu = false;

    boolean wasOnGround = false;

    boolean hKeyDown = false;

    boolean groundDown = false;
    boolean groundUp = false;
    boolean groundLeft = false;
    boolean groundRight = false;

    public PlayerControl() {

    }

    public PlayerControl(double x, double y) {
        positionXY = new Point2D(x, y);
    }

    @Override
    public void onAdded(Entity entity) {
        position = entity.getComponentUnsafe(PositionComponent.class);
    }

    @Override
    public void onUpdate(Entity entity, double d) {
        updatePosition();
        velocityDecay();
        isColliding();
    }

    private void updatePosition() {

        velocityY += gravity;

        if (groundDown) {
            if (velocityY > 0) {
                velocityY = 0;
            }
        }

        positionXY = positionXY.add(velocityX, velocityY);
        position.setValue(positionXY);
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

    public void moveToMouse(Point2D mouse) {
        if (!isInMenu) {
            hKeyDown = true;
            double angleTemp = getAngle(positionXY.add(imageHight / 2, imageWidth / 2), mouse);

            if (Math.abs(velocityX) < velocityCapX) {
                velocityX += Math.cos(angleTemp) * accelerationX;
            }
            if (Math.abs(velocityY) < velocityCapY) {
                velocityY += Math.sin(angleTemp) * accelerationY;
            }

            System.out.println(angleTemp > Math.PI / 3 && angleTemp < 2 * Math.PI / 3);
            if (angleTemp > Math.PI / 3 && angleTemp < 2 * Math.PI / 3) {
                //dig();
            }
        }
    }

    public double getAngle(Point2D player, Point2D target) {
        double angle = Math.atan2(target.getY() - player.getY(), target.getX() - player.getX());

        if (angle < 0) {
            angle += Math.PI * 2;
        }

        return angle;
    }

    public void isColliding() {

        groundDown = false;
        groundUp = false;
        groundLeft = false;
        groundRight = false;

        Point2D landStart = LandControl.landPos;
        double xOffSet = -landStart.getX() + positionXY.getX();
        double yOffSet = -landStart.getY() + positionXY.getY();
        int arrX = (int) Math.floor(xOffSet / 64);
        int arrY = (int) Math.floor(yOffSet / 64);
        System.out.println("x = " + arrX + "y = " + arrY);

        //Colliding down
        if (arrY > -2) {
            groundDown = true;
        }
    }

    public void hitGround() {
        positionXY = positionXY.add(-velocityX, 0);
        positionXY = positionXY.add(0, -velocityY);
        velocityX = 0;
        velocityY = 0;
        wasOnGround = true;
    }

    public Point2D rtnPosition() {
        return positionXY;
    }

    public void triggerGround(int x) {
        switch (x) {
            case 1:
                groundDown = true;
            case 2:
                groundUp = true;
            case 3:
                groundLeft = true;
            case 4:
                groundRight = true;
        }
    }
}
