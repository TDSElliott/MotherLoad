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
import java.util.ArrayList;
import java.util.Random;
import static javafx.application.Application.launch;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javafx.util.Duration;

import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.event.NotificationEvent;
import com.almasb.fxgl.io.DataFile;
import com.almasb.fxgl.scene.FXGLMenu;
import com.almasb.fxgl.scene.SceneFactory;
import com.almasb.fxgl.scene.menu.FXGLDefaultMenu;
import com.almasb.fxgl.scene.menu.MenuType;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.settings.UserProfile;
import com.almasb.fxgl.time.UpdateEvent;
import java.io.Serializable;
import javafx.scene.Node;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import javafx.scene.media.AudioClip;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.scene.IntroScene;
import com.almasb.fxgl.scene.SceneFactory;
import com.almasb.fxgl.scene.intro.VideoIntroScene;
import com.almasb.fxgl.settings.GameSettings;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Tyler Elliott
 * @author Mackenzie Guy
 * @author Logan Laird
 * @author Victor the Rooooski <-- Nice
 */
public class MotherLoadApp extends GameApplication {

    private Entity player, fuelShop, oreShop, repairShop, upgradeShop;
    private PlayerControl CtrPlayer;
    private IntegerProperty fuel, armour, credits;
    private InGameWindow fuelWindow, armourWindow, shopWindow;
    private boolean mouseDown = false;
    
    public static int ironOre, bronzeOre, silverOre, goldOre, titOre;
    // fuelLoss and fuelLossStatic will be the same, but it cleans up lower code
    private int fuelLoss = 1, fuelLossStatic = 1, fuelLossDynamic = 10;
    
    //    AudioPlayer ap;
    //    Music m;
    private AudioClip music;

    public static Entity[][] ground = new Entity[1000][15000];
    //public static LandControl[][] ctrLand = new LandControl[20000][5000];
    public static byte[][] arrTier = new byte[1000][15000];

    public static byte[] inventory = new byte[10]; //Inventory

//------------------------------------------------------------------------------
    @Override
    protected void initSettings(GameSettings gs) {
        // The settings code to generate the window and remove FXGL intro for dev
        gs.setWidth(800);
        gs.setHeight(700);
        gs.setTitle("MotherLoad");
        gs.setVersion("0.5 [BETA]");
        gs.setIntroEnabled(false);
        gs.setMenuEnabled(true); //Change later
        gs.setMenuStyle(MenuStyle.WARCRAFT3);
        gs.setProfilingEnabled(true); // Profiing enabled/disabled (dev/release)
        gs.setCloseConfirmation(false); // Close warning enabled/disabled
        gs.setApplicationMode(ApplicationMode.DEVELOPER); // Dev, Debug, or Release
    }
//------------------------------------------------------------------------------

