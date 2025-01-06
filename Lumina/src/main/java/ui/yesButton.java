package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.BackButtons.*;

public class yesButton {
    private BufferedImage[] yesImages; // Array to hold button images
    private boolean mouseOver, mousePressed; // Booleans to track mouse state
    private int colIndex; // Index to track which image to show
    protected int x, y, width, height; // Position and size of the button
    protected Rectangle bounds; // Rectangle representing the button bounds

    // Constructor to initialize button properties
    public yesButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds(); // Create the bounds of the button
        loadConfirmImages(); // Load the button images
    }

    // Method to load the button images
    private void loadConfirmImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.YES_BUTTON); // Load the sprite atlas
        yesImages = new BufferedImage[2]; // Initialize the image array
        for (int i = 0; i < yesImages.length; i++) // Loop through the images
            yesImages[i] = temp.getSubimage(i * YES_BUTTON_DEFAULT_WIDTH, 0, YES_BUTTON_DEFAULT_WIDTH, YES_BUTTON_DEFAULT_HEIGHT); // Extract subimage
    }

    // Method to update the button state
    public void update() {
        colIndex = 0; // Default image index
        if (mouseOver) // If mouse is over the button
            colIndex = 1; // Change to hover image index
    }

    // Method to reset mouse states
    public void resetBools() {
        mouseOver = false; // Reset mouse over state
        mousePressed = false; // Reset mouse pressed state
    }

    // Method to draw the button
    public void draw(Graphics g) {
        g.drawImage(yesImages[colIndex], x, y, YES_BUTTON_WIDTH, YES_BUTTON_HEIGHT, null); // Draw the button image
    }

    // Method to set mouse over state
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver; // Set mouse over state
    }

    // Method to check if mouse is pressed
    public boolean isMousePressed() {
        System.out.println("THE YES BUTTON IS PRESSED"); // Print debug message
        return mousePressed; // Return mouse pressed state
    }

    // Method to set mouse pressed state
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed; // Set mouse pressed state
    }

    // Method to create the button bounds
    private void createBounds() {
        bounds = new Rectangle(x, y, width, height); // Initialize the bounds rectangle
    }

    // Getters and setters for button properties
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rectangle getBounds() {
        return bounds; // Return the button bounds
    }
}