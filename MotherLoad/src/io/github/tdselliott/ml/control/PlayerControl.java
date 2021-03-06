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
import static com.almasb.fxgl.app.FXGL.getAudioPlayer;
import com.almasb.fxgl.entity.component.PositionComponent;
import io.github.tdselliott.ml.MotherLoadApp;
import javafx.geometry.Point2D;

/**
 * Player movement code 
 * contains - hitboxes, velocitys, mining
 * @author Mackenzie Guy
 */
public class PlayerControl extends AbstractControl {

    //Creates all the reqired variables
    protected PositionComponent position;

    private MotherLoadApp app = (MotherLoadApp) FXGL.getApp();
    private Point2D landStart = new Point2D(0, 400);
    private Point2D positionXY;
    private Point2D simMouseXY = new Point2D(0, 0);
    private double velocityX = 0;
    private double velocityY = 0;
    public double accelerationX = .06;
    public double accelerationY = .12;
    private double lastAngle = 0;

    private final int imageWidth = 50;
    private final int imageHight = 50;

    private double gravity = 0.06;

    private final double velocityCapX = 7;
    private final double velocityCapY = 10;
    private final double velocityDecay = .1;

    public boolean isInMenu = false;
    private boolean mouseDownBug = false;

    //what way is player pointing
    private boolean isPointDown = false;
    private boolean isPointLeft = false;
    private boolean isPointRight = false;

    //is player moving
    boolean hKeyDown = false;

    //is blocks near miner
    boolean groundDown = false;
    boolean groundUp = false;
    boolean groundLeft = false;
    boolean groundRight = false;

    ////////////////////////////////Animate Vars////////////////////////////////
    private Point2D animateTargit;
    private boolean isAnimating = false;
    private boolean lessThenX = false;
    private boolean lessThenY = false;

    public double drillSpeed = 1;

    private int animateX;
    private int animateY;
    private double animateAngle;

    ////////////////////////////////////////////////////////////////////////////
    public PlayerControl() {

    }
    
    /**
     * add at XY
     * @param x x
     * @param y y
     */
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
        if (!isAnimating) {
            updatePosition();
            velocityDecay();
            isColliding();
        } else {
            animateToBlock();
        }