    @Override
    protected SceneFactory initSceneFactory() {
        return new SceneFactory() {

            // 2. override main menu and things you need
            @NotNull
            @Override
            public FXGLMenu newMainMenu(@NotNull GameApplication app) {
                return new FXGLDefaultMenu(app, MenuType.MAIN_MENU) {
                    @Override
                    protected Node createBackground(double width, double height) {
                        return getAssetLoader().loadTexture("Menu Image.png");
                    }

                };
            }

            // 4. override game menu
            @NotNull
            @Override
            public FXGLMenu newGameMenu(@NotNull GameApplication app) {
                return new FXGLDefaultMenu(app, MenuType.GAME_MENU) {
                    @Override
                    protected Node createBackground(double width, double height) {
                        return getAssetLoader().loadTexture("Menu Image.png");
                    }

                };
            }
        };
    }
//------------------------------------------------------------------------------
    @Override
    public DataFile saveState() {
        // save state into `data`
        Serializable data = arrTier;

    return new DataFile(data);
    }
//------------------------------------------------------------------------------
    @Override
    public void loadState(DataFile dataFile) {
        // SomeType is the actual type of the object serialized
        // e.g. String, Bundle, HashMap, etc.
        byte[][] data = (byte[][]) dataFile.getData();
        data = arrTier;
        // do something with `data`
    }
//------------------------------------------------------------------------------
    @Override
    protected void initInput() {
        Input input = getInput(); // get input service
        
        input.addAction(new UserAction("Move With Mouse") {
            @Override
            protected void onAction() {
                CtrPlayer.moveToMouse(input.getMousePositionWorld());
                mouseDown = true;
            }
            @Override
            protected void onActionEnd() {
                mouseDown = false;
            }
        }, MouseButton.PRIMARY);

        input.addAction(new UserAction("Move up") {
            @Override
            protected void onAction() {
                CtrPlayer.moveToMouse(input.getMousePositionWorld());
            }
        }, KeyCode.W);

        // Dev-code, the end-user should open by moving to shop 1- fuel 2 - ore 3 - armour
        input.addInputMapping(new InputMapping("Open Fuel Shop", KeyCode.DIGIT1));
        input.addInputMapping(new InputMapping("Open Selling Shop", KeyCode.DIGIT2));
        input.addInputMapping(new InputMapping("Open Armour Repair", KeyCode.DIGIT3));
    }
//------------------------------------------------------------------------------
    @Override
    protected void initAssets() {
    }
//------------------------------------------------------------------------------
    @Override
    protected void initGame() {
        getAudioPlayer().setGlobalMusicVolume(0.3);
        getAudioPlayer().setGlobalSoundVolume(0.5);
        getAudioPlayer().playMusic("29 BONUS Horror.mp3");

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
        //getGameScene().getViewport().setZoom(.5);
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
        
        getMasterTimer().runAtInterval(() -> { // lambda (calling a method with parameters and code seperated by ->)
            fuel.set(fuel.get() - fuelLoss); // Set the counter down
        }, Duration.millis(250)); // Every second (250 millis == 1/4 second)
        
        fuelShop = EntityFactory.newFuelShop(1000, 164); //Adds player at (100, 100)
        oreShop = EntityFactory.newOreShop(1500, 164); //Adds player at (100, 100)
        repairShop = EntityFactory.newRepairShop(2000, 164); //Adds player at (100, 100)
        upgradeShop = EntityFactory.newUpgradeShop(2500, 164); //Adds player at (100, 100)
        getGameWorld().addEntities(fuelShop, repairShop, oreShop, upgradeShop); //Adds player to the world  
    }
//------------------------------------------------------------------------------
    @Override
    protected void initPhysics() {
        PhysicsWorld physicsWorld = getPhysicsWorld();
        physicsWorld.setGravity(0, 5);
        
        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FUELSHOP) {
            // Open on begin, close on end
            @Override
            protected void onCollisionBegin(Entity player, Entity fuelshop) {
                openFuelWindow();
            }
            @Override
            protected void onCollisionEnd(Entity player, Entity fuelshop) {
                closeFuelWindow();
            }
        });
        
        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ORESHOP) {
            // Open on begin, close on end
            @Override
            protected void onCollisionBegin(Entity player, Entity oreshop) {
                openSellWindow();
            }
            @Override
            protected void onCollisionEnd(Entity player, Entity oreshop) {
                closeSellWindow();
            }
        });
        
        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.REPAIRSHOP) {
            // Open on begin, close on end
            @Override
            protected void onCollisionBegin(Entity player, Entity armourShop) {
                openArmourWindow();
            }
            @Override
            protected void onCollisionEnd(Entity player, Entity armourShop) {
                closeArmourWindow();
            }
        });
        
        // Setting the zone for opening shop menu 
//        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FUELSHOP) {
//            @Override
//            protected void onCollisionBegin(Entity player, Entity fuelshop) {
//                System.out.print("Open fuel:");
//                openFuelWindow();
//            }  
//            protected void onCollisonEnd(Entity player, Entity fuelshop) {
//                System.out.print("Close fuel:");
//                closeFuelWindow();
//            }
//            
//        });
    }
