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
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;
import io.github.tdselliott.ml.control.PlayerControl;
import static javafx.application.Application.launch;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Tyler Elliott
 * @author Mackenzie Guy
 * @author Logan Laird
 * @author Victor Russan
 */
public class MotherLoadApp extends GameApplication {

    private Entity player;
    private PlayerControl CtrPlayer;
    
    
    @Override
    protected void initSettings(GameSettings gs) {
        // The settings code to generate the window and remove FXGL intro for dev
        gs.setWidth(800);
        gs.setHeight(700);
        gs.setTitle("MotherLoad");
        gs.setVersion("0.01 [ALPHA]");
        gs.setIntroEnabled(false); // FXGL intro disabled (there is another mandatory load screen)
        gs.setMenuEnabled(false); // Disables initial menu
        gs.setProfilingEnabled(true); // Profiing enabled/disabled (dev/release)
        gs.setCloseConfirmation(false); // Close warning enabled/disabled
        gs.setApplicationMode(ApplicationMode.DEVELOPER); // Dev, Debug, or Release
    }
//------------------------------------------------------------------------------
    @Override
    protected void initInput() {
        Input input = getInput(); // get input service
        
        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                CtrPlayer.moveUp();
            }
        }, KeyCode.W);
        
        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                CtrPlayer.moveHorizontal(true);
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                CtrPlayer.moveHorizontal(false);
            }
        }, KeyCode.S);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                CtrPlayer.moveDown();
            }
        }, KeyCode.D);
    }
//------------------------------------------------------------------------------
    @Override
    protected void initAssets() {
    }
//------------------------------------------------------------------------------
    @Override
    protected void initGame() {
        player = EntityFactory.newPlayer(100 , 100);
        getGameWorld().addEntity(player);
        CtrPlayer = player.getControlUnsafe(PlayerControl.class);
    }
//------------------------------------------------------------------------------
    @Override
    protected void initPhysics() {
        
    }
//------------------------------------------------------------------------------
    @Override
    protected void initUI() {
        
    }
//------------------------------------------------------------------------------
    @Override
    protected void onUpdate(double d) {
        
    }
//------------------------------------------------------------------------------
    /**
     * Contains FXGL code to launch the window.
     * Nothing else will need to be added in here.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
//------------------------------------------------------------------------------
}
////////////////////////////////////////////////////////////////////////////////
