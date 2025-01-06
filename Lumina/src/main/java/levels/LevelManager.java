package levels;

import com.example.lumina.Game;
import gamestates.Gamestate;
import javafx.scene.transform.Scale;
import save.SaveLevelProgress;
import utilz.HelpMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static audio.AudioPlayer.MENU_1;
import static save.SaveProgress.deleteProgress;

public class LevelManager {

    private Game game; // Variabilising the game instance
    private BufferedImage[] levelSprite; // Storing the level tileset used into an arraylist
    private ArrayList<Level> levels; // Array for storing each level instance
    private int lvlIndex = 0; // Default level index (start from stage 0 - Tutorial)


    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadLevel() {
        this.lvlIndex = SaveLevelProgress.loadLevelProgress();
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxXLvlOffset(newLevel.getLvlOffsetX());
        game.getPlaying().setMaxYLvlOffset(newLevel.getLvlOffsetY());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
        game.getPlaying().getPlayer().setSpawn(HelpMethods.GetPlayerSpawn(newLevel.img));

    }

    public void resetNewLevel() {
        lvlIndex = 0;
        game.getPlaying().getPlayer().setNumCrystalFragments(0);
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxXLvlOffset(newLevel.getLvlOffsetX());
        game.getPlaying().setMaxYLvlOffset(newLevel.getLvlOffsetY());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
        game.getPlaying().getPlayer().setSpawn(HelpMethods.GetPlayerSpawn(newLevel.img));
    }

    // Save the current level the player is in before returning the main menu/exiting
    public void saveLevel() {
        SaveLevelProgress.saveLevelProgress(lvlIndex);
    }

    // Method for loading the next level in the game
    public void loadNextLevel() {
        lvlIndex++; // Increment level index for proceeding into next stage

        if (lvlIndex >= levels.size()) { // If all 3 stages are cleared
            lvlIndex = 0; // Set back to tutorial stage
            game.getPlaying().getPlayer().setNumCrystalFragments(0); // Reset number of crystal fragments
            System.out.println("No more levels, game completed");
            deleteProgress();
            Gamestate.state = Gamestate.MENU;
            game.getAudioPlayer().playSong(MENU_1);
        }

        Level newLevel = levels.get(lvlIndex); // Get the data of next level

        game.getPlaying().setMaxXLvlOffset(newLevel.getLvlOffsetX()); // Gets the level offset
        game.getPlaying().setMaxYLvlOffset(newLevel.getLvlOffsetY());

        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData()); // Loads the data and information of the player (e.g. player spawn location)
        game.getPlaying().getEnemyManager().loadEnemies(newLevel); // Loads enemies in the next level
        game.getPlaying().getObjectManager().loadObjects(newLevel); // Loads objects in the next level
    }

    private void buildAllLevels() { // Initialise all levels in the game (But not loaded/shown to the player)
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img)); // Add a new level instance to the levels array
    }

    private void importOutsideSprites() { // Loads the tileset for the elements/tiles used in each stage
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[170];
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 11; i++) {
                int index = j * 11 + i;
                levelSprite[index] = img.getSubimage(i * 16, j * 16, 16, 16);
            }
        }
    }

    public void draw(Graphics g, int xlvlOffset, int ylvlOffset) { // Draw the level based on the selected tiles used
        for (int j = 0; j < levels.get(lvlIndex).getLevelData().length; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i - xlvlOffset, Game.TILES_SIZE * j - ylvlOffset, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }

    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getLevelIndex() {
        return lvlIndex;
    }
}