//------------------------------------------------------------------------------
    @Override
    protected void initUI() {
        // The fuel counter being added to the UI
        this.fuel = new SimpleIntegerProperty(1000);
        Text fuelCounter = getUIFactory().newText("", Color.CADETBLUE, 20);
        fuelCounter.setTranslateX(10);
        fuelCounter.setTranslateY(25);
        fuelCounter.textProperty().bind(fuel.asString("Fuel Remaining: %d"));
        getGameScene().addUINode(fuelCounter);
        
        // The armour counter being added to the UI
        this.armour = new SimpleIntegerProperty(10);
        Text armourCounter = getUIFactory().newText("", Color.CRIMSON, 20);
        armourCounter.setTranslateX(300);
        armourCounter.setTranslateY(25);
        armourCounter.textProperty().bind(armour.asString("Armour Level: %d"));
        getGameScene().addUINode(armourCounter);
        
        // The credits counter being added to the UI
        this.credits = new SimpleIntegerProperty(100);
        Text creditsCounter = getUIFactory().newText("", Color.LIME, 20);
        creditsCounter.setTranslateX(590);
        creditsCounter.setTranslateY(25);
        creditsCounter.textProperty().bind(credits.asString("Credits: %d"));
        getGameScene().addUINode(creditsCounter);
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
        upDateLand();
        // Fuel consumption increase if moving (if mouse held down) 
        if(mouseDown) {
            fuelLoss = fuelLossDynamic; // 10
        } else {
            fuelLoss = fuelLossStatic; // 1
        }
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

                if (y >= 0 && x >= 0 && arrTier[x][y] == 0) {
                    for (int z = 2; z < TierSize + 2; z++) {
                        if (getDirtType(z, y) > Math.random() && !hasPickedGround) {
                            ground[x][y] = EntityFactory.newGround(64 * x + groundStartX, 64 * y + groundStartY, x, y, z);
                            arrTier[x][y] = (byte) z;
                            //ctrLand[x][y] = ground[x][y].getControlUnsafe(LandControl.class);
                            getGameWorld().addEntity(ground[x][y]);
                            hasPickedGround = true;
                        }
                    }
                    if (!hasPickedGround) {
                        ground[x][y] = EntityFactory.newGround(64 * x + groundStartX, 64 * y + groundStartY, x, y, 1);
                        arrTier[x][y] = 1;
                        //ctrLand[x][y] = ground[x][y].getControlUnsafe(LandControl.class);
                        getGameWorld().addEntity(ground[x][y]);
                    }
                }
            }
        }
        for (int y = posY - 1; y < posY + 16; y++) { //X For loop
            for (int x = posX - 1; x < posX + 16; x++) { //Y For loop
                if (y >= 0 && x >= 0) {
                    if ((y == posY - 1 || y == posY + 15) || (x == posX - 1 || x == posX + 15)) {
                        if (ground[x][y] != null) {
                            if (!ground[x][y].isActive() && arrTier[x][y] > 0) {
                                ground[x][y] = EntityFactory.newGround(64 * x + groundStartX, 64 * y + groundStartY, x, y, arrTier[x][y]);
                                //ctrLand[x][y] = ground[x][y].getControlUnsafe(LandControl.class);
                                getGameWorld().addEntity(ground[x][y]);
                            }
                        }
                    }
                }
            }
        }
        for (int y = posY - 2; y < posY + 18; y++) { //X For loop
            for (int x = posX - 2; x < posX + 18; x++) { //Y For loop
                if (y >= 0 && x >= 0) {
                    if ((y == posY - 2 || y == posY + 17) || (x == posX - 2 || x == posX + 17)) {
                        if (arrTier[x][y] > 0) {
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
    @OnUserAction(name = "Open Fuel Shop", type = ActionType.ON_ACTION_BEGIN)
    public void openFuelWindow() {

        // Create in-game window
        fuelWindow = new InGameWindow("Fuel Shop", WindowDecor.CLOSE);
        
        Button btnFuel = new Button();
        btnFuel.setText("Add Fuel");
        
        btnFuel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(credits.get() > 0) {
                    System.out.println("Fuel refilled!");
                    fuel.set(fuel.get() + 100);
                    credits.set(credits.get() - 10);
                }
            }
        });
        
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(10, 10, 10, 10));
        flow.setStyle("-fx-background-color: #33cc33;");
        flow.setHgap(5);
        flow.getChildren().addAll(btnFuel);
       
        
        fuelWindow.setContentPane(flow);
        
        // Set properties
        fuelWindow.setPrefSize(150, 50);
        fuelWindow.setPosition(0, 600);
        fuelWindow.setBackgroundColor(Color.ORANGE);

        // Attach to the game scene as a UI node
        getGameScene().addUINode(fuelWindow);
    }
//------------------------------------------------------------------------------
    @OnUserAction(name = "Open Selling Shop", type = ActionType.ON_ACTION_BEGIN)
    public void openSellWindow() {

        // Create in-game window
        shopWindow = new InGameWindow("Sell Ore", WindowDecor.CLOSE);
        
        Button sellOre = new Button();
        sellOre.setText("Sell your Ore");
        sellOre.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                int bank = 0;
                bank += ironOre *   10;
                bank += bronzeOre * 100;
                bank += silverOre * 1000;
                bank += goldOre *   10000;
                bank += titOre *    100000;
                
                credits.set(credits.get() + bank);
                bank = 0;
                ironOre = 0; bronzeOre = 0; silverOre = 0; goldOre = 0; titOre = 0;
            }
        });
        
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(10, 10, 10, 10));
        flow.setStyle("-fx-background-color: #6600ff;");
        flow.setHgap(5);
        flow.getChildren().addAll(sellOre);
       
        
        shopWindow.setContentPane(flow);
        
        // Set properties
        shopWindow.setPrefSize(150, 50);
        shopWindow.setPosition(325, 600);
        shopWindow.setBackgroundColor(Color.ORANGE);

        // Attach to the game scene as a UI node
        getGameScene().addUINode(shopWindow);
    }
