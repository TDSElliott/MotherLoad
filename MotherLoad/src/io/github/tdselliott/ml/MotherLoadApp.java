package io.github.tdselliott.ml;

import com.almasb.ents.Entity;
import com.almasb.fxgl.entity.ScrollingBackgroundView;
import com.almasb.fxgl.input.ActionType;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.InputMapping;
import com.almasb.fxgl.input.OnUserAction;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.scene.menu.MenuStyle;
import com.almasb.fxgl.ui.InGameWindow;
import com.almasb.fxgl.ui.InGameWindow.WindowDecor;
import io.github.tdselliott.ml.control.PlayerControl;
import static javafx.application.Application.launch;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import com.almasb.fxgl.scene.FXGLMenu;
import com.almasb.fxgl.scene.menu.FXGLDefaultMenu;
import com.almasb.fxgl.scene.menu.MenuType;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.media.AudioClip;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.scene.SceneFactory;
import com.almasb.fxgl.settings.GameSettings;
import io.github.tdselliott.ml.control.BackgroundControl;
import javax.swing.JOptionPane;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Tyler Elliott
 * @author Mackenzie Guy
 * @author Logan Laird
 * @author Victor the Rooooski <-- Nice
 */
public class MotherLoadApp extends GameApplication {

    private Entity player, fuelShop, oreShop, repairShop, upgradeShop, underGroundBackground, darkness;
    public PlayerControl CtrPlayer;
    public BackgroundControl CtrBackground;
    public BackgroundControl CtrDarkness;
    public IntegerProperty fuel, armour, credits;
    private InGameWindow fuelWindow, armourWindow, shopWindow, upgradesWindow;

    private boolean mouseDown = false;

    public int maxArmour = 10, maxFuel = 1000;

    private int drillUpgardes = 0, lightUpgardes = 0, armorUpgardes = 0, engineUpgardes = 0, fuelUpgardes = 0;

    public static int ironOre, bronzeOre, silverOre, goldOre, titOre, estOre, emeraldOre, rubyOre, diamOre;
    private int fuelLoss = 1, fuelLossStatic = 1, fuelLossDynamic = 10;

    private AudioClip music;

    public static Entity[][] ground = new Entity[2000][15000];
    //public static LandControl[][] ctrLand = new LandControl[20000][5000];
    public static byte[][] arrTier = new byte[2000][15000];

//------------------------------------------------------------------------------
    /**
     * Sets the setting of the window and the game. Sets the height and width of
     * the game, sets the title and version number.
     *
     * @param gs - Game Settings
     */
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
        gs.setProfilingEnabled(false); // Profiing enabled/disabled (dev/release)
        gs.setCloseConfirmation(false); // Close warning enabled/disabled
        gs.setApplicationMode(ApplicationMode.RELEASE); // Dev, Debug, or Release
    }
//------------------------------------------------------------------------------

    //Sets the menu image.
    /**
     * //Sets the menu image
     *
     * @return - The menu image
     */
    @Override
    protected SceneFactory initSceneFactory() {
        return new SceneFactory() {

            //Override main menu
            @NotNull
            @Override
            public FXGLMenu newMainMenu(@NotNull GameApplication app) {
                return new FXGLDefaultMenu(app, MenuType.MAIN_MENU) {
                    @Override
                    protected Node createBackground(double width, double height) {
                        return getAssetLoader().loadTexture("Menu Image.png"); //Sets the image to the "Menu Image.png"
                    }

                };
            }

            //Override game menu
            @NotNull
            @Override
            public FXGLMenu newGameMenu(@NotNull GameApplication app) {
                return new FXGLDefaultMenu(app, MenuType.GAME_MENU) {
                    @Override
                    protected Node createBackground(double width, double height) {
                        return getAssetLoader().loadTexture("Menu Image.png"); //Sets the image to the "Menu Image.png"
                    }

                };
            }
        };
    }
