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
import io.github.tdselliott.ml.MotherLoadApp;
import javafx.geometry.Point2D;

/**
 *
 * @author Mackenzie Guy
 * @author The Rooski
 */
public class PlayerControl extends AbstractControl {

    //Creates all the reqired variables
    protected PositionComponent position;

    private Point2D positionXY;
    private Point2D mouseXY;
    private Point2D simMouseXY;
    private double velocityX = 0;
    private double velocityY = 0;
    private double accelerationX = .06;
    private double accelerationY = .12;

    private int imageWidth = 60;
    private int imageHight = 60;

    private double gravity = 0.06;

    private double velocityCapX = 5;
    private double velocityCapY = 5;
    private double velocityDecay = .1;

    private boolean isInMenu = false;
    private boolean mouseHeld = false;

    private boolean isPointDown = false;
    private boolean isPointUp = false;
    private boolean isPointLeft = false;
    private boolean isPointRight = false;

    boolean hKeyDown = false;

    boolean groundDown = false;
    boolean groundUp = false;
    boolean groundLeft = false;
    boolean groundRight = false;

    public PlayerControl() {

    }

    public PlayerControl(double x, double y) {
        positionXY = new Point2D(x, y); //Sets the positionXY variable equal to the Point2D with the given coordinates
    }

    @Override
    public void onAdded(Entity entity) {
        position = entity.getComponentUnsafe(PositionComponent.class);
    }

    @Override
    public void onUpdate(Entity entity, double d) {
        //Does the following methods on every update
        updatePosition();
        velocityDecay();
        isColliding();

    }

    private void updatePosition() {

        velocityY += gravity; //Adds the gravity value to the velocityY variable

        if (groundDown) {
            if (velocityY > 0) {
                velocityY = 0;
            }
        }
        if (groundLeft) {
            if (velocityX < 0) {
                velocityX = 0;
            }
        }
        if (groundRight) {
            if (velocityX > 0) {
                velocityX = 0;
            }
        }
        if (groundUp) {
            if (velocityY < 0) {
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

    public void mouseDown(Point2D mouse) {
        mouseXY = mouse;
        mouseHeld = true;
    }

    public void moveToMouse(Point2D mouse) {
        System.out.println(mouse == mouseXY);
        if(mouse == mouseXY){
            simMouseXY.add(velocityX, velocityY);
        } else {
            simMouseXY = mouse;
        }
        
        mouseHeld = true;
        if (!isInMenu) {
            hKeyDown = true;
            double angleTemp = getAngle(positionXY.add(imageHight / 2, imageWidth / 2), simMouseXY);

            if (Math.abs(velocityX) < velocityCapX) {
                velocityX += Math.cos(angleTemp) * accelerationX;
            }
            if (Math.abs(velocityY) < velocityCapY) {
                velocityY += Math.sin(angleTemp) * accelerationY;
            }

            if (angleTemp > Math.PI / 4 && angleTemp < 3 * Math.PI / 4) {
                isPointDown = true;
            } else if (angleTemp > 5 * Math.PI / 4 && angleTemp < 7 * Math.PI / 4) {
                isPointUp = true;
            } else if (angleTemp > 3 * Math.PI / 4 && angleTemp < 5 * Math.PI / 4) {
                isPointLeft = true;
            } else {
                isPointRight = true;
            }
            mouseXY = mouse;
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

        //Point2D landStart = LandControl.landPos;
        Point2D landStart = new Point2D(0, 400);
        double xOffSet = -landStart.getX() + positionXY.getX() + imageWidth / 2;
        double yOffSet = -landStart.getY() + positionXY.getY();
        int arrX = (int) Math.floor(xOffSet / 64);
        int arrY = (int) Math.floor(yOffSet / 64);

        if (arrX >= 0 && arrY >= -1) {
            //Colliding down
            if (MotherLoadApp.ground[arrX][arrY + 1].isActive()) {
                groundDown = true;
                if (isPointDown) {
                    MotherLoadApp.ground[arrX][arrY + 1].removeAllComponents();
                    MotherLoadApp.ground[arrX][arrY + 1].removeFromWorld();
                
                    
                    oreType(arrX, arrY);
                }
            }

            //Colliding Left/Right
            if (arrY >= 0 && arrX > 0) {
                double tempX = xOffSet - imageWidth / 2;
                //left
                if (MotherLoadApp.ground[arrX - 1][arrY].isActive()) {
                    if ((int) (Math.floor(tempX / 64)) != arrX) {
                        groundLeft = true;
                        if (isPointLeft && groundDown) {
                            MotherLoadApp.ground[arrX - 1][arrY].removeAllComponents();
                            MotherLoadApp.ground[arrX - 1][arrY].removeFromWorld();
          
                            oreType(arrX, arrY);
                        }
                    }
                }
                //right
                if (MotherLoadApp.ground[arrX + 1][arrY].isActive()) {
                    if ((int) (Math.floor((tempX + imageWidth) / 64)) != arrX) {

                        groundRight = true;
                        if (isPointRight && groundDown) {
                            MotherLoadApp.ground[arrX + 1][arrY].removeAllComponents();
                            MotherLoadApp.ground[arrX + 1][arrY].removeFromWorld();

                            oreType(arrX, arrY);

                        }
                    }
                }
            }
            //Colliding Up
            if (arrY > 0) {
                int tmpArrY = (int) Math.floor((yOffSet + imageHight) / 64);
                if (MotherLoadApp.ground[arrX][tmpArrY - 1].isActive()) {
                    groundUp = true;
                }
            }
        }
        isPointDown = false;
        isPointUp = false;
        isPointLeft = false;
        isPointRight = false;

    }

    public Point2D rtnPosition() {
        return positionXY;
    }

    public void oreType(int arrX, int arrY) {
        
        if (MotherLoadApp.CtrLand[arrX][arrY + 1].Tier == 1) {
            MotherLoadApp.in.add("Iron");
            System.out.println(MotherLoadApp.in);
        } else if(MotherLoadApp.CtrLand[arrX][arrY + 1].Tier == 2) {
            MotherLoadApp.in.add("Bronze");
            System.out.println(MotherLoadApp.in);
        }
    }

}
