package inputs;

import com.example.lumina.GamePanel;
import com.example.lumina.GameWindow;
import gamestates.Gamestate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;
    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) { // Not used

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(Gamestate.state) { // Different gamestate has different keyboard inputs - When player released key previously pressed (Refer to each of the gamestates for the
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
            default:
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(Gamestate.state) { // Different gamestate has different keyboard inputs
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameSettings().keyPressed(e);
                break;
            default:
                break;
        }

        if (e.getKeyCode() == KeyEvent.VK_F4) { // F4 to set game to fullscreen
            gamePanel.getGame().getGameWindow().ToggleFullScreen(); // Toggle Full Screen
        }
    }
}
