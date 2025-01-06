
package ui;

import com.example.lumina.Game;
import gamestates.Playing;
import gamestates.Statemethods;
import utilz.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class InstructionWindow implements Statemethods {
    // Lists to store instruction areas and texts
    private List<Rectangle> sequentialInstructionAreas;
    private List<String[]> sequentialInstructionTexts;
    private List<Rectangle> triggeredInstructionAreas;
    private List<String[]> triggeredInstructionTexts;

    // Lists to store the original instruction areas and texts
    private List<Rectangle> originalSequentialInstructionAreas;
    private List<String[]> originalSequentialInstructionTexts;
    private List<Rectangle> originalTriggeredInstructionAreas;
    private List<String[]> originalTriggeredInstructionTexts;

    // Indices to keep track of current instruction
    private int currentSequentialIndex = 0;
    private int currentTriggeredIndex = -1; // -1 means no triggered instruction is active
    private int triggeredIndex = 0;

    // Flags and states for instructions
    private boolean showInstructions = false;
    private boolean instructionsShown = false;
    private InstructionState instructionState = InstructionState.NONE;
    private long animationStartTime;

    // Constants for animation durations and typewriter effect speed
    private static final int ANIMATION_DURATION = 1000; // 1 second for each animation
    private static final long TYPEWRITER_SPEED = 50;
    private static final long BLINK_INTERVAL = 500; // Blinking interval for "Press ENTER to continue"
    private Playing playing;

    // Flags for specific windows
    private static boolean isJumpSpawnWindow;
    private static boolean isAttackSpawnWindow;
    private static boolean isDashSpawnWindow;

    // Animation states
    public enum InstructionState {NONE, OPENING, DISPLAYING, CLOSING}

    // Constructor to initialize instruction areas and texts
    public InstructionWindow(List<Rectangle> sequentialInstructionAreas, List<String[]> sequentialInstructionTexts,
                             List<Rectangle> triggeredInstructionAreas, List<String[]> triggeredInstructionTexts) {
        this.sequentialInstructionAreas = new ArrayList<>(sequentialInstructionAreas);
        this.sequentialInstructionTexts = new ArrayList<>(sequentialInstructionTexts);
        this.triggeredInstructionAreas = new ArrayList<>(triggeredInstructionAreas);
        this.triggeredInstructionTexts = new ArrayList<>(triggeredInstructionTexts);

        // Store the original lists
        this.originalSequentialInstructionAreas = new ArrayList<>(sequentialInstructionAreas);
        this.originalSequentialInstructionTexts = new ArrayList<>(sequentialInstructionTexts);
        this.originalTriggeredInstructionAreas = new ArrayList<>(triggeredInstructionAreas);
        this.originalTriggeredInstructionTexts = new ArrayList<>(triggeredInstructionTexts);
    }

    // Method to update the instruction window state
    public void update() {
        if (instructionState != InstructionState.NONE) { // Check if there is any instruction state to process
            long elapsedTime = System.currentTimeMillis() - animationStartTime; // Calculate elapsed time since the animation started
            if (elapsedTime > ANIMATION_DURATION) { // Check if the animation duration has passed
                switch (instructionState) { // Handle different instruction states
                    case OPENING:
                        instructionState = InstructionState.DISPLAYING; // Switch to displaying state
                        animationStartTime = System.currentTimeMillis(); // Reset animation start time
                        break;
                    case DISPLAYING:
                        // Stay in displaying state until Enter is pressed
                        break;
                    case CLOSING:
                        if (currentTriggeredIndex != -1) { // Check if a triggered instruction is being displayed
                            // Remove the triggered instruction after closing
                            triggeredInstructionAreas.remove(currentTriggeredIndex);
                            triggeredInstructionTexts.remove(currentTriggeredIndex);
                            currentTriggeredIndex = -1; // Reset the triggered index
                            triggeredIndex += 1; // Increment the triggered index
                        } else if (currentSequentialIndex < sequentialInstructionAreas.size() - 1) { // Check if there are more sequential instructions
                            currentSequentialIndex++; // Move to the next sequential instruction
                            instructionState = InstructionState.OPENING; // Switch to opening state
                            animationStartTime = System.currentTimeMillis(); // Reset animation start time
                        } else { // If no more instructions to display
                            instructionState = InstructionState.NONE; // Set state to none
                            showInstructions = false; // Hide instructions
                            instructionsShown = true; // Mark instructions as shown
                        }
                        break;
                }
            }
        }
    }

    // Method to draw the instruction window
    @Override
    public void draw(Graphics g) {
        if (!showInstructions) return; // Do not draw if instructions are not to be shown

        Graphics2D g2d = (Graphics2D) g; // Cast Graphics to Graphics2D for advanced drawing features
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Enable anti-aliasing for smoother edges

        g.setColor(new Color(0, 0, 0, 150)); // Set color to semi-transparent black
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT); // Fill the entire game window with semi-transparent black

        String[] lines;
        if (currentTriggeredIndex != -1) { // If a triggered instruction is being displayed
            lines = triggeredInstructionTexts.get(currentTriggeredIndex); // Get the triggered instruction text
        } else { // If a sequential instruction is being displayed
            lines = sequentialInstructionTexts.get(currentSequentialIndex); // Get the sequential instruction text
        }

        // Calculate the size of the instruction window based on the text length
        FontMetrics metrics = g.getFontMetrics(new Font("Monospaced", Font.PLAIN, 14)); // Get font metrics for calculating text dimensions
        int windowWidth = 0;
        int windowHeight = 0;
        for (String line : lines) { // Loop through each line of instruction text
            int lineWidth = metrics.stringWidth(line); // Get the width of the line
            windowWidth = Math.max(windowWidth, lineWidth); // Update the window width to the maximum line width
            windowHeight += metrics.getHeight(); // Add the height of the line to the window height
        }
        windowWidth += 20; // Add padding to the window width
        windowHeight += 60; // Add padding and space for "Press ENTER to continue" to the window height

        int windowX = (Game.GAME_WIDTH - windowWidth) / 2; // Center the window horizontally
        int windowY = (Game.GAME_HEIGHT - windowHeight) / 4; // Position the window vertically (upper quarter)

        long elapsedTime = System.currentTimeMillis() - animationStartTime; // Calculate elapsed time since the animation started
        float progress = Math.min(1.0f, (float) elapsedTime / ANIMATION_DURATION); // Calculate the animation progress (0 to 1)

        switch (instructionState) { // Handle different instruction states
            case OPENING:
                windowHeight = (int) (windowHeight * progress); // Increase the window height based on progress
                if (progress >= 1.0f) { // Check if the animation is complete
                    instructionState = InstructionState.DISPLAYING; // Switch to displaying state
                    animationStartTime = System.currentTimeMillis(); // Reset animation start time for typewriter effect
                }
                break;
            case DISPLAYING:
                // No change in window size
                break;
            case CLOSING:
                windowHeight = (int) (windowHeight * (1 - progress)); // Decrease the window height based on progress
                break;
        }

        // Draw window background
        g.setColor(new Color(0, 0, 0, 255)); // Set color to opaque black
        g.fillRect(windowX, windowY, windowWidth, windowHeight); // Draw the instruction window background

        // Draw window border
        g.setColor(Color.WHITE); // Set color to white
        g.drawRect(windowX, windowY, windowWidth, windowHeight); // Draw the instruction window border

        if (instructionState == InstructionState.DISPLAYING) { // If the instruction state is displaying
            g.setColor(Color.WHITE); // Set color to white
            g.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Set font to Monospaced with plain style and size 14

            // Typewriter effect for main text
            long typewriterElapsed = System.currentTimeMillis() - animationStartTime; // Calculate elapsed time for typewriter effect
            int totalCharacters = (int) (typewriterElapsed / TYPEWRITER_SPEED); // Calculate the total number of characters to display

            int displayedCharacters = 0;
            for (int i = 0; i < lines.length; i++) { // Loop through each line of instruction text
                if (displayedCharacters < totalCharacters) { // Check if there are more characters to display
                    String line = lines[i];
                    int charactersToDisplay = Math.min(line.length(), totalCharacters - displayedCharacters); // Calculate the number of characters to display for the current line
                    g.drawString(line.substring(0, charactersToDisplay), windowX + 10, windowY + 20 + i * 20); // Draw the current line up to the calculated number of characters
                    displayedCharacters += charactersToDisplay; // Update the displayed characters count
                }
            }

            // Draw the smaller text at the bottom center with blinking effect
            g.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Set font to Monospaced with plain style and size 12
            String hintText = "Press ENTER to continue"; // Hint text to display
            int hintTextWidth = metrics.stringWidth(hintText); // Calculate the width of the hint text
            int hintTextX = windowX + (windowWidth - hintTextWidth) / 2 + 20; // Calculate the x position to center the hint text
            int hintTextY = windowY + windowHeight - 20; // Calculate the y position for the hint text

            // Blinking effect
            if ((System.currentTimeMillis() / BLINK_INTERVAL) % 2 == 0) { // Check if the hint text should be visible based on the blinking interval
                g.drawString(hintText, hintTextX, hintTextY); // Draw the hint text
            }
        }
    }

    // Empty implementations for mouse events
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    // Method to handle key press events
    @Override
    public void keyPressed(KeyEvent e) {
        if (showInstructions && e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (instructionState == InstructionState.DISPLAYING) {
                long typewriterElapsed = System.currentTimeMillis() - animationStartTime;
                int totalCharacters = (int) (typewriterElapsed / TYPEWRITER_SPEED);
                String[] lines = currentTriggeredIndex != -1 ? triggeredInstructionTexts.get(currentTriggeredIndex) : sequentialInstructionTexts.get(currentSequentialIndex);
                int totalTextLength = 0;
                for (String line : lines) {
                    totalTextLength += line.length();
                }
                if (totalCharacters < totalTextLength) {
                    // If not all text is displayed, display all text instantly
                    animationStartTime -= (totalTextLength - totalCharacters) * TYPEWRITER_SPEED;
                } else {
                    // If all text is displayed, proceed to closing
                    instructionState = InstructionState.CLOSING;
                    animationStartTime = System.currentTimeMillis();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


    public void checkInstructionArea(Rectangle2D.Float playerHitbox) {
        // Check if no instruction is being displayed
        if (instructionState == InstructionState.NONE) {
            // Check if player intersects with the current sequential instruction area
            if (currentSequentialIndex < sequentialInstructionAreas.size() &&
                    sequentialInstructionAreas.get(currentSequentialIndex).intersects(playerHitbox)) {
                showInstructions = true; // Set flag to show instructions
                instructionState = InstructionState.OPENING; // Set instruction state to opening
                animationStartTime = System.currentTimeMillis(); // Record the start time of the animation
            } else {
                // Check if player intersects with any triggered instruction area
                for (int i = 0; i < triggeredInstructionAreas.size(); i++) {
                    if (triggeredInstructionAreas.get(i).intersects(playerHitbox)) {
                        showInstructions = true; // Set flag to show instructions
                        instructionState = InstructionState.OPENING; // Set instruction state to opening
                        animationStartTime = System.currentTimeMillis(); // Record the start time of the animation
                        currentTriggeredIndex = i; // Set the current triggered instruction index
                        break; // Exit the loop once a triggered area is found
                    }
                }
            }
        }
    }

    // Method to check if instructions should be shown
    public boolean isShowInstructions() {
        return showInstructions; // Return the flag indicating if instructions should be shown
    }

    // Method to get the current instruction state
    public InstructionState getInstructionState() {
        return instructionState; // Return the current instruction state
    }

    // Method to get the current triggered index
    public int getTriggeredIndex() {
        return triggeredIndex; // Return the current triggered index
    }

    // Method to reset the instruction window state
    public void reset() {
        if (isJumpSpawnWindow) { // Check if the jump spawn window is active
            triggeredIndex = 3; // Set triggered index for jump spawn window
            this.currentSequentialIndex = 1; // Set current sequential index for jump spawn window
        } else if (isDashSpawnWindow) { // Check if the dash spawn window is active
            triggeredIndex = 4; // Set triggered index for dash spawn window
            this.currentSequentialIndex = 1; // Set current sequential index for dash spawn window
        } else if (isAttackSpawnWindow) { // Check if the attack spawn window is active
            triggeredIndex = 5; // Set triggered index for attack spawn window
            this.currentSequentialIndex = 1; // Set current sequential index for attack spawn window
        } else { // Default case
            triggeredIndex = 0; // Reset triggered index
            this.currentSequentialIndex = 0; // Reset current sequential index
        }
        showInstructions = false; // Reset flag to show instructions
        instructionsShown = false; // Reset flag indicating instructions have been shown
        instructionState = InstructionState.NONE; // Set instruction state to none

        // Restore the original lists of instruction areas and texts
        this.sequentialInstructionAreas = new ArrayList<>(originalSequentialInstructionAreas);
        this.sequentialInstructionTexts = new ArrayList<>(originalSequentialInstructionTexts);
        this.triggeredInstructionAreas = new ArrayList<>(originalTriggeredInstructionAreas);
        this.triggeredInstructionTexts = new ArrayList<>(originalTriggeredInstructionTexts);
    }

    // Static method to set the jump window index
    public static void setjumpWindowIndex(boolean jumpWindowIndex) {
        isJumpSpawnWindow = jumpWindowIndex;
    }

    // Static method to set the attack window index
    public static void setAttackWindowIndex(boolean attackWindowIndex) {
        isAttackSpawnWindow = attackWindowIndex;
    }

    // Static method to set the dash window index
    public static void setDashWindowIndex(boolean dashWindowIndex) {
        isDashSpawnWindow = dashWindowIndex;
    }

    // Method to check if a jump instruction is displayed
    public boolean isJumpInstructionDisplayed() {
        System.out.println("CurrentIndex is " + this.triggeredIndex);
        return this.triggeredIndex == 3 || this.triggeredIndex == 4 || this.triggeredIndex == 5;
    }

    // Method to check if an attack instruction is displayed
    public boolean isAttackInstructionDisplayed() {
        return this.triggeredIndex == 5;
    }

    // Method to check if a dash instruction is displayed
    public boolean isDashInstructionDisplayed() {
        return this.triggeredIndex == 4 || this.triggeredIndex == 5;
    }
}