//------------------------------------------------------------------------------

    /**
     * Gets the input from the user. Moves th user with the mouse or with "W"
     * key towards the mouse
     */
    @Override
    protected void initInput() {
        Input input = getInput(); //Get input service

        input.addAction(new UserAction("Move With Mouse") {
            @Override
            protected void onAction() { //When the mouse button is pressed
                CtrPlayer.moveToMouse(input.getMousePositionWorld()); //Do the method 'moveToMouse' in the PlayerControl class. (Moves the player towards the mouse)
                mouseDown = true; //Sets the mouseDown boolean to true
            }

            @Override
            protected void onActionEnd() { //When the mouse button is released
                mouseDown = false; //Sets the mouseDown boolean to false
            }
        }, MouseButton.PRIMARY); //All happens when the left mouse button is pressed

        input.addAction(new UserAction("Move up") {
            @Override
            protected void onAction() { //When the button is pressed
                CtrPlayer.moveToMouse(input.getMousePositionWorld()); //Do the method 'moveToMouse' in the PlayerControl class. (Moves the player towards the mouse)
            }
        }, KeyCode.W); //All this happens when the "W" button is pressed

        input.addInputMapping(new InputMapping("Open Fuel Shop", KeyCode.JAPANESE_KATAKANA));
        input.addInputMapping(new InputMapping("Open Selling Shop", KeyCode.JAPANESE_HIRAGANA));
        input.addInputMapping(new InputMapping("Open Armour Repair", KeyCode.JAPANESE_ROMAN));
        input.addInputMapping(new InputMapping("Open Upgrades Shop", KeyCode.UNDEFINED));
        // Needed to be left in because causes crash when removed.
    }
//------------------------------------------------------------------------------

    @Override
    protected void initAssets() {

    }
