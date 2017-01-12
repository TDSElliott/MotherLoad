package io.github.tdselliott.ml;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.settings.GameSettings;
import static javafx.application.Application.launch;

/**
 *
 * @author Tyler Elliott
 * @author Mackenzie Guy
 * @author Logan L
 * @author Victor Russan
 */
public class MotherLoadApp extends GameApplication {

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
        System.out.println("Testing the print while Mack edits other code.");
    }
//------------------------------------------------------------------------------
    @Override
    protected void initAssets() {
        System.out.println("001");
        System.out.print("tstestsgfas");
    }
//------------------------------------------------------------------------------
    @Override
    protected void initGame() {
        
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
