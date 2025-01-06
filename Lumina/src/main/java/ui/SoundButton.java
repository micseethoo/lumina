package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.PauseButtons.*;

public class SoundButton extends MainButton {

    private BufferedImage[][] soundImages; // Stores the buttons in a 2D Array for each different button and animation states for each button
    private boolean mouseOver, mousePressed; // Boolean to check if button is hovered on or pressed (For animation purposes and to do some action (e.g. mute game)
    private boolean muted; // Boolean to mute sounds in game
    private int rowIndex, colIndex; // For button drawing purposes

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImages();
    }

    // Loads the sprite sheet for the sound buttons
    private void loadSoundImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImages = new BufferedImage[2][3];
        for(int j = 0; j < soundImages.length; j++)
            for (int i = 0; i < soundImages[j].length; i++)
                soundImages[j][i] = temp.getSubimage(i*SOUND_SIZE_DEFAULT, j*SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);

    }

    // Update the state the button is in (For animation purposes as well) e.g. muted has different image
    public void update() {
        if (muted)
            rowIndex = 1;
        else
            rowIndex = 0;


        colIndex = 0;
        if(mouseOver)
            colIndex = 1;
        if(mousePressed)
            colIndex = 2;

    }

    // Resets all animations related to mouse actions
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    // Draw the buttons
    public void draw(Graphics g) {
        g.drawImage(soundImages[rowIndex][colIndex], x, y, width, height, null);
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
