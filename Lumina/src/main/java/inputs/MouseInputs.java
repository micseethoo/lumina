package inputs;

import com.example.lumina.GamePanel;
import gamestates.Gamestate;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;
    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseMoved(MouseEvent e) { // Mainly to show hover animation of each button
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e); // For allowing users to hover buttons that pop up in the playing state (e.g. level completed screen shown in the playing state after player clears a level)
                gamePanel.getGame().getPlaying().getPlayerBuffs().getMouseCoords(e); // Show tooltip of buff
                break;
            case OPTIONS:
                gamePanel.getGame().getGameSettings().mouseMoved(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { // Not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e); // For menu button interactions in the main menu of the game (e.g. new game button pressed to start new game)
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e); // For allowing users to interact with any button that pops up (e.g. level completed screen shown in the playing state after player clears a level)
                break;
            case OPTIONS:
                gamePanel.getGame().getGameSettings().mousePressed(e); // For button interactions in settings of the game
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) { // Same thing as above for mousePressed method, but reads events where the mouse button is released after being pressed
        switch(Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameSettings().mouseReleased(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { // Not used

    }

    @Override
    public void mouseExited(MouseEvent e) { // Not used

    }

    @Override
    public void mouseDragged(MouseEvent e) { // Same thing as above for mousePressed method, but reads events where the mouse button is released after being pressed
        switch (Gamestate.state) {
            case OPTIONS:
                gamePanel.getGame().getGameSettings().mouseDragged(e);
                break;
            default:
                break;
        }
    }

}
