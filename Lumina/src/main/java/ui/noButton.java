
package ui; // Defines the package this class belongs to.

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.BackButtons.*;


public class noButton { // Declares the noButton class.
    private BufferedImage[] noImages; // An array to hold images for the button states.
    private boolean mouseOver, mousePressed; // Booleans to track if the mouse is over or pressing the button.
    private int colIndex; // Index to track the current image to display.
    protected int x, y, width, height; // Coordinates and dimensions of the button.
    protected Rectangle bounds; // Rectangle to represent the button's bounds.

    public noButton(int x, int y, int width, int height) { // Constructor for the noButton class.
        this.x = x; // Sets the x-coordinate.
        this.y = y; // Sets the y-coordinate.
        this.width = width; // Sets the button's width.
        this.height = height; // Sets the button's height.
        createBounds(); // Calls the method to create the button's bounds.
        loadConfirmImages(); // Calls the method to load the button's images.
    }

    private void loadConfirmImages() { // Method to load the button images.
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.NO_BUTTON); // Loads the sprite atlas for the button.
        noImages = new BufferedImage[2]; // Initializes the image array with a size of 2.
        for (int i = 0; i < noImages.length; i++) // Loops through the array to load each image.
            noImages[i] = temp.getSubimage(i * NO_BUTTON_DEFAULT_WIDTH, 0, NO_BUTTON_DEFAULT_WIDTH, NO_BUTTON_DEFAULT_HEIGHT); // Extracts the subimage for each button state.
    }

    public void update() { // Method to update the button's state.
        colIndex = 0; // Resets the image index to 0.
        if(mouseOver) // Checks if the mouse is over the button.
            colIndex = 1; // Sets the image index to 1 if the mouse is over the button.
    }

    public void resetBools() { // Method to reset the mouse state booleans.
        mouseOver = false;
        mousePressed = false;
    }

    public void draw(Graphics g) { // Method to draw the button.
        g.drawImage(noImages[colIndex], x, y, NO_BUTTON_WIDTH, NO_BUTTON_HEIGHT, null); // Draws the button image at the specified coordinates.
    }

    public void setMouseOver(boolean mouseOver) { // Setter for mouseOver.
        this.mouseOver = mouseOver; // Sets the mouseOver value.
    }

    public boolean isMousePressed() { // Getter for mousePressed.
        return mousePressed; // Returns the mousePressed value.
    }

    public void setMousePressed(boolean mousePressed) { // Setter for mousePressed.
        this.mousePressed = mousePressed; // Sets the mousePressed value.
    }

    private void createBounds() { // Method to create the button's bounds.
        bounds = new Rectangle(x, y, width, height); // Initializes the bounds with the button's coordinates and dimensions.
    }

    public int getX() { // Getter for the x-coordinate.
        return x;
    }

    public void setX(int x) { // Setter for the x-coordinate.
        this.x = x;
    }

    public int getY() { // Getter for the y-coordinate.
        return y;
    }

    public void setY(int y) { // Setter for the y-coordinate.
        this.y = y;
    }

    public int getWidth() { // Getter for the width.
        return width;
    }

    public void setWidth(int width) { // Setter for the width.
        this.width = width;
    }

    public int getHeight() { // Getter for the height.
        return height;
    }

    public void setHeight(int height) { // Setter for the height.
        this.height = height;
    }

    public Rectangle getBounds() { // Getter for the bounds.
        return bounds;
    }
}