        app.CtrBackground.setPosition(positionXY.getX() - 400, positionXY.getY() - 350);
        app.CtrDarkness.setPosition(positionXY.getX() - 400, positionXY.getY() - 350);
//        System.out.println("player Hight = " + getPosLandY(2, true, true));
//        app.CtrDarkness.setDepth(getPosLandY(2, false, false));
    }

    /**
     * Velocity and move code
     */
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

        int arrX = getPosLandX(0, false, true);
        if (arrX < 10 || arrX > 1990) {
            velocityX = -velocityX;
        }

        if (velocityY > velocityCapY) {
            velocityY = 10;
        } else if (velocityY < -velocityCapY) {
            velocityY = -10;
        }
        if (velocityX > velocityCapX) {
            velocityX = 7;
        } else if (velocityX < -velocityCapX) {
            velocityX = -7;
        }

        positionXY = positionXY.add(velocityX, velocityY);
        position.setValue(positionXY);

    }

    /**
     * stops moving after time
     */
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

    /**
     * moves to the mouse
     * @param mouse Point2D mouse's XY pos
     */
    public void moveToMouse(Point2D mouse) {
        mouseDownBug = (int) mouse.getX() == (int) simMouseXY.getX() && (int) mouse.getY() == (int) simMouseXY.getY();

        if (!isAnimating) {
            hKeyDown = true;

            double angleTemp;
            if (mouseDownBug) {
                angleTemp = lastAngle;
            } else {
                angleTemp = getAngle(positionXY.add(imageHight / 2, imageWidth / 2), mouse);
                lastAngle = angleTemp;
                simMouseXY = mouse;
            }

            velocityX += Math.cos(angleTemp) * accelerationX;
            velocityY += Math.sin(angleTemp) * accelerationY;

            if (angleTemp > Math.PI / 4 && angleTemp < 3 * Math.PI / 4) {
                isPointDown = true;
            } else if (angleTemp > 5 * Math.PI / 4 && angleTemp < 7 * Math.PI / 4) {
                //break
            } else if (angleTemp > 3 * Math.PI / 4 && angleTemp < 5 * Math.PI / 4) {
                isPointLeft = true;
            } else {
                isPointRight = true;
            }
        }
    }

    /**
     * moves to a block and removes it
     */
    public void animateToBlock() {

        double x = Math.cos(animateAngle) * drillSpeed;
        double y = Math.sin(animateAngle) * drillSpeed;

        positionXY = positionXY.add(x, y);
        position.setValue(positionXY);

        double offSetX = positionXY.getX() - animateTargit.getX();
        double offSetY = positionXY.getY() - animateTargit.getY();

        if (((lessThenX && offSetX <= 0) || (!lessThenX && offSetX >= 0))
                && (lessThenY && offSetY <= 0) || (!lessThenY && offSetY >= 0)) {
            oreType(animateX, animateY);
            MotherLoadApp.ground[animateX][animateY].removeFromWorld();
            MotherLoadApp.arrTier[animateX][animateY] = -1;
            getAudioPlayer().playSound("Dig.wav"); //Plays the digging sound
            isAnimating = false;
        }
    }

    /**
     * starts the drilling animation
     * @param x landY pos to move to
     * @param y landX pos to move to
     * @param side is it on the X-axis
     */
    public void setAnimateStart(int x, int y, boolean side) {
        if (!isInMenu) {
            double pointY;

            lessThenX = false;
            lessThenY = false;
            isAnimating = true;

            animateX = x;
            animateY = y;

            double pointX = (64 * x) + 32 - (imageWidth / 2);
            if (side) {
                pointY = (64 * y) + landStart.getY() + 62 - imageHight;
            } else {
                pointY = (64 * y) + landStart.getY() + 55 - imageHight;
            }

            animateTargit = new Point2D(pointX, pointY);

            animateAngle = getAngle(positionXY, animateTargit);

            if (pointX < positionXY.getX()) {
                lessThenX = true;
            }
            if (pointY < positionXY.getY()) {
                lessThenY = true;
            }
        }
    }

    /**
     * gets angle between two points
     * @param player point 1
     * @param target point 2
     * @return angle
     */
    public double getAngle(Point2D player, Point2D target) {
        double angle = Math.atan2(target.getY() - player.getY(), target.getX() - player.getX());

        if (angle < 0) {
            angle += Math.PI * 2;
        }

        return angle;
    }

    /**
     * checks if colliding with land if so act accordingly
     */
    public void isColliding() {

        groundDown = false;
        groundUp = false;
        groundLeft = false;
        groundRight = false;

        int arrXDown1 = getPosLandX(0, false, true);
        int arrXDown2 = getPosLandX(1, false, true);
        int arrXDown3 = getPosLandX(2, false, true);
        int arrYDown = getPosLandY(1, true, true);

        if (arrYDown >= 0) {
            if (MotherLoadApp.ground[arrXDown1][arrYDown].isActive()) {
                groundDown = true;
                if (velocityY > 9) {
                    app.damagePlayer(25);
                } else if (velocityY > 8) {
                    app.damagePlayer(10);
                } else if (velocityY > 7) {
                    app.damagePlayer(10);
                }

                if (Math.abs(velocityY) > 1) {
                    positionXY = positionXY.add(0, -velocityY);
                }
            } else if (MotherLoadApp.ground[arrXDown2][arrYDown].isActive()) {
                groundDown = true;
                if (velocityY > 9) {
                    app.damagePlayer(25);
                } else if (velocityY > 8) {
                    app.damagePlayer(10);
                } else if (velocityY > 7) {
                    app.damagePlayer(10);
                }
                if (Math.abs(velocityY) > 1) {
                    positionXY = positionXY.add(0, -velocityY);
                }
            }
            if (isPointDown && MotherLoadApp.ground[arrXDown3][arrYDown].isActive()) {
                setAnimateStart(arrXDown3, arrYDown, false);
            }
        }
        //left / right

        int arrYLR1 = getPosLandY(0, false, true);
        int arrYLR2 = getPosLandY(1, false, true);
        int arrYLR3 = getPosLandY(2, false, true);

        //left
        int arrXLeft = getPosLandX(0, true, true);
        //right
        int arrXRight = getPosLandX(1, true, false);
        if (arrYLR2 >= 0) {
            //left
            if (MotherLoadApp.ground[arrXLeft][arrYLR2].isActive()) {
                groundLeft = true;
                positionXY = positionXY.add(-velocityX, 0);
            }
            if (arrYLR1 >= 0) {
                if (MotherLoadApp.ground[arrXLeft][arrYLR1].isActive()) {
                    groundLeft = true;
                    if (!groundLeft) {
                        positionXY = positionXY.add(-velocityX, 0);
                    }
                }
                if (isPointLeft && MotherLoadApp.ground[arrXLeft][arrYLR3].isActive() && groundDown) {
                    setAnimateStart(arrXLeft, arrYLR3, true);
                }
            }
            //right
            if (MotherLoadApp.ground[arrXRight][arrYLR2].isActive()) {
                groundRight = true;
                positionXY = positionXY.add(-velocityX, 0);
            }
            if (arrYLR1 >= 0) {
                if (MotherLoadApp.ground[arrXRight][arrYLR1].isActive()) {
                    groundRight = true;
                    if (!groundRight) {
                        positionXY = positionXY.add(-velocityX, 0);
                    }
                }
                if (isPointRight && MotherLoadApp.ground[arrXRight][arrYLR3].isActive() && groundDown) {
                    setAnimateStart(arrXRight, arrYLR3, true);
                }
            }
        }

        //up
        int arrYUp = getPosLandY(0, true, false);
        int arrXup1 = getPosLandX(0, false, false);
        int arrXup2 = getPosLandX(1, false, false);
        if (arrYUp >= 0) {
            if (MotherLoadApp.ground[arrXup1][arrYUp].isActive()) {
                groundUp = true;
                positionXY = positionXY.add(0, -velocityY);
            } else if (MotherLoadApp.ground[arrXup2][arrYUp].isActive()) {
                groundUp = true;
                positionXY = positionXY.add(0, -velocityY);
            }
        }

        isPointDown = false;
        isPointLeft = false;
        isPointRight = false;
    }

    /**
     * gets the x pos of the land array
     * @param point where on the lander to check top middle or bottem
     * @param isSide in checking on X-axis
     * @param Left is on left side
     * @return int Xpos of land array
     */
    public int getPosLandX(int point, boolean isSide, boolean Left) {

        double offset;
        switch (point) {
            case 0:
                offset = -landStart.getX() + positionXY.getX();
                break;
            case 1:
                offset = -landStart.getX() + positionXY.getX() + imageWidth;
                break;
            case 2:
                offset = -landStart.getX() + positionXY.getX() + imageWidth / 2;
                break;
            default:
                offset = 0;
        }
        if (isSide) {
            if (Left) {
                offset -= 1;
            } else {
                offset += 1;
            }
        }

        int posX = (int) Math.floor(offset / 64);
        return posX;
    }

    /**
     * gets the x pos of the land array
     * @param point where on the lander to check top middle or bottem
     * @param isSide in checking on Y-axis
     * @param Down is on bottem of the carft
     * @return int Ypos of land array
     */
    public int getPosLandY(int point, boolean isSide, boolean Down) {
        double offset;
        switch (point) {
            case 0:
                offset = -landStart.getY() + positionXY.getY();
                break;
            case 1:
                offset = -landStart.getY() + positionXY.getY() + imageWidth;
                break;
            case 2:
                offset = -landStart.getY() + positionXY.getY() + imageWidth / 2;
                break;
            default:
                offset = 0;
        }
        if (isSide) {
            if (Down) {
                offset += 1;
            } else {
                offset -= 1;
            }
        }

        int posY = (int) Math.floor(offset / 64);
        return posY;
    }

    /**
     * returns the location of the miner
     * @return the point2d of the minder
     */
    public Point2D rtnPosition() {
        return positionXY;
    }

    /**
     * adds ores to backend invintory to be sold
     */
    /**
     * Checks what ore is drilled and adds the specific ore to the inventory. Plays the drilling sound.
     * @param arrX - the X position in the ground array
     * @param arrY - the Y position in the ground array
     */
    public void oreType(int arrX, int arrY) {

        //Checks the tier number of the drilled block
        switch (MotherLoadApp.arrTier[arrX][arrY]) {
            case 2: //If the tier is 2 (Iron)
                MotherLoadApp.ironOre++; //Adds one to the ironOre variable
                getAudioPlayer().playSound("Iron Ore.wav"); //Plays the iron drilling sound
                break;
            case 3: //If the tier is 3 (Bronze)
                MotherLoadApp.bronzeOre++; //Adds one to the ironOre variable
                getAudioPlayer().playSound("Bronze Ore.wav"); //Plays the bronze drilling sound
                break;
            case 4:
                MotherLoadApp.silverOre++;
                getAudioPlayer().playSound("Silver Ore.wav");
                break;
            case 5:
                MotherLoadApp.goldOre++;
                getAudioPlayer().playSound("Gold Ore.wav");
                break;
            case 6:
                MotherLoadApp.titOre++;
                getAudioPlayer().playSound("Titanium Ore.wav");
                break;
            case 7:
                MotherLoadApp.estOre++;
                getAudioPlayer().playSound("Est Ore.wav");
                break;
            case 8:
                MotherLoadApp.emeraldOre++;
                getAudioPlayer().playSound("Emerald Ore.wav");
                break;
            case 9:
                MotherLoadApp.rubyOre++;
                getAudioPlayer().playSound("Ruby Ore.wav");
                break;
            case 10:
                MotherLoadApp.diamOre++;
                getAudioPlayer().playSound("Diamond Ore.wav");
                break;
            default:
                break;
        }
    }
}
