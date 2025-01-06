package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.BackButtons.*;

public class BackButton extends MainButton {
    private BufferedImage[] backImages;
    private boolean mouseOver, mousePressed;
    private int colIndex;

    public BackButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadBackImages();
    }

    private void loadBackImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BACK_BUTTONS);
        backImages = new BufferedImage[2];
        for (int i = 0; i < backImages.length; i++)
            backImages[i] = temp.getSubimage(i * BACK_BUTTON_DEFAULT_WIDTH, 0, BACK_BUTTON_DEFAULT_WIDTH, BACK_BUTTON_DEFAULT_HEIGHT);
    }

    public void update() {
        colIndex = 0;
        if(mouseOver)
            colIndex = 1;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public void draw(Graphics g) {
        g.drawImage(backImages[colIndex], x, y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT, null);
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
