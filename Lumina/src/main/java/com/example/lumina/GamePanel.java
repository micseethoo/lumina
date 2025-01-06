package com.example.lumina;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import utilz.LoadSave;

import javax.swing.*;
import java.awt.*;

//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
import static com.example.lumina.Game.GAME_WIDTH;
import static com.example.lumina.Game.GAME_HEIGHT;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game) {

        mouseInputs = new MouseInputs(this); // Initialises all mouse inputs in the game
        this.game = game;

        setPanelSize(); // Set size of window
        addKeyListener(new KeyboardInputs(this)); // Responsible for reading all keyboard actions made by player by initialising a key listener
        addMouseListener(mouseInputs); // Checks for any mouse button clicks
        addMouseMotionListener(mouseInputs); // Checks for mouse movement
    }

    // Creates a new window based on the scale and size of the game
    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("Size:" + GAME_WIDTH + "x" + GAME_HEIGHT);
    }

    // Command responsible for drawing the assets and everything in game (master draw)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame() {
        return game;
    }

}
