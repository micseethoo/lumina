package gamestates;

import com.example.lumina.Game;
import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.InstructionWindow;
import ui.LevelCompletedOverlay;
//import ui.PauseOverlay;
import gamestates.PauseOverlay;
import utilz.Constants;
import utilz.LoadSave;
import utilz.PlayerBuffs;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class Playing extends State implements Statemethods {

    // Variables for most instances
    private Player player;
    private Enemy enemy;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PlayerBuffs playerBuffs;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private InstructionWindow instructionWindow;

    // variable for checking whether game is paused to show pause overlay as well as to allow players to pause/unpause game
    private boolean paused = false;


    // Variable for level/stage related values
    private int xLvlOffset;
    private int yLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int upBorder = (int) (0.5 * Game.GAME_HEIGHT);
    private int downBorder = (int) (0.5 * Game.GAME_HEIGHT);
    private int maxLvlOffsetX;
    private int maxLvlOffsetY;

    // Variable to store stage background
    private BufferedImage stageBG;

    // Boolean variable
    private boolean gameOver;
    private boolean lvlCompleted = false;
    private boolean playerDying = false;
    private boolean inputEnabled = true; // (ONLY IN TUTORIAL STAGE/LEVEL) Checks if player is allowed to perform certain actions (since certain actions are limited until relevant window is shown)

    // Variable to store position of elements in game
    public int x,y;



    public Playing(Game game) {
        super(game);
        initClasses(); // Initialise all essential classes

        stageBG = LoadSave.GetSpriteAtlas(LoadSave.STAGE_BACKGROUND); // Get the stage bg

        calcLvlOffset();
        loadStartLevel();
    }

    // Method to load the content of the next level
    public void loadNextLevel() {
        player.setNumCrystalFragments(player.getNumCrystalFragments() + getLevelManager().getCurrentLevel().getCrystals().size()); // Updates the number of crystal fragments based on the collected crystals from previous stages
        resetAll(); // Reset all values for all entities and objects back to the default
        levelManager.loadNextLevel(); // Loads the contents of the next stage
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn()); // Sets the spawn point of the player in the next stage
    }

    // Load all enemies and objects in the game based on level the player is in
    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }


    // Method to calculate level offsets
    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffsetX();
        maxLvlOffsetY = levelManager.getCurrentLevel().getLvlOffsetY();
    }


    // Initialise all necessary classes
    private void initClasses() {
        levelManager = new LevelManager(game); // Create a new levelManager instance in the playing state for getting all values and assets pertaining to the levels of the game
        enemyManager = new EnemyManager(this); // Create a new enemyManager instance to spawn and update all enemies in the game
        objectManager = new ObjectManager(this, enemyManager); // Create a new enemyManager instance to spawn and update all objects in the game

        player = new Player(80, 80, (int) (64 * Game.SCALE), (int) (64 * Game.SCALE), this, objectManager); // Initialise the player instance
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData()); // Gets the current level that the player is in
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn()); // Sets the spawn of the player

        playerBuffs = new PlayerBuffs(player); // Create new playerBuffs instance to show buffs that the player has and tooltips

        pauseOverlay = new PauseOverlay(this); // Creates a new pause overlay in the game
        gameOverOverlay = new GameOverOverlay(this); // Create a new game over overlay in the game
        levelCompletedOverlay = new LevelCompletedOverlay(this); // Creates a new level completed overlay in the game



        // FOR TUTORIAL STAGE (STAGE 0)
        if(levelManager.getLevelIndex() == 0) {
            System.out.println("IS IT RUNNING HERE");
            Constants.InstructionWindowConstants.instructionAreaIntro.y += (int) player.getHitbox().y;
            Constants.InstructionWindowConstants.instructionAreaCrystal.y += (int) player.getHitbox().y;
            Constants.InstructionWindowConstants.instructionAreaCrystal.x += (int) player.getHitbox().x;
            Constants.InstructionWindowConstants.instructionAreaPotion.y += (int) player.getHitbox().y;
            Constants.InstructionWindowConstants.instructionAreaPotion.x += (int) player.getHitbox().x;
            Constants.InstructionWindowConstants.instructionAreaJump.y += (int) player.getHitbox().y;
            Constants.InstructionWindowConstants.instructionAreaJump.x += (int) player.getHitbox().x;
            Constants.InstructionWindowConstants.instructionAreaDash.y += (int) player.getHitbox().y;
            Constants.InstructionWindowConstants.instructionAreaDash.x += (int) player.getHitbox().x;
            Constants.InstructionWindowConstants.instructionAreaAttack.y += (int) player.getHitbox().y;
            Constants.InstructionWindowConstants.instructionAreaAttack.x += (int) player.getHitbox().x;
            System.out.println("Level Index is "+ levelManager.getLevelIndex());
            java.util.List<Rectangle> sequentialInstructionAreas = Arrays.asList(
                    Constants.InstructionWindowConstants.instructionAreaIntro,
                    Constants.InstructionWindowConstants.instructionAreaMovement
            );
            java.util.List<String[]> sequentialInstructionTexts = Arrays.asList(
                    Constants.InstructionWindowConstants.instructionIntro,
                    Constants.InstructionWindowConstants.instructionMovement
            );
            java.util.List<Rectangle> triggeredInstructionAreas = Arrays.asList(
                    Constants.InstructionWindowConstants.instructionAreaCrystal,
                    Constants.InstructionWindowConstants.instructionAreaPotion,
                    Constants.InstructionWindowConstants.instructionAreaJump,
                    Constants.InstructionWindowConstants.instructionAreaDash,
                    Constants.InstructionWindowConstants.instructionAreaAttack
            );
            List<String[]> triggeredInstructionTexts = Arrays.asList(
                    Constants.InstructionWindowConstants.instructionCrystal,
                    Constants.InstructionWindowConstants.instructionPotion,
                    Constants.InstructionWindowConstants.instructionJump,
                    Constants.InstructionWindowConstants.instructionDash,
                    Constants.InstructionWindowConstants.instructionAttack
            );
            instructionWindow = new InstructionWindow(sequentialInstructionAreas, sequentialInstructionTexts,
                    triggeredInstructionAreas, triggeredInstructionTexts);

        }
    }

    // Update all events and actions in the game (e.g. conditions, behavior, etc)
    @Override
    public void update() {
        if(paused)
            pauseOverlay.update();
        else if (lvlCompleted)
            levelCompletedOverlay.update();
        else if (gameOver)
            gameOverOverlay.update();
        else if(playerDying)
            player.update();

        // TUTORIAL STAGE ONLY (STAGE 0)
        else if(levelManager.getLevelIndex() == 0) {
            if(instructionWindow.isShowInstructions()) {
                instructionWindow.update();
            }

            else { // Update everything as normal, along with the instruction window
                levelManager.update();
                objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
                player.update();
                enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
                checkCloseToBorder();
                instructionWindow.checkInstructionArea(player.getHitbox());
            }


        }


        else { // If player is not on tutorial stage, loading everything as normal (without the instruction window)
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        }
    }

    // Method to check whether to pan the player view to the edges of the level
    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int playerY = (int) player.getHitbox().y;
        int diffX = playerX - xLvlOffset;
        int diffY = playerY - yLvlOffset;

        if (diffX > rightBorder) {
            xLvlOffset += diffX - rightBorder;
        } else if (diffX < leftBorder) {
            xLvlOffset += diffX - leftBorder;
        }

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        } else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }

        if (diffY > upBorder)
            yLvlOffset += diffY - upBorder;
        else if (diffY < downBorder)
            yLvlOffset += diffY - downBorder;

        if (yLvlOffset > maxLvlOffsetY)
            yLvlOffset = maxLvlOffsetY;
        else if (yLvlOffset < 0)
            yLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) { // Draw all the elements in the game
        g.drawImage(stageBG, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT,null);

        levelManager.draw(g, xLvlOffset, yLvlOffset);
        player.render(g, xLvlOffset, yLvlOffset);
        playerBuffs.drawBuffs(g); // Draw player buff icons + tooltips
        enemyManager.draw(g, xLvlOffset, yLvlOffset);
        objectManager.draw(g, xLvlOffset, yLvlOffset);

        if(levelManager.getLevelIndex() == 0) {
            instructionWindow.draw(g);
        }



        if (paused) { // Darken screen when game is paused
            g.setColor(new Color(0,0,0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver) // Draw game over overlay when player dies
            gameOverOverlay.draw(g);
        else if (lvlCompleted) // Draw level completed overlay when player finishes level
            levelCompletedOverlay.draw(g);
    }

    // Reset all in game elements (e.g. player status, enemy health and spawn location, object location, etc.)
    public void resetAll() {
        gameOver = false;
        paused = false;
        playerDying = false;
        lvlCompleted = false;

        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    // Method to check if player's hitbox touches potion
    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }

    // Method to check if player's hitbox touches traps
    public void checkTrapsTouched(Player player) {
        objectManager.checkTrapsTouched(player);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mousePressed(e);

            // Only used in tutorial level/stage
            else if (levelManager.getLevelIndex() == 0) {
                player.setAttacking(false);
                if(instructionWindow.isAttackInstructionDisplayed()) { // Checks if instruction window for tutorial for attacking mechanic is true
                    if (e.getButton() == MouseEvent.BUTTON1)
                        player.setAttacking(true);
                    else if (e.getButton() == MouseEvent.BUTTON3)
                        player.dash();
                }

                else if(instructionWindow.isDashInstructionDisplayed())  // Checks if instruction window for tutorial for dash mechanic is true
                    if (e.getButton() == MouseEvent.BUTTON3)
                        player.dash();

            } else {
                if (e.getButton() == MouseEvent.BUTTON1)
                    player.setAttacking(true);
                else if (e.getButton() == MouseEvent.BUTTON3)
                    player.dash();
            }
        } else {
            gameOverOverlay.mousePressed(e); // Allow for mouse interactions for game over overlay
        }
    }

    // Checks mouse events for mouse released (after pressing on a button)
    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
        } else {
            gameOverOverlay.mouseReleased(e);
        }
    }

    // Checks mouse events for mouse moved (for hover animatonl)
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
            else
                playerBuffs.getMouseCoords(e);
        } else
            gameOverOverlay.mouseMoved(e);
    }

    // Checks keyboard commands made by player
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && inputEnabled)
            gameOverOverlay.keyPressed(e); // (Check game over overlay for more key commands there)
        else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A: // player goes left when A key pressed/held down
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true); // player goes right when D key pressed/held down
                    break;
                case KeyEvent.VK_SPACE: // player jumps when SPACE key pressed/held down
                    if (levelManager.getLevelIndex() == 0) { // (Not shown in document) Do not allow player to jump before window that teaches player about jump is shown
                        player.setJump(false);
                        if (instructionWindow.isJumpInstructionDisplayed())
                            player.setJump(true); // Allow player to jump after instruction window shown for jump
                    } else
                        player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
                case KeyEvent.VK_BACK_SPACE: // (Not shown in document) If player wishes to skip tutorial, press the BACKSPACE Key (Only applicable to tutorial stage)
                    if (levelManager.getLevelIndex() == 0)
                        setLevelCompleted(true);
                case KeyEvent.VK_ENTER:
                    instructionWindow.keyPressed(e); // (Not shown in document) Close instruction window when ENTER key pressed
                    objectManager.enterPortal();
                    break;
                case KeyEvent.VK_F:
                    player.toggleInvisibility(); // Toggle invisibility
                    break;
                case KeyEvent.VK_F10:
                    setLevelCompleted(true); // (Not shown in document) - Allows player to instantly skip level (Admin command - testing purposes)
                    break;
                case KeyEvent.VK_F12:
                    player.addCrystalCount(); // (Not shown in document) - Adds counter by 1 (Admin command - testing purposes)
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver && inputEnabled)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false); // Stop moving left when A key is released
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false); // Stop moving right when D key is released
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false); // Stop jumping when SPACE key is released
                    break;
        }
    }

    // Responsible for setting the stage/level to be completed for each stage/level
    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
        if(levelCompleted) {
            game.getAudioPlayer().lvlCompleted(); // Play audio of level completed when stage is completed
        }
    }


    // Sets the max level offset shown to the player in the horizontal direction
    public void setMaxXLvlOffset(int XlvlOffset) {
        this.maxLvlOffsetX = XlvlOffset;
    }


    // Sets the max level offset shown to the player in the vertical direction
    public void setMaxYLvlOffset(int YlvlOffset) {
        this.maxLvlOffsetY = YlvlOffset;
    }

    // Method for unpausing the game
    public void unpauseGame() {
        paused = false;
    }

    public void WindowFocusLost() {
        player.resetDirBooleans();
    }

    // Getter for player instance
    public Player getPlayer() {
        return player;
    }

    // Getter for enemyManager instance
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    // Getter for objectManager instance
    public ObjectManager getObjectManager() {
        return objectManager;
    }

    // Getter for levelManager instance
    public LevelManager getLevelManager() {

        return levelManager;
    }

    // Setter for player to dying when player dies
    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }

    // Setter for pausing game
    public void setPaused(boolean p) {
        this.paused = p;
    }

    // (Not in document) gets the index for which instruction window to show
    public int getInstructionIndex() {
        return instructionWindow.getTriggeredIndex();
    }

    // Getter to determine whether the game over state is true or false (Used to show game over overlay)
    public boolean isGameOver() {
        return gameOver;
    }

    // Getter for playerBuffs instance
    public PlayerBuffs getPlayerBuffs() {
        return playerBuffs;
    }

    public int getxLvlOffset() {
        return xLvlOffset;
    }

    public int getyLvlOffset() {
        return yLvlOffset;
    }
}
