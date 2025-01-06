package ui;

import gamestates.Gamestate;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

import static utilz.Constants.UI.Buttons.*;

public class PauseOverlayButton {
    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = P_WIDTH / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    public PauseOverlayButton(int xPos, int yPos, int rowIndex , Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, P_WIDTH, P_HEIGHT);
    }

    private void loadImgs() {
        imgs = new BufferedImage[2];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BUTTON);

        // Check the dimensions of the source image
        int sourceWidth = temp.getWidth();
        int sourceHeight = temp.getHeight();

        System.out.println("Source Image Dimensions: " + sourceWidth + "x" + sourceHeight);

        // Ensure the coordinates and dimensions are within bounds
        for (int i = 0; i < imgs.length; i++) {
            int x = i * P_WIDTH_DEFAULT;
            double y = rowIndex * P_HEIGHT_DEFAULT;

            System.out.println("Extracting subimage at: (" + x + ", " + y + ") with dimensions: " + P_WIDTH_DEFAULT + "x" + P_HEIGHT_DEFAULT);

            if (x + P_WIDTH_DEFAULT <= sourceWidth && y + P_HEIGHT_DEFAULT <= sourceHeight) {
                imgs[i] = temp.getSubimage(x, (int)y, P_WIDTH_DEFAULT, (int)P_HEIGHT_DEFAULT);
            } else {
                throw new RasterFormatException("Subimage coordinates are out of bounds: (" + x + ", " + y + ")");
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, P_WIDTH, P_HEIGHT, null);
    }

    public void update() {
        index = 0;
        if (mouseOver)
        {
            index = 1;
        }

        if (mousePressed)
        {
            index = 0;
        }

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


    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public Gamestate getState() {
        return state;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void applyGameState() {
        Gamestate.state = state;
    }



}

