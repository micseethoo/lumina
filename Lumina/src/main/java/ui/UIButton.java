package ui;

import java.awt.*;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.UIButtons.*;

public class UIButton extends MainButton {
    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    public UIButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    // Load the button images from a sprite sheet
    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * UIBUTTON_SIZE_DEFAULT, rowIndex * UIBUTTON_SIZE_DEFAULT, UIBUTTON_SIZE_DEFAULT, UIBUTTON_SIZE_DEFAULT);

    }

    // Update the animation/image of the button based on mouse action
    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;

    }


    // Draw the buttons
    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, UIBUTTON_SIZE, UIBUTTON_SIZE, null);
    }


    // Resets the animations of the button (only for mouse events related animations, e.g. hover animation)
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
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
}