//------------------------------------------------------------------------------

    /**
     * Adds in the player, stores. Sets the music and sound volume.
     */
    @Override
    protected void initGame() { //All happens when the game start

        getAudioPlayer().setGlobalMusicVolume(0.3); //Sets the music volume to 30%
        getAudioPlayer().setGlobalSoundVolume(0.5); //Sets the sound volume to 50%
        getAudioPlayer().playMusic("29 BONUS Horror.mp3"); //Plays the background music
        //Create player
        player = EntityFactory.newPlayer(2000, 100); //Adds player at (100, 100)
        getGameWorld().addEntity(player); //Adds player to the world
        CtrPlayer = player.getControlUnsafe(PlayerControl.class); //Sets the CtrPLayer class to the PlayerControl class

        //Ground start coordinates
        int groundStartX = 0;
        int groundStartY = 400;

        //background
        underGroundBackground = EntityFactory.newFullScreenImage(true);
        getGameWorld().addEntity(underGroundBackground);
        CtrDarkness = underGroundBackground.getControlUnsafe(BackgroundControl.class);

        //darkness
        darkness = EntityFactory.newFullScreenImage(false);
        getGameWorld().addEntity(darkness);
        CtrBackground = darkness.getControlUnsafe(BackgroundControl.class);
        CtrBackground.enableRadar();

        // 1. load texture to be the background and specify orientation (horizontal or vertical) 
        getGameScene().addGameView(new ScrollingBackgroundView(getAssetLoader().loadTexture("Background.png", 1066, 600),
                Orientation.HORIZONTAL));
        getGameScene().getViewport().bindToEntity(player, 400 - 28, 350 - 28);

        //getGameScene().getViewport().setZoom(.5); //uncomment to see macks magic
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

    /**
     * Checks the collision between the player and the stores. Opens the store
     * windows if the player is in range of the store. Closes the store window
     * if the player is too far away from the store.
     */
    @Override
    protected void initPhysics() {
        PhysicsWorld physicsWorld = getPhysicsWorld(); //Creates a new PhysicsWorld
        physicsWorld.setGravity(0, 5);

        //Checks if the Player is colliding with the Fuelshop
        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FUELSHOP) {
            // Open on begin, close on end
            @Override
            protected void onCollisionBegin(Entity player, Entity fuelshop) {
                openFuelWindow();
                getAudioPlayer().playSound("Store Open.wav");
                CtrPlayer.isInMenu = true;

            }

            @Override
            protected void onCollisionEnd(Entity player, Entity fuelshop) {
                closeFuelWindow();
                getAudioPlayer().playSound("Store Close.wav");
                CtrPlayer.isInMenu = false;
            }
        });

        //Checks if the player is colliding with the oreshop
        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ORESHOP) {
            // Open on begin, close on end
            @Override
            protected void onCollisionBegin(Entity player, Entity oreshop) {
                openSellWindow();
                getAudioPlayer().playSound("Store Open.wav");
                CtrPlayer.isInMenu = true;
            }

            @Override
            protected void onCollisionEnd(Entity player, Entity oreshop) {
                closeSellWindow();
                getAudioPlayer().playSound("Store Close.wav");
                CtrPlayer.isInMenu = false;
            }
        });

        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.REPAIRSHOP) {
            // Open on begin, close on end
            @Override
            protected void onCollisionBegin(Entity player, Entity armourShop) {
                openArmourWindow();
                getAudioPlayer().playSound("Store Open.wav");
                CtrPlayer.isInMenu = true;
            }

            @Override
            protected void onCollisionEnd(Entity player, Entity armourShop) {
                closeArmourWindow();
                getAudioPlayer().playSound("Store Close.wav");
                CtrPlayer.isInMenu = false;
            }
        });

        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.UPGRADESHOP) {
            // Open on begin, close on end
            @Override
            protected void onCollisionBegin(Entity player, Entity upgradesShop) {
                openUpgradesWindow();
                getAudioPlayer().playSound("Store Open.wav");
                CtrPlayer.isInMenu = true;
            }

            @Override
            protected void onCollisionEnd(Entity player, Entity upgradesShop) {
                closeUpgradesWindow();
                getAudioPlayer().playSound("Store Close.wav");
                CtrPlayer.isInMenu = false;

            }
        });
    }
//------------------------------------------------------------------------------

    /**
     * Adds the fuel, armor, and credits counter to the world.
     */
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
        this.credits = new SimpleIntegerProperty(1000);
        Text creditsCounter = getUIFactory().newText("", Color.LIME, 20);
        creditsCounter.setTranslateX(590);
        creditsCounter.setTranslateY(25);
        creditsCounter.textProperty().bind(credits.asString("Credits: %d"));
        getGameScene().addUINode(creditsCounter);

    }
