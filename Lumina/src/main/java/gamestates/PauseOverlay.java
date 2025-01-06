package gamestates;


import com.example.lumina.Game;
import save.SaveCrystalProgress;
import save.SaveLevelProgress;
import ui.*;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static audio.AudioPlayer.MENU_1;

public class PauseOverlay extends State {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private PauseOverlayButton[] buttons = new PauseOverlayButton[4];


    public PauseOverlay(Playing playing) {
        super(playing.getGame());
        this.playing = playing;
        loadButtons();
        loadBackground();
    }



    // Loads the pause menu image
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND); // Get the pause menu image
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE * 2.5f); // Size of pause menu (Horizontal)
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE * 2.5f); // Size of pause menu (Vertical)

        // Position of pause menu image to place
        bgX = Game.GAME_WIDTH / 2 - bgW / 2; // In the middle
        bgY = (int) (100 * Game.SCALE); // A few pixels down
    }

    // Loads new buttons
    private void loadButtons() {
        buttons[0] = new PauseOverlayButton((int) (550 * Game.SCALE), (int) (220 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new PauseOverlayButton((int) (550 * Game.SCALE), (int) (270 * Game.SCALE), 1, Gamestate.PLAYING);
        buttons[2] = new PauseOverlayButton((int) (550 * Game.SCALE), (int) (320 * Game.SCALE), 2, Gamestate.OPTIONS);
        buttons[3] = new PauseOverlayButton((int) (550 * Game.SCALE), (int) (370* Game.SCALE), 3, Gamestate.MENU);
    }


    // Updates the buttons to check for user actions/events
    public void update() {
        // URM Buttons
//        menuB.update();
//        restartB.update();
//        unpauseB.update();
//
//        settings.update();
        for(PauseOverlayButton mb : buttons)
            mb.update();
    }

    // Draws the images for the buttons + pause menu image
    public void draw(Graphics g) {
        //Pause Menu Background
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        // Buttons for pause overlay
        for(PauseOverlayButton mb : buttons)
            mb.draw(g);
    }

    // Reset button states (e.g. no longer hovered)
    private void resetButtons() {
        for(PauseOverlayButton mb : buttons) {
            mb.resetBools();
        }
    }


    // Checks if button is pressed
    public void mousePressed(MouseEvent e) {
        for(PauseOverlayButton mb : buttons) {
            if(isIn2(e, mb)) {
                mb.setMousePressed(true);

            }
        }
    }


    // Checks if button being first pressed is released
    public void mouseReleased(MouseEvent e) {
        for(PauseOverlayButton mb : buttons) {
            if (isIn2(e, mb)) { // Check if the mouse is over a button
                if (mb.isMousePressed())
                    mb.applyGameState(); // Apply the button's game state

                if (mb.getState() == Gamestate.PLAYING) {
                    if (mb == buttons[0]) {
                        playing.unpauseGame(); // Unpause the game
                    } else if (mb == buttons[1]) {
                        playing.resetAll(); // Reset the game
                    }
                } else if (mb.getState() == Gamestate.OPTIONS) {
                    if (mb == buttons[2]) {
                        game.getGameSettings().setBackPaused(true); // Open the options menu
                    }
                } else if (mb.getState() == Gamestate.MENU) {
                    if (mb == buttons[3]) {
                        int levelIndex = game.getPlaying().getLevelManager().getLevelIndex();
                        int savedLevelIndex = SaveLevelProgress.loadLevelProgress();

                        if (levelIndex > savedLevelIndex)
                            SaveCrystalProgress.saveCrystalProgress(game.getPlaying().getPlayer().getNumCrystalFragments());

                        playing.getLevelManager().saveLevel(); // Save the current level
                        playing.getGame().getAudioPlayer().stopSong(); // Stop current song
                        playing.getGame().getAudioPlayer().playSong(MENU_1); // Play menu song
                    }
                }

                break;
            }
        }
        resetButtons();
    }

    // Checks if mouse is hovering on button
    public void mouseMoved(MouseEvent e) {
        for(PauseOverlayButton mb : buttons)
            mb.setMouseOver(false);

        for(PauseOverlayButton mb : buttons)
            if(isIn2(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
    }
}
