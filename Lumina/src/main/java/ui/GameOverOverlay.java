package ui;

import com.example.lumina.Game;
import gamestates.Gamestate;
import gamestates.Playing;
import save.SaveCrystalProgress;
import save.SaveLevelProgress;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static audio.AudioPlayer.GAMEOVER;
import static audio.AudioPlayer.MENU_1;
import static utilz.Constants.UI.UIButtons.UIBUTTON_SIZE;

public class GameOverOverlay {

    // Variables
    private Playing playing; // Variabilising the playing instance to access methods or return values to that instance
    private BufferedImage img; // For storing image of death screen overlay
    private int imgX, imgY, imgW, imgH; // Position and dimensions of death screen overlay
    private UIButton menu, restart; // UI button instances for both returning to main menu and to restart level

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        createImg();
        createButtons();
    }


    // Initialise buttons
    private void createButtons() {
        // Position of button
        int restartX = (int) (400 * Game.SCALE);
        int menuX = (int) (654 * Game.SCALE);
        int bY = (int) (340 * Game.SCALE);

        restart = new UIButton(restartX, bY, UIBUTTON_SIZE, UIBUTTON_SIZE, 1); // Restart button
        menu = new UIButton(menuX, bY, UIBUTTON_SIZE, UIBUTTON_SIZE, 2); // Menu button
    }

    // Initialise images to be used for the buttons + overlay background
    private void createImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN); // Image overlay for game over screen
        imgW = (int) (img.getWidth() * Game.SCALE * 2f); // Size of overlay (Horizontal)
        imgH = (int) (img.getHeight() * Game.SCALE * 2f); // Size of overlay (Vertical)
        imgX = Game.GAME_WIDTH / 2 - imgW / 2; // Set to middle of screen (Horizontal)
        imgY = (int) (100 * Game.SCALE * 1.5); // Set position below from the top
    }

    // Draw the overlay + buttons
    public void draw(Graphics g) {
        g.setColor(new Color(0,0,0, 200));
        g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);

        restart.draw(g);
        menu.draw(g);
    }

    // Update and to capture mouse events of the user
    public void update() {
        menu.update();
        restart.update();
    }

    // Checks if key presses are done by the player
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { // ESCAPE key returns the player back to main menu (alternative method of going back to menu)
            playing.resetAll();
            playing.getGame().getAudioPlayer().stopEffect(GAMEOVER);
            playing.getGame().getAudioPlayer().playSong(MENU_1);
            Gamestate.state = Gamestate.MENU;
        }
    }

    // Checks if mouse cursor is over bounding box
    private boolean isIn(UIButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    // Checks if mouse is over button (For hover animation)
    public void mouseMoved(MouseEvent e) {
        restart.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(restart, e))
            restart.setMouseOver(true);
    }

    // Checks button previously pressed is released
    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();

                System.out.println("im here 3" + playing.getLevelManager().getLevelIndex() + SaveLevelProgress.loadLevelProgress());
                if (playing.getLevelManager().getLevelIndex() > SaveLevelProgress.loadLevelProgress()) {
                    System.out.println("Im here" + playing.getPlayer().getNumCrystalFragments());
                    SaveCrystalProgress.saveCrystalProgress(playing.getPlayer().getNumCrystalFragments());
                }
                playing.getLevelManager().saveLevel(); // Save the current level
                playing.getGame().getAudioPlayer().stopSong(); // Stop current song

                playing.setGameState(Gamestate.MENU);
            }
        } else if (isIn(restart, e))
            if(restart.isMousePressed()) {
                playing.resetAll();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
            }

        menu.resetBools();
        restart.resetBools();
        playing.getGame().getAudioPlayer().stopEffect(GAMEOVER);
    }

    // Checks for button pressing actions/events
    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(restart, e))
            restart.setMousePressed(true);
    }

}