//------------------------------------------------------------------------------
    @OnUserAction(name = "Open Armour Repair", type = ActionType.ON_ACTION_BEGIN)
    public void openArmourWindow() {

        // Create in-game window
        armourWindow = new InGameWindow("Armour Repair", WindowDecor.CLOSE);
        
        Button armour = new Button();
        armour.setText("Repair Armour");
        armour.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Armour repaired!");
            }
        });
        
        
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(10, 10, 10, 10));
        flow.setStyle("-fx-background-color: #DAE6F3;");
        flow.setHgap(5);
        flow.getChildren().addAll(armour);
       
        
        armourWindow.setContentPane(flow);
        
        // Set properties
        armourWindow.setPrefSize(150, 50);
        armourWindow.setPosition(650, 600);
        armourWindow.setBackgroundColor(Color.ORANGE);

        // Attach to the game scene as a UI node
        getGameScene().addUINode(armourWindow);
    }
//------------------------------------------------------------------------------
    public void closeFuelWindow() {
        System.out.println("run");
        getGameScene().removeUINodes(fuelWindow);
    }
//------------------------------------------------------------------------------
    public void closeSellWindow() {
        System.out.println("run");
        getGameScene().removeUINodes(shopWindow);
    }
//------------------------------------------------------------------------------
    public void closeArmourWindow() {
        System.out.println("run");
        getGameScene().removeUINodes(armourWindow);
    }
//------------------------------------------------------------------------------
    public double getDirtType(int Tier, int x) {

        double chance = -.00002 * (x + 20 - (20 * Tier)) * (x - 40 - (20 * Tier));
        return chance;
    }
//------------------------------------------------------------------------------
}
////////////////////////////////////////////////////////////////////////////////
