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
import io.github.tdselliott.ml.control.LandControl;
import io.github.tdselliott.ml.control.PlayerControl;
import java.util.ArrayList;
import java.util.Random;
import static javafx.application.Application.launch;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

// Joke/Reference List -- To be deleted only if you wish to incure the WRATH OF GOD
// Seriously I will kill you if you delete these - Tyler
// These to be inserted randomly around the code
// 867-5309
// Dr. D. Lerious
// Mr F
// more to follow...
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

    public static Entity[][] ground = new Entity[100][100];
    public static LandControl[][] CtrLand = new LandControl[ground.length][ground[0].length];

    public static ArrayList<String> in = new ArrayList(); //Inventory ArrayList

//------------------------------------------------------------------------------
    @Override
    protected void initSettings(GameSettings gs) {
        // The settings code to generate the window and remove FXGL intro for dev
        gs.setWidth(800);
        gs.setHeight(700);
        gs.setTitle("MotherLoad");
        gs.setVersion("0.01 [ALPHA]");
        gs.setIntroEnabled(false); // FXGL intro disabled (there is another mandatory load screen)
        gs.setMenuEnabled(false); // Disables initial menu
        gs.setMenuStyle(MenuStyle.WARCRAFT3);
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
                System.out.println(in);
            }
        }, MouseButton.PRIMARY);
        
        input.addAction(new UserAction("Move up") {
            @Override
            protected void onAction() {
                CtrPlayer.moveToMouse(input.getMousePositionWorld());
            }
        }, KeyCode.W);

        // Opens on any key you want (right now 'O') it's shop-idea
        input.addInputMapping(new InputMapping("Open", KeyCode.O));
    }
//------------------------------------------------------------------------------

    @Override
    protected void initAssets() {
    }
//------------------------------------------------------------------------------

    @Override
    protected void initGame() {

        //Create player
        player = EntityFactory.newPlayer(300, 100); //Adds player at (100, 100)
        getGameWorld().addEntity(player); //Adds player to the world
        CtrPlayer = player.getControlUnsafe(PlayerControl.class); //Sets the CtrPLayer class to the PlayerControl class

        //Ground start coordinates
        int groundStartX = 0;
        int groundStartY = 400;
        //Create ground
        for (int x = 0; x < ground.length; x++) { //X For loop
            for (int y = 0; y < ground[x].length; y++) { //Y For loop
                int TierSize = 5;
                
                boolean hasPickedGround = false;
                for (int z = 1; z < TierSize + 1; z++) {
//                    System.out.println(getDirtType(z, x));
                    if (getDirtType(z, y) > Math.random() && !hasPickedGround) {
//                        System.out.print("ye boi");
                        ground[x][y] = EntityFactory.newGround(64 * x + groundStartX, 64 * y + groundStartY, x, y, z);//dirt
                        CtrLand[x][y] = ground[x][y].getControlUnsafe(LandControl.class);
                        getGameWorld().addEntity(ground[x][y]);
                        hasPickedGround = true;
                        
                    }
                }
                if (!hasPickedGround) {
                    ground[x][y] = EntityFactory.newGround(64 * x + groundStartX, 64 * y + groundStartY, x, y, 0);//dirt
                    CtrLand[x][y] = ground[x][y].getControlUnsafe(LandControl.class);
                    getGameWorld().addEntity(ground[x][y]);
                }
                
                
            }
        }
        
        //LandControl.landPos = new Point2D(0, 400);

        // 1. load texture to be the background and specify orientation (horizontal or vertical) 
        //getGameScene().addGameView(new ScrollingBackgroundView(getAssetLoader().loadTexture("Background.png", 1066, 600),
        //        Orientation.HORIZONTAL));
        
        
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

    public double getDirtType(int Tier, int x) {

        double chance = -.00002 * (x + 40 - (20 * Tier)) * (x - 60 - (20 * Tier));
        return chance;
    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physicsWorld = getPhysicsWorld();
        physicsWorld.setGravity(0, 5);
    }
//------------------------------------------------------------------------------

    @Override
    protected void initUI() {
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

    public static Entity[][] getGround() {
        return ground;
    }

    public void setCamera() {
        // attach gameworld to object
        getGameScene().getViewport().bindToEntity(player, 400, 350);
    }

    @OnUserAction(name = "Open", type = ActionType.ON_ACTION_BEGIN)
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
}
////////////////////////////////////////////////////////////////////////////////
