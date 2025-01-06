package com.example.lumina;


import audio.AudioPlayer;
import gamestates.GameSettings;
import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.Menu;
import ui.SettingButton;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Game implements Runnable{

    // Frames per second (FPS) and Updates per second (UPS) of the game
    private final int fps_set = 120;
    private final int ups_set = 200;


    // Instance Variables
    private GameWindow gameWindow;
    private GamePanel gamePanel;

    private Playing playing;
    private Menu menu;
    private GameSettings gSettings;
    private SettingButton settings;
    private AudioPlayer audioPlayer;

    public final static int TILES_DEFAULT_SIZE = 36; // The default size of each tile before scaling
    public static float SCALE = 2.2226666666666666666666666666667f; // Change this when you want to make everything in the game smaller or bigger (SCALE of 1.6666667 makes the game run at 1920x1080)
    public final static int TILES_IN_WIDTH = 32; // How many tiles wide the screen is able to show at a time
    public final static int TILES_IN_HEIGHT = 18;// How many tiles tall the screen is able to show at a time
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE); // The actual size of each tile in the game
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH; // The actual width of the game (in pixels) after scaling
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT; // The actual height of the game (in pixels) after scaling

    // Font instance for setting the custom font in the game
    private Font customFont;

    public Game() {
        initClasses(); // Initialising all classes needed in the game

        gamePanel = new GamePanel(this); // Creates a new game panel instance in the game (for setting up the methods for drawing up all assets in the game as well as setting up the default size of the game window; also responsible for receiving inputs from the mouse and keyboard of the user)
        gameWindow = new GameWindow(gamePanel); // Creates a new game window and the properties of the window (e.g. if it is resizable
        gamePanel.setFocusable(true); // Ensures that the game is focused first on launch
        gamePanel.requestFocus(); // Ensures that the game requires users to focus on the window to play/perform actions

        startGameLoop(); // Initialises the game loop of the game
    }

    // Initialising all necessary classes in game
    private void initClasses() {
        settings = new SettingButton(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        gSettings = new GameSettings(this);
    }

    private void startGameLoop() {
        Thread gameThread = new Thread(this); // Creates a new thread outside of the main runnable
        gameThread.start(); // Initialises the newly defined thread
    }

    // Updates all actions or events in game (and in menus')
    private void update() {
        switch(Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
                gSettings.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    // Draws all of the assets in the game, including sprites and menu UI
    public void render(Graphics g) {
        switch(Gamestate.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case OPTIONS:
                gSettings.draw(g);
                break;
            default:
                break;

        }
    }

    // Set custom font and getting it from folder
    public void setCustomFont(Graphics g, float fontSize) {
        try {
            // Load the pixel font from the resource folder
            InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, is);
                customFont = customFont.deriveFont(fontSize);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            } else {
                System.err.println("Font file not found.");
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        if (customFont != null) {
            g.setFont(customFont);
        }
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / fps_set; // Defines the number of nanoseconds per tick
        double timePerUpdate = 1000000000.0 / ups_set; // Defines the number of nanoseconds per tick
        long previousTime = System.nanoTime();

        // For checking purposes
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        // For determining the time difference between each tick for both updates and rendering
        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime(); // Important for determining the time difference between each tick

            // Calculating the time difference per tick
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime; // Resets the time difference and starting from the next tick in the loop

            if (deltaU > 1.0) { // Update the game every tick (tick = 1 second over UPS)
                update(); // Update the game once
                updates++; // For checking purposes (Adds 1 count to the updates counter)
                deltaU--; // Reset the time delta
            }

            if (deltaF > 1.0) { // Render the game every tick (tick = 1 second over FPS)
                gamePanel.repaint(); // Repaint the game once
                frames++;  // For checking purposes (Adds 1 count to the frames counter)
                deltaF--; // Reset the time delta
            }

            // For checking purposes (Prints out the UPS and FPS of the game)
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }

    }

    // Checks if user lost focus from window (e.g. alt + tab)
    public void WindowFocusLost() {
        if(Gamestate.state == Gamestate.PLAYING && !playing.isGameOver()) {
            playing.getPlayer().resetDirBooleans();
            playing.setPaused(true); // Pauses game when window is clicked out
            }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public GameSettings getGameSettings() {
        return gSettings;
    }

    public SettingButton getSettings() {
        return settings;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public int getFps_set() {
        return fps_set;
    }
}
