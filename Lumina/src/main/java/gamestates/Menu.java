package gamestates;

import com.example.lumina.Game;
import save.SaveProgress;
import ui.MenuButton;
import ui.noButton;
import ui.yesButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.BackButtons.*;
import static utilz.Constants.UI.BackButtons.NO_BUTTON_HEIGHT;

public class Menu extends State implements Statemethods {

    private MenuButton[] buttons = new MenuButton[4]; // Fixed array list size to store all button instances in main menu
    private BufferedImage titleImg, backgroundImg, overrideImg; // Buffered Image variable to store title and main menu background image
    private int titleX, titleY, titleWidth, titleHeight; // Variable to store position and size of title in main menu

    // Save override variables
    private boolean isOverwriteActive = false;
    private boolean isMenuButtonActive = true;
    private int bgX, bgY, bgW, bgH; // Height and Width and Position of the SaveOverride Background
    private yesButton confirmBYes;
    private noButton confirmBNo;

    public Menu(Game game) {
        super(game);
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND); // Load Main menu background
        loadButtons();
        loadBackground();
        loadSaveOverrideInterface();
    }

    // Load background of menu screen + title image
    private void loadBackground() {
        titleImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_TITLE);
        titleWidth = (int) (titleImg.getWidth() * Game.SCALE * 2);
        titleHeight = (int) (titleImg.getHeight() * Game.SCALE * 2);

        titleX = Game.GAME_WIDTH / 2 - titleWidth / 2;
        titleY = Game.GAME_HEIGHT / 2 - (titleHeight / 2) - 100;
    }

    private void loadSaveOverrideInterface() {
        overrideImg = LoadSave.GetSpriteAtlas(LoadSave.OVERRIDE_SCREEN); // Load override interface
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE * 0.3f);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE * 0.3f);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (100 * Game.SCALE);

        // Positions of yes and no buttons for save overrride interface
        int yesX = (int) (350 * Game.SCALE);
        int noX = (int) (670 * Game.SCALE);
        int confirmY = (int) (330 * Game.SCALE);

        // Yes & No button initialisation for save override interface
        confirmBYes = new yesButton(yesX, confirmY, YES_BUTTON_WIDTH, YES_BUTTON_HEIGHT);
        confirmBNo = new noButton(noX, confirmY, NO_BUTTON_WIDTH, NO_BUTTON_HEIGHT);
    }

    // Load buttons in main menu screen
    private void loadButtons() {
        buttons[0] = new MenuButton((int) (150 * Game.SCALE), (int) (450 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton((int) (150 * Game.SCALE), (int) (500 * Game.SCALE), 1, Gamestate.PLAYING);
        buttons[2] = new MenuButton((int) (150 * Game.SCALE), (int) (550 * Game.SCALE), 2, Gamestate.OPTIONS);
        buttons[3] = new MenuButton((int) (150 * Game.SCALE), (int) (600 * Game.SCALE), 3, Gamestate.QUIT);
    }

    @Override
    // Update state of the buttons and to detect user input (e.g. button clicked)
    public void update() {
        for(MenuButton mb : buttons)
            mb.update();

        // Save override buttons
        confirmBYes.update();
        confirmBNo.update();
    }

    @Override
    // Draw main menu elements
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        g.drawImage(titleImg, titleX, titleY, titleWidth, titleHeight, null);
        for(MenuButton mb : buttons)
            mb.draw(g);

        if(isOverwriteActive) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            g.drawImage(overrideImg, bgX, bgY, bgW, bgH, null);
            confirmBYes.draw(g);
            confirmBNo.draw(g);

        }
    }

    // Reset button states
    private void resetButtons() {
        for(MenuButton mb : buttons) {
            mb.resetBools();
        }
    }

    // UNUSED
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    // Checks if button is pressed by user
    @Override
    public void mousePressed(MouseEvent e) {
        if(isMenuButtonActive) {
            for (MenuButton mb : buttons) {
                if (isIn(e, mb)) {
                    mb.setMousePressed(true);
                }
            }
        }

        if(isInConfirm(e, confirmBYes)) {
            confirmBYes.setMousePressed(true);
            isMenuButtonActive = true;
        }

        if(isInConfirm2(e, confirmBNo)) {
            confirmBNo.setMousePressed(true);
            isMenuButtonActive = true;
        }
    }

    // Checks if button pressed is later released by user
    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed())
                    if (mb == buttons[0]) {
                        if (SaveProgress.doesProgressFileExist()) {
                            isOverwriteActive = true;
                            isMenuButtonActive = false;
                        } else {
                            mb.applyGameState();
                            if (mb.getState() == Gamestate.PLAYING) {

                                game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                            }
                        }

                    } else if (mb == buttons[1]) {
                        if (SaveProgress.doesProgressFileExist()) {
                            Gamestate.state = Gamestate.PLAYING;
                            game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                            game.getPlaying().getLevelManager().loadLevel();
                            game.getPlaying().getPlayer().resetAll();
                            game.getPlaying().resetAll();
                            isOverwriteActive = false;
                        }
                    } else {
                        mb.applyGameState();
                        if (mb.getState() == Gamestate.PLAYING) {

                            game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                        }
                    }


                break;
            }
        }

        if(isInConfirm(e, confirmBYes)) {
            if (confirmBYes.isMousePressed())
            {
                Gamestate.state = Gamestate.PLAYING;
                game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                game.getPlaying().setPaused(false);
                game.getPlaying().resetAll();
                SaveProgress.saveProgress(0,0);
                game.getPlaying().getLevelManager().resetNewLevel();
                isOverwriteActive = false;
            }


        }

        if(isInConfirm2(e, confirmBNo)) {
            if (confirmBNo.isMousePressed()) // Go back to main manu when the user press on no
                isOverwriteActive = false;
        }
        resetButtons();
        confirmBYes.resetBools();
        confirmBNo.resetBools();
    }

    // Checks if button is being hovered by user (For hover animation)
    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons)
            mb.setMouseOver(false);

        for(MenuButton mb : buttons)
            if(isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }

        // Save override
        confirmBYes.setMouseOver(false);
        confirmBNo.setMouseOver(false);

        if (isInConfirm(e, confirmBYes)) {
            confirmBYes.setMouseOver(true);
            confirmBYes.setMousePressed(true);
        }

        if (isInConfirm2(e, confirmBNo)) {
            confirmBNo.setMouseOver(true);
            confirmBNo.setMousePressed(true);
        }
    }

    // Checks if certain key commands are pressed by the player
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_Q) { // Players can alternatively press q to exit game
            Gamestate.state = Gamestate.QUIT;
        }
    }


    // UNUSED
    @Override
    public void keyReleased(KeyEvent e) {

    }

    //
    private boolean isInConfirm(MouseEvent e, yesButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    private boolean isInConfirm2(MouseEvent e, noButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