//------------------------------------------------------------------------------

    /**
     * Reduces the fuel by 10 if player is moving, or 1 of player is not moving
     *
     * @param d
     */
    @Override
    protected void onUpdate(double d) {
        upDateLand();
        // Fuel consumption increase if moving (if mouse held down) 
        if (mouseDown) {
            fuelLoss = fuelLossDynamic; // -10 fuel
        } else {
            fuelLoss = fuelLossStatic; // -1 fuel
        }
        if (fuel.get() <= 0) {
            JOptionPane.showMessageDialog(null, "GameOver, You ran out of fuel");
            System.out.println("GameOver");
            exit();
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

    /**
     *
     */
    public void upDateLand() {
        double camX = getGameScene().getViewport().getX();
        double camY = getGameScene().getViewport().getY() - 400;
        int posX = (int) Math.floor(camX / 64) - 1;
        int posY = (int) Math.floor(camY / 64) - 1;
        int groundStartX = 0;
        int groundStartY = 400;

        for (int y = posY; y < posY + 15; y++) { //X For loop
            for (int x = posX; x < posX + 15; x++) { //Y For loop
                int TierSize = 9; //Amount of ores in the game
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

    /**
     * Opens the fuelshop window
     */
    @OnUserAction(name = "Open Fuel Shop", type = ActionType.ON_ACTION_BEGIN)
    public void openFuelWindow() {

        // Create in-game window
        fuelWindow = new InGameWindow("Fuel Shop", WindowDecor.CLOSE); //Closes the window

        Button btnFuel = new Button();
        btnFuel.setText("Add Fuel");

        btnFuel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int cost = (maxFuel - fuel.get());
                System.out.println();
                if (credits.get() > cost) {
                    System.out.println("Fuel Refilled!");
                    fuel.set(maxFuel);
                    credits.set(credits.get() - cost);
                } else {
                    System.out.println("Fuel Semi-refilled!");
                    fuel.set(fuel.get() + credits.get());
                    credits.set(0);
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
                //Creates a new bank int variable and sets add the value of each ore multipled by the number of each ores you have
                int bank = 0;
                bank += ironOre * 20;
                bank += bronzeOre * 30;
                bank += silverOre * 40;
                bank += goldOre * 70;
                bank += titOre * 100;
                bank += estOre * 500;
                bank += emeraldOre * 1000;
                bank += rubyOre * 5000;
                bank += diamOre * 10000;

                //Sets the creadits to the value of the bank
                credits.set(credits.get() + bank);

                //Resets the bank value
                //Resets the ores back to 0
                ironOre = 0;
                bronzeOre = 0;
                silverOre = 0;
                goldOre = 0;
                titOre = 0;
                estOre = 0;
                emeraldOre = 0;
                rubyOre = 0;
                diamOre = 0;
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

    @OnUserAction(name = "Open Upgrades Shop", type = ActionType.ON_ACTION_BEGIN)
    public void openUpgradesWindow() {

        // Create in-game window
        upgradesWindow = new InGameWindow("Upgrades Shop", WindowDecor.CLOSE);

        Button Drill = new Button();
        Drill.setText("Upgrade Drill - " + ((int) (100 * Math.pow(drillUpgardes, 2)) + 100) + "CR");
        Drill.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (drillUpgardes < 10 && credits.get() > ((100 * Math.pow(drillUpgardes, 2)) + 100)) {
                    credits.set(credits.get() - ((int) (100 * Math.pow(drillUpgardes, 2)) + 100));
                    drillUpgardes++;
                    Drill.setText("Upgrade Drill - " + ((int) (100 * Math.pow(drillUpgardes, 2)) + 100) + "CR");
                    CtrPlayer.drillSpeed += 1;
                }
            }
        });
        Button Light = new Button();
        Light.setText("Upgrade Light - " + ((int) (100 * Math.pow(lightUpgardes, 2)) + 100) + "CR");
        Light.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (lightUpgardes < 10 && credits.get() > ((100 * Math.pow(lightUpgardes, 2)) + 100)) {
                    credits.set(credits.get() - ((int) (100 * Math.pow(lightUpgardes, 2)) + 100));
                    lightUpgardes++;
                    Light.setText("Upgrade Light - " + ((int) (100 * Math.pow(lightUpgardes, 2)) + 100) + "CR");
                    CtrDarkness.screenSizeMin += .25;
                }
            }
        });
        Button Armor = new Button();
        Armor.setText("Upgrade Armor - " + ((int) (100 * Math.pow(armorUpgardes, 2)) + 100) + "CR");
        Armor.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println((armorUpgardes < 10 && credits.get() > ((100 * Math.pow(armorUpgardes, 2)) + 100)));
                System.out.println(((100 * Math.pow(armorUpgardes, 2)) + 100));
                if (armorUpgardes < 10 && credits.get() > (((100 * Math.pow(armorUpgardes, 2)) + 100))) {
                    credits.set(credits.get() - ((int) (100 * Math.pow(armorUpgardes, 2)) + 100));
                    armorUpgardes++;
                    Armor.setText("Upgrade Armor - " + ((int) (100 * Math.pow(armorUpgardes, 2)) + 100) + "CR");
                    maxArmour += 10;
                    armour.add(armour.get() + 10);
                }
            }
        });
        Button Engine = new Button();
        Engine.setText("Upgrade Engine - " + ((int) (100 * Math.pow(engineUpgardes, 2)) + 100) + "CR");
        Engine.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (engineUpgardes < 10 && credits.get() > ((100 * Math.pow(engineUpgardes, 2)) + 100)) {
                    credits.set(credits.get() - ((int) (100 * Math.pow(engineUpgardes, 2)) + 100));
                    engineUpgardes++;
                    Engine.setText("Upgrade Engine - " + ((int) (100 * Math.pow(engineUpgardes, 2)) + 100) + "CR");
                    CtrPlayer.accelerationX += .01;
                    CtrPlayer.accelerationY += .01;
                }
            }
        });
        Button Fuel = new Button();
        Fuel.setText("Upgrade FuelTank - " + ((int) (100 * Math.pow(fuelUpgardes, 2)) + 100) + "CR");
        Fuel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (fuelUpgardes < 10 && credits.get() > ((100 * Math.pow(fuelUpgardes, 2)) + 100)) {
                    credits.set(credits.get() - (int) ((100 * Math.pow(fuelUpgardes, 2)) + 100));
                    fuelUpgardes++;
                    Fuel.setText("Upgrade FuelTank - " + ((int) (100 * Math.pow(lightUpgardes, 2)) + 100) + 100 + "CR");
                    maxFuel += 500 * Math.pow(fuelUpgardes, 2);
                }
            }
        });
        Button Radar = new Button();
        Radar.setText("Fix Stuck Radar Dish - " + 5000 + "CR");
        Radar.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (fuelUpgardes < 10 && credits.get() > 5000) {
                    if (!Radar.toString().contains("Sold")) {
                        credits.set(credits.get() - 5000);
                        Radar.setText("Upgrade Radar - Sold Out");
                        CtrBackground.enableRadarSpin();
                    }
                }
            }
        });

        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(10, 10, 10, 10));
        flow.setStyle("-fx-background-color: #6600ff;");
        flow.setHgap(5);
        flow.getChildren().addAll(Drill, Light, Armor, Engine, Fuel, Radar);

        upgradesWindow.setContentPane(flow);

        // Set properties
        upgradesWindow.setPrefSize(200, 300);
        upgradesWindow.setPosition(325, 400);
        upgradesWindow.setBackgroundColor(Color.ORANGE);

        // Attach to the game scene as a UI node
        getGameScene().addUINode(upgradesWindow);
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
                if (credits.get() >= 100) {
                    System.out.println("Armour repaired!");
                    credits.set(credits.get() - 100);
                }
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
        getGameScene().removeUINodes(fuelWindow);
    }
//------------------------------------------------------------------------------

    public void closeSellWindow() {
        getGameScene().removeUINodes(shopWindow);
    }
//------------------------------------------------------------------------------

    public void closeArmourWindow() {
        getGameScene().removeUINodes(armourWindow);
    }
//------------------------------------------------------------------------------

    public void closeUpgradesWindow() {
        getGameScene().removeUINodes(upgradesWindow);
    }
//------------------------------------------------------------------------------

    public double getDirtType(int Tier, int x) {

        double chance = -0.00003 * (x + 60 - (20 * Tier - 1)) * (x - 40 - (20 * Tier - 1));
        return chance;
    }
//------------------------------------------------------------------------------

    public void damagePlayer(int damage) {
        if (damage >= armour.get()) {
            armour.set(armour.get() - damage);
            //end game
            JOptionPane.showMessageDialog(null, "GameOver, You hit the ground too hard");
            System.out.println("GameOver");
            exit();
        } else {
            armour.set(armour.get() - damage);
        }

    }
}
////////////////////////////////////////////////////////////////////////////////
