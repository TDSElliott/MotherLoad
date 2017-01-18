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
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.ScrollingBackgroundView;
import com.almasb.fxgl.gameplay.qte.QTE;
import com.almasb.fxgl.input.ActionType;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.InputMapping;
import com.almasb.fxgl.input.OnUserAction;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.scene.SceneFactory;
import com.almasb.fxgl.scene.menu.MenuStyle;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.ui.InGameWindow;
import com.almasb.fxgl.ui.InGameWindow.WindowDecor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.github.tdselliott.ml.control.LandControl;
import io.github.tdselliott.ml.control.PlayerControl;
import io.github.tdselliott.ml.ui.InventoryView;
import java.util.ArrayList;
import java.util.Random;
import static javafx.application.Application.launch;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


import javafx.util.Duration;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 *
 * @author Tyler Elliott
 * @author Mackenzie Guy
 * @author Logan Laird
 * @author Victor the Rooooski <-- Nice
 */
public class MotherLoadApp extends GameApplication {

    private Entity player;
    private PlayerControl CtrPlayer;

    public static Entity[][] ground = new Entity[5000][5000];
    public static LandControl[][] ctrLand = new LandControl[5000][5000];
    public static byte[][] arrTier = new byte[5000][5000];
    
    public static ArrayList<String> in = new ArrayList(); //Inventory ArrayList

//------------------------------------------------------------------------------
    @Override
    protected void initSettings(GameSettings gs) {
        // The settings code to generate the window and remove FXGL intro for dev
        gs.setWidth(800);
        gs.setHeight(700);
        gs.setTitle("MotherLoad");
        gs.setVersion("0.01 [ALPHA]");
        gs.setIntroEnabled(false); 
        gs.setMenuEnabled(false); //Change later
        gs.setMenuStyle(MenuStyle.GTA5);
        gs.setProfilingEnabled(true); // Profiing enabled/disabled (dev/release)
        gs.setCloseConfirmation(false); // Close warning enabled/disabled
        gs.setApplicationMode(ApplicationMode.DEVELOPER); // Dev, Debug, or Release
    }
//------------------------------------------------------------------------------
    @Override
    protected void initInput() {
        Input input = getInput(); // get input service

        input.addAction(new UserAction("Move With Mouse") {
            @Override
            protected void onAction() {
                CtrPlayer.moveToMouse(input.getMousePositionWorld());
            }
        }, MouseButton.PRIMARY);

        input.addAction(new UserAction("Move up") {
            @Override
            protected void onAction() {
                CtrPlayer.moveToMouse(input.getMousePositionWorld());
            }
        }, KeyCode.W);

        // Opens on any key you want (right now 'O') it's shop-idea
        input.addInputMapping(new InputMapping("Open Fuel Shop", KeyCode.DIGIT1));
        input.addInputMapping(new InputMapping("Open Sell", KeyCode.DIGIT2));
    }
//------------------------------------------------------------------------------
    @Override
    protected void initAssets() {
    }
//------------------------------------------------------------------------------
    @Override
    protected void initGame() {

        //Create player
        player = EntityFactory.newPlayer(2000, 100); //Adds player at (100, 100)
        getGameWorld().addEntity(player); //Adds player to the world
        CtrPlayer = player.getControlUnsafe(PlayerControl.class); //Sets the CtrPLayer class to the PlayerControl class

        //Ground start coordinates
        int groundStartX = 0;
        int groundStartY = 400;

        // 1. load texture to be the background and specify orientation (horizontal or vertical) 
//        getGameScene().addGameView(new ScrollingBackgroundView(getAssetLoader().loadTexture("Background.png", 1066, 600),
//                Orientation.HORIZONTAL));
        getGameScene().getViewport().bindToEntity(player, 400, 350);
        // QUICKTIME EVENTS CODE BELOW, for reference, currently timed
        // Uncomment to use as is, take away the timer to use as a once-off
//        getMasterTimer().runAtInterval(() -> {
//            // 1. get QTE service
//            QTE qte = getQTE();
//            
//            // 2. start event with duration and keys to be pressed
//            qte.start(yes -> {
//                // This is the example, 'yes' is used to determine success/failure
//                System.out.println("Successful? " + yes);
//            }, Duration.seconds(25), KeyCode.T, KeyCode.Y, KeyCode.L, KeyCode.E, KeyCode.R);
//        }, Duration.seconds(5));
    }
//------------------------------------------------------------------------------
    @Override
    protected void initPhysics() {
        PhysicsWorld physicsWorld = getPhysicsWorld();
        physicsWorld.setGravity(0, 5);
    }
//------------------------------------------------------------------------------
    @Override
    protected void initUI() {
        getGameScene().addUINode(new InventoryView(player, getWidth(), getHeight()));
//        Texture texture = getAssetLoader().loadTexture("Background.png");
//
//        //Creates a new EntityView called "bg" and sets it to the texure previously created
//        EntityView bg = new EntityView(texture);
//
//        //Adds the "bg" entityview to the game
//        getGameScene().addGameView(bg);
    }
//------------------------------------------------------------------------------
    @Override
    protected void onUpdate(double d) {
        setCamera();
        upDateLand();
    }
//------------------------------------------------------------------------------
    /**
     * Contains FXGL code to launch the window. Nothing else will need to be
     * added in here.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
//------------------------------------------------------------------------------
    public void setCamera() {
        // attach gameworld to object
        //getGameScene().getViewport().bindToEntity(player, 400, 350);
    }
//------------------------------------------------------------------------------
    @OnUserAction(name = "Open Fuel Shop", type = ActionType.ON_ACTION_BEGIN)
    public void openWindow() {

        // Create in-game window
        InGameWindow window = new InGameWindow("Feul Shop", WindowDecor.CLOSE);

        // Set properties
        window.setPrefSize(300, 200);
        window.setPosition(400, 300);
        window.setBackgroundColor(Color.ORANGE);

        // Attach to the game scene as a UI node
        getGameScene().addUINode(window);
    }
//------------------------------------------------------------------------------
    public void upDateLand() {
        double camX = getGameScene().getViewport().getX();
        double camY = getGameScene().getViewport().getY() - 400;
        int posX = (int) Math.floor(camX / 64) - 1;
        int posY = (int) Math.floor(camY / 64) - 1;
        int groundStartX = 0;
        int groundStartY = 400;

        for (int y = posY; y < posY + 15; y++) { //X For loop
            for (int x = posX; x < posX + 15; x++) { //Y For loop
                int TierSize = 5; //Amount of ores in the game
                boolean hasPickedGround = false;

                if (y >= 0 && x >= 0 && ground[x][y] == null) {
                    for (int z = 1; z < TierSize + 1; z++) {
                        if (getDirtType(z, y) > Math.random() && !hasPickedGround) {
                            ground[x][y] = EntityFactory.newGround(64 * x + groundStartX, 64 * y + groundStartY, x, y, z);
                            arrTier[x][y] = (byte)z;
                            ctrLand[x][y] = ground[x][y].getControlUnsafe(LandControl.class);
                            getGameWorld().addEntity(ground[x][y]);
                            hasPickedGround = true;
                        }
                    }
                    if (!hasPickedGround) {
                        ground[x][y] = EntityFactory.newGround(64 * x + groundStartX, 64 * y + groundStartY, x, y, 0);
                        arrTier[x][y] = 0;
                        ctrLand[x][y] = ground[x][y].getControlUnsafe(LandControl.class);
                        getGameWorld().addEntity(ground[x][y]);
                    }
                }
            }
        }
        for (int y = posY - 1; y < posY + 16; y++) { //X For loop
            for (int x = posX - 1; x < posX + 16; x++) { //Y For loop
                if (y >= 0 && x >= 0) {
                    if ((y == posY - 1 || y == posY + 16) || (x == posX - 1 || x == posX + 15)) {
                        if (ground[x][y] != null) {
                            if (!ground[x][y].isActive() && !ctrLand[x][y].Mined) {
                                ground[x][y] = EntityFactory.newGround(64 * x + groundStartX, 64 * y + groundStartY, x, y, arrTier[x][y]);
                                ctrLand[x][y] = ground[x][y].getControlUnsafe(LandControl.class);
                                getGameWorld().addEntity(ground[x][y]);
                            }
                        }
                    }
                }
            }
        }
        for (int y = posY - 2; y < posY + 17; y++) { //X For loop
            for (int x = posX - 2; x < posX + 17; x++) { //Y For loop
                if (y >= 0 && x >= 0) {
                    if ((y == posY - 2 || y == posY + 17) || (x == posX - 2 || x == posX + 17)) {
                        if (ground[x][y] != null) {
                            if (ground[x][y].isActive()) {
                                getGameWorld().removeEntity(ground[x][y]);
                            }
                        }
                    }
                }
            }
        }
    }
//------------------------------------------------------------------------------
    @OnUserAction(name = "Open Sell", type = ActionType.ON_ACTION_BEGIN)
    public void openWindow2() {

        // Create in-game window
        InGameWindow window = new InGameWindow("Sell", WindowDecor.CLOSE);

        // Set properties
        window.setPrefSize(300, 200);
        window.setPosition(400, 300);
//        window.setBackgroundColor(Color.BLUE);
//        window.set

        // Attach to the game scene as a UI node
        getGameScene().addUINode(window);
    }
//------------------------------------------------------------------------------
    public double getDirtType(int Tier, int x) {

        double chance = -.00002 * (x + 40 - (20 * Tier)) * (x - 60 - (20 * Tier));
        return chance;
    }
}
////////////////////////////////////////////////////////////////////////////////
