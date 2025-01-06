package ui;

import com.example.lumina.Game;
import gamestates.Gamestate;
import gamestates.Playing;
import save.SaveCrystalProgress;
import save.SaveLevelProgress;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.UIButtons.*;

public class LevelCompletedOverlay {

    // Variables
    private Playing playing; // Variabilising the playing instance to get some methods there
    private UIButton menu, next; // Initialise UI buttons for returning to main menu or goign to next stage
    private BufferedImage img; // Store image instance for level completed background
    private int bgX, bgY, bgW, bgH; // Position and dimension of level completed background

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButtons();
    }

    // Initialise buttons to go to next stage or back to main menu
    private void initButtons() {
        int menuX = (int) (440 * Game.SCALE);
        int nextX = (int) (612 * Game.SCALE);
        int bY = (int) (300 * Game.SCALE);

        next = new UIButton(nextX, bY, UIBUTTON_SIZE, UIBUTTON_SIZE, 0);
        menu = new UIButton(menuX, bY, UIBUTTON_SIZE, UIBUTTON_SIZE, 2);
    }

    // Initialise image of level completed borders
    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_COMPLETED_IMG);
        bgW = (int) (img.getWidth() * Game.SCALE * 1.5f);
        bgH = (int) (img.getHeight() * Game.SCALE * 1.5f);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (175 * Game.SCALE);
    }


    // Draw the images mentioned above
    public void draw(Graphics g) {
        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);
    }

    // Update the button to check for user actions or events
    public void update() {
        next.update();
        menu.update();
    }

    // Checks if mouse cursor is on the buttons
    private boolean isIn(UIButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }


    // Checks if mouse is over the buttons (trigger hover animation)
    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(next, e))
            next.setMouseOver(true);
    }

    // Checks if mouse is released after pressing a button
    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                if (playing.getLevelManager().getLevelIndex() > SaveLevelProgress.loadLevelProgress()) {
                    SaveCrystalProgress.saveCrystalProgress(playing.getPlayer().getNumCrystalFragments());
                }
                playing.loadNextLevel();
                playing.getLevelManager().saveLevel(); // Save the current level
                playing.getGame().getAudioPlayer().stopSong(); // Stop current song

                playing.setGameState(Gamestate.MENU);
            }
        } else if (isIn(next, e))
            if(next.isMousePressed()) {
                playing.loadNextLevel();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
            }

        menu.resetBools();
        next.resetBools();
    }


    // Checks if mouse has pressed on a button
    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(next, e))
            next.setMousePressed(true);
    }

}
