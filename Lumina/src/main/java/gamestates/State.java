package gamestates;

import audio.AudioPlayer;
import com.example.lumina.Game;
import ui.MenuButton;
import ui.PauseOverlayButton;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;


    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public boolean isIn2(MouseEvent e, PauseOverlayButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }


    // Switch game states method
    public void setGameState(Gamestate state) {
        switch(state) {
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
        }

        Gamestate.state = state;
    }
}
