package gamestates;

import com.example.lumina.Game;
import ui.BackButton;
import ui.MainButton;
import ui.SettingButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.BackButtons.BACK_BUTTON_HEIGHT;
import static utilz.Constants.UI.BackButtons.BACK_BUTTON_WIDTH;

public class GameSettings extends State implements Statemethods {


    // Variables
    private SettingButton settings; // Variabilising settings instance
    private BufferedImage backgroundImg, settingsBackgroundImg; // Image for backdrop and settings (w/o buttons)
    private int bgX, bgY, bgW, bgH; // Position of backdrop image
    private BackButton backB; // Variabilising back button instance
    private boolean pausedBack = false; // Checks if the settings is accessed from the main menu or pause menu

    public GameSettings(Game game) {
        super(game);
        loadImgs();
        loadButton();
        settings = game.getSettings();
    }


    // Load back button
    private void loadButton() {
        // Position of back button in the settings
        int backX = (int) (420 * Game.SCALE);
        int backY = (int) (496 * Game.SCALE);

        backB = new BackButton(backX, backY, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT); // Create new back button
    }

    // Load image background in settings
    private void loadImgs() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        settingsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.SETTINGS);

        bgW = (int) (settingsBackgroundImg.getWidth() * Game.SCALE * 2f);
        bgH = (int) (settingsBackgroundImg.getHeight() * Game.SCALE * 2f);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (70 * Game.SCALE);
    }

    // Update the buttons to check for any user action/events
    @Override
    public void update() {
        backB.update();
        settings.update();
    }

    // Draw the buttons in the settings
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null); // Backdrop image
        g.drawImage(settingsBackgroundImg, bgX, bgY, bgW, bgH, null); // Image of the settings (w/o buttons)

        backB.draw(g); // Back button
        settings.draw(g); // All settings related buttons here

        game.setCustomFont(g, 10f * Game.SCALE); // Set font size to 10
        g.drawString(Game.GAME_WIDTH + "x" + Game.GAME_HEIGHT , (int) (624 * Game.SCALE), (int) (184 * Game.SCALE)); // Resolution of game shown
        g.drawString(String.valueOf(game.getFps_set()), (int) (654 * Game.SCALE), (int) (244 * Game.SCALE)); // Framerate of game shown
    }

    public void mouseDragged(MouseEvent e) {
        settings.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // Checks for mouse clicks on buttons in settings
    @Override
    public void mousePressed(MouseEvent e) {
        if(isIn(e, backB))
            backB.setMousePressed(true);
        else
            settings.mousePressed(e);
    }

    // Checks if mouse released on button
    @Override
    public void mouseReleased(MouseEvent e) {
        if(isIn(e, backB)) {
            if (backB.isMousePressed())
                if(pausedBack)
                {
                    Gamestate.state = Gamestate.PLAYING; // (If accessed through pause menu) Back button leads back to pause screen
                    game.getPlaying().setPaused(true);
                    pausedBack = false;
                }

                else
                {
                    Gamestate.state = Gamestate.MENU; // (If accessed through main menu) Back button pressed leads back to main menu
                }
        } else
            settings.mouseReleased(e);

        backB.resetBools();
    }

    // Checks if mouse is over button (Trigger hover animation)
    @Override
    public void mouseMoved(MouseEvent e) {
        backB.setMouseOver(false);

        if(isIn(e, backB)) {
            backB.setMouseOver(true);
            backB.setMousePressed(true);
        } else
            settings.mouseMoved(e);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) // Pressing escape key returns to main menu as well
            if(pausedBack) {
                Gamestate.state = Gamestate.PLAYING; // (If accessed through pause menu) Back button leads back to pause screen
                game.getPlaying().setPaused(true);
                pausedBack = false;
            } else
                Gamestate.state = Gamestate.MENU; // (If accessed through main menu) Back button pressed leads back to main menu
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void setBackPaused(boolean pausedBack)
    {
        this.pausedBack = pausedBack;
    }

    // Checks the bounds if mouse is over bounds to trigger certain events (e.g. mouse hover animation when mouse is over a bounding rectangle)
    private boolean isIn(MouseEvent e, MainButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
