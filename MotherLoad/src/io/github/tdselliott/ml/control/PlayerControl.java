package io.github.tdselliott.ml.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import static com.almasb.fxgl.app.FXGL.getAudioPlayer;
import com.almasb.fxgl.entity.component.PositionComponent;
import io.github.tdselliott.ml.MotherLoadApp;
import javafx.geometry.Point2D;

/**
 *
 * @author Tyler Elliott
 * @author Mackenzie Guy
 * @author The Roooski
 * @author Logan laird
 */
public class PlayerControl extends AbstractControl {

    //Creates all the reqired variables
    protected PositionComponent position;

    private Point2D positionXY;
    private Point2D mouseXY;
    private Point2D simMouseXY = new Point2D(0, 0);
    private double velocityX = 0;
    private double velocityY = 0;
    private double accelerationX = .06;
    private double accelerationY = .12;
    private double lastAngle = 0;

    private int imageWidth = 20;
    private int imageHight = 20;

    private double gravity = 0.06;

    private double velocityCapX = 5;
    private double velocityCapY = 5;
    private double velocityDecay = .1;

    private boolean isInMenu = false;
    private boolean mouseDownBug = false;
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

        if (velocityY < velocityCapY) {
            velocityY += gravity; //Adds the gravity value to the velocityY variable
        }
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

    public void mouseDown(Point2D mouse2) {
        mouseXY = mouse2;
    }

    public void moveToMouse(Point2D mouse) {
        if ((int) mouse.getX() == (int) simMouseXY.getX() && (int) mouse.getY() == (int) simMouseXY.getY()) {
            mouseDownBug = true;
        } else {
            mouseDownBug = false;
        }

        if (!isInMenu) {
            hKeyDown = true;
            
            double angleTemp;
            if (mouseDownBug) {
                angleTemp = lastAngle;
                System.out.println("y");
            } else {
                angleTemp = getAngle(positionXY.add(imageHight / 2, imageWidth / 2), mouse);
                lastAngle = angleTemp;
                System.out.println("n");
            }

            if (Math.abs(velocityX) < velocityCapX) {
                velocityX += Math.cos(angleTemp) * accelerationX;
            }
            velocityY += Math.sin(angleTemp) * accelerationY;
            if (Math.abs(velocityY) < velocityCapY) {
                velocityY += Math.sin(angleTemp) * accelerationY;
            } else if (velocityY > velocityCapY && Math.sin(angleTemp) * accelerationY < 0) {
                velocityY += Math.sin(angleTemp) * accelerationY;
            }

            if (angleTemp > Math.PI / 4 && angleTemp < 3 * Math.PI / 4) {
                isPointDown = true;
            } else if (angleTemp > 5 * Math.PI / 4 && angleTemp < 7 * Math.PI / 4) {
                //break isPointUp = true;
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

        int arrXDown1 = getPosLandX(0, false, true);
        int arrXDown2 = getPosLandX(1, false, true);
        int arrXDown3 = getPosLandX(2, false, true);
        int arrYDown = getPosLandY(1, true, true);

        if (arrYDown >= 0) {
            if (MotherLoadApp.ground[arrXDown1][arrYDown].isActive()) {
                groundDown = true;
                positionXY = positionXY.add(0, -velocityY);
            } else if (MotherLoadApp.ground[arrXDown2][arrYDown].isActive()) {
                groundDown = true;
                positionXY = positionXY.add(0, -velocityY);
            }
            if (isPointDown && MotherLoadApp.ground[arrXDown3][arrYDown].isActive()) {
                oreType(arrXDown3, arrYDown);
                MotherLoadApp.ground[arrXDown3][arrYDown].removeFromWorld();
                MotherLoadApp.arrTier[arrXDown3][arrYDown] = -1;
                getAudioPlayer().playSound("Dig.wav");
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
                System.out.println(MotherLoadApp.ground[arrXLeft][arrYLR3].isActive() && groundDown);
                if (MotherLoadApp.ground[arrXLeft][arrYLR1].isActive()) {
                    groundLeft = true;
                    if (!groundLeft) {
                        positionXY = positionXY.add(-velocityX, 0);
                    }
                }
                if (isPointLeft && MotherLoadApp.ground[arrXLeft][arrYLR3].isActive() && groundDown) {
                    oreType(arrXLeft, arrYLR3);
                    MotherLoadApp.ground[arrXLeft][arrYLR3].removeFromWorld();
                    MotherLoadApp.arrTier[arrXLeft][arrYLR3] = -1;
                    getAudioPlayer().playSound("Dig.wav");
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
                    oreType(arrXRight, arrYLR3);
                    MotherLoadApp.ground[arrXRight][arrYLR3].removeFromWorld();
                    MotherLoadApp.arrTier[arrXRight][arrYLR3] = -1;
                    getAudioPlayer().playSound("Dig.wav");
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
        isPointUp = false;
        isPointLeft = false;
        isPointRight = false;
    }

    public int getPosLandX(int point, boolean isSide, boolean Left) {
        Point2D landStart = new Point2D(0, 400);
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

    public int getPosLandY(int point, boolean isSide, boolean Down) {
        Point2D landStart = new Point2D(0, 400);
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

    public Point2D rtnPosition() {
        return positionXY;
    }

    public void oreType(int arrX, int arrY) {

        switch (MotherLoadApp.arrTier[arrX][arrY]) {
            case 2:
                MotherLoadApp.ironOre++;
                System.out.println("Iron: " + MotherLoadApp.ironOre);
                break;
            case 3:
                MotherLoadApp.bronzeOre++;
                System.out.println("Bronze: " + MotherLoadApp.bronzeOre);
                break;
            case 4:
                MotherLoadApp.silverOre++;
                System.out.println("Silver: " + MotherLoadApp.silverOre);
                break;
            case 5:
                MotherLoadApp.goldOre++;
                System.out.println("Gold: " + MotherLoadApp.goldOre);
                break;
            case 6:
                MotherLoadApp.titOre++;
                System.out.println("Titanium: " + MotherLoadApp.titOre);
                break;
            default:
                break;
        }
    }
}
