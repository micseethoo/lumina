package ui;

import com.example.lumina.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static utilz.Constants.UI.BackButtons.BACK_BUTTON_HEIGHT;
import static utilz.Constants.UI.BackButtons.BACK_BUTTON_WIDTH;
import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class SettingButton {

    // Variables
    private VolumeButton musicVolumeButton, sfxVolumeButton; // Sliders for sound effects and music
    private SoundButton musicButton, sfxButton; // Mute buttons for sound effects and music
    private BackButton backButton;
    private Game game; // Variabilising the game instance for accessing certain methods or passing values to the game instance

    public SettingButton(Game game) {
        this.game = game;
        createSoundButtons();
        createVolumeButtons();
    }

    // Initialise and create the sound buttons (volume slider)
    private void createVolumeButtons() {
        int vX = (int) (420 * Game.SCALE);
        int musicY = (int) (165 * Game.SCALE);
        int sfxY = (int) (218 * Game.SCALE);
        musicVolumeButton = new VolumeButton(vX, musicY, SLIDER_WIDTH, VOLUME_HEIGHT);
        sfxVolumeButton = new VolumeButton(vX, sfxY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    // Initialise and create the sound buttons (mute)
    private void createSoundButtons() {
        int soundX = (int) (550 * Game.SCALE);
        int musicY = (int) (167 * Game.SCALE);
        int sfxY = (int) (220 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    // TO BE UPDATED
    private void createBackButton() {
        int backX = (int) (550 * Game.SCALE);
        int backY = (int) (900 * Game.SCALE);
        backButton = new BackButton(backX, backY, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
    }

    // Update button based on user action/event
    public void update() {
        musicButton.update();
        sfxButton.update();
        musicVolumeButton.update();
        sfxVolumeButton.update();
    }

    // Draw buttons
    public void draw(Graphics g) {
        musicButton.draw(g);
        sfxButton.draw(g);
        musicVolumeButton.draw(g);
        sfxVolumeButton.draw(g);
    }

    // Checks if volume sliders are being dragged
    public void mouseDragged(MouseEvent e) {
        if (musicVolumeButton.isMousePressed()) {
            float valueBefore = musicVolumeButton.getFloatValue();
            musicVolumeButton.changeX(e.getX());
            float valueAfter = musicVolumeButton.getFloatValue();
            if (valueBefore != valueAfter)
                game.getAudioPlayer().setVolumeMusic(valueAfter);
        } else if (sfxVolumeButton.isMousePressed()) {
            float valueBefore = sfxVolumeButton.getFloatValue();
            sfxVolumeButton.changeX(e.getX());
            float valueAfter = sfxVolumeButton.getFloatValue();
            if (valueBefore != valueAfter)
                game.getAudioPlayer().setVolumeSFX(valueAfter);
        }
    }

    // Checks if buttons are being pressed
    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(e, musicVolumeButton))
            musicVolumeButton.setMousePressed(true);
        else if (isIn(e, sfxVolumeButton))
            sfxVolumeButton.setMousePressed(true);
    }

    // Checks if pressed button is released
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) { // Music mute button
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
                game.getAudioPlayer().toggleSongMute();
            }
        } else if (isIn(e, sfxButton)) { // SFX mute button
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                game.getAudioPlayer().toggleEffectMute();
            }
        }

        // Reset state of buttons after released
        musicButton.resetBools();
        sfxButton.resetBools();
        musicVolumeButton.resetBools();
        sfxVolumeButton.resetBools();
    }

    // Checks if mouse is over button (Hover animation)
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        musicVolumeButton.setMouseOver(false);
        sfxVolumeButton.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, musicVolumeButton))
            musicVolumeButton.setMouseOver(true);
        else if (isIn(e, sfxVolumeButton))
            sfxVolumeButton.setMouseOver(true);
    }


    // Checks if mouse cursor is within the bounding box set for each button
    private boolean isIn(MouseEvent e, MainButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}

