package ui;

import gamestates.Gamestate;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.Buttons.*;

public class MenuButton {

    // Variables
    private int xPos, yPos, rowIndex, index; // Position of buttons and its animation indices
    private int xOffsetCenter = B_WIDTH / 2; // The offset of the bounds so that the box is in the middle instead of being in the left
    private Gamestate state; // Variabilising the gamestate instance for accessing or returning some values or methods from the gamestate class
    private BufferedImage[] imgs; // Store image array for the buttons (+ animations)
    private boolean mouseOver, mousePressed; // Variable for checking whether mouse is over a button or pressing on a button
    private Rectangle bounds; // Bounds for checking the mouse over a button or any other action

    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;

        loadImgs();
        initBounds();
    }

    // Initialise bounds for each of the buttons
    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    // Load images of the main menu buttons
    private void loadImgs() {
        imgs = new BufferedImage[2];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    // Draw the main menu buttons
    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    // Update the main menu buttons to check for any user mouse event
    public void update() {
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 0;
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

    public void applyGameState() {
        Gamestate.state = state;
    }

    // Reset all button states
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
}
