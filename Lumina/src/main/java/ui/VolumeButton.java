package ui;

import utilz.LoadSave;
import static utilz.Constants.UI.VolumeButtons.*;

import java.awt.*;
import java.awt.image.BufferedImage;





public class VolumeButton extends MainButton {

    // Variables
    private BufferedImage[] imgs; // Image array for storing sprite sheet images of volume handle (Different animations)
    private BufferedImage slider; // To store slider bar image
    private int index = 0; // Default animation/drawing of slider handle
    private boolean mouseOver, mousePressed; // Boolean to check different conditions for animation
    private int buttonX, minX, maxX; // Position for sliders (min and max values) + handle
    private float floatValue = 0f; // For updating volume in game

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y , VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2; // Size of bounding box
        buttonX = x + width / 2; // Size of handle
        this.x = x - 8;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2 - 8; // Position of 0% volume of bar
        maxX = x + width - VOLUME_WIDTH / 2 - 9; // Position of 100% volume of bar
        loadImgs();
    }

    // Load sprite sheet of volume slider
    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0,VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT); // Slider handle
        }

        slider = temp.getSubimage(3*VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT); // Slider bar image

    }


    // Update to check for user inputs/events/actions
    public void update() {
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
    }

    // Draw the slider bar + handle
    public void draw(Graphics g) {
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);

    }

    // Update and change position of slider handle
    public void changeX(int x) {
        if(x < minX)
            buttonX = minX; // Do not let it exceed the left
        else if (x > maxX)
            buttonX = maxX; // Do not let it exceed the right
        else
            buttonX = x; // Wherever location it is at

        updateFloatValue();
        bounds.x = buttonX - VOLUME_WIDTH / 2; // Make bounds at the middle of the slider (Horizontal plane)
    }

    // Update volume based on position of slider handle
    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
        floatValue = value / range;
    }

    // Reset mosue event related animations (e.g. hover animation)
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
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

    public float getFloatValue() {
        return floatValue;
    }
}
