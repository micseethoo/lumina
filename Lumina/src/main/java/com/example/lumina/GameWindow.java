//package com.example.lumina;
//
//import utilz.LoadSave;
//
//import javax.swing.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowFocusListener;
//
//public class GameWindow {
//    private JFrame jframe;
//    public GameWindow(GamePanel gamePanel) {
//
//        jframe = new JFrame();
//
//        jframe.setIconImage(LoadSave.GetSpriteAtlas(LoadSave.ICON));
//        jframe.setTitle("Lumina: The Shattered Light");
//
//        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jframe.add(gamePanel);
//        jframe.setResizable(true);
//        jframe.pack();
//        jframe.setLocationRelativeTo(null);
//        jframe.setVisible(true);
//        jframe.addWindowFocusListener(new WindowFocusListener() {
//            @Override
//            public void windowGainedFocus(WindowEvent e) {
////                gamePanel.getGame().WindowFocusLost();
//            }
//
//            @Override
//            public void windowLostFocus(WindowEvent e) {
//                gamePanel.getGame().WindowFocusLost();
//            }
//        });
//    }
//}

package com.example.lumina;

import utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {
    // Variables
    private JFrame jframe;
    private static boolean isFullScreen = false;
    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    public GameWindow(GamePanel gamePanel) {
        jframe = new JFrame();

        jframe.setIconImage(LoadSave.GetSpriteAtlas(LoadSave.ICON)); // Initialising icon of game
        jframe.setTitle("Lumina: The Shattered Light"); // Setting title of game

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Makes it so that the application closes when the exit button is clicked on the top right
        jframe.add(gamePanel); // Adds the actual gamePanel window instnace
        jframe.setResizable(true); // Allow the user to resize the already initialised window
        jframe.setSize(Game.GAME_WIDTH, Game.GAME_HEIGHT + 36); // Set the size of the window to the default game size (36 is added to account for the title bar)
        jframe.setLocationRelativeTo(null); // Sets the window in the middle of the user's screen
        jframe.setUndecorated(false);  // Make the frame undecorated

        setFullScreen(false);  // Start in full-screen mode

        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // gamePanel.getGame().WindowFocusLost();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().WindowFocusLost();
            }
        });
    }

    // Allow users to toggle between full and windowed mode
    public void ToggleFullScreen() {
        setFullScreen(!isFullScreen); // Check method below
    }

    // Method for setting the game between windowed mode and full screen
    private void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            jframe.dispose(); // Delete the initial window
            jframe.setUndecorated(true); // Removes the top bar elements (icon, title of game, minimize and close options)
            gd.setFullScreenWindow(jframe); // Sets the game to full screen
        } else {
            gd.setFullScreenWindow(null); // unsets the jframe window to full screen
            jframe.dispose(); // Removes the instance of jframe in fullscreen mode
            jframe.setUndecorated(false); // Creates a window with the top bar elements
            jframe.setVisible(true); // Sets the window to be visible to the user
        }
        isFullScreen = fullScreen; // Boolean change that determines whether window has been set to full screen or not
    }


}



