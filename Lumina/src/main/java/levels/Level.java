package levels;

import com.example.lumina.Game;
import entities.*;
import objects.*;
import utilz.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.*;

public class Level {

    // Variables for getting, storing and loading level data
    public BufferedImage img;
    private int[][] lvlData;

    // Arraylist variables for all enemies in the game
    private ArrayList<Slime> slimes;
    private ArrayList<Golem> golems;
    private ArrayList<Wolf> wolves;
    private ArrayList<Lightseeker> lightseekers;
    private ArrayList<ReaverHound> reaverHounds;
    private ArrayList<ImperialMage> imperialMages;

    // Arraylist variables for objects in the game
    private ArrayList<Potion> potions;
    private ArrayList<Crystal> crystals;
    private ArrayList<MagicSpikes> magicSpikes;
    private ArrayList<MagicPool> magicPools;
    private ArrayList<DartTrap> dartTraps;
    private ArrayList<Portal> portal;

    // Variables related to the stage and how it is shown to the player
    private int lvlTilesWide;
    private int lvlTilesHigh;
    private int maxTilesXOffset;
    private int maxTilesYOffset;
    private int maxLvlOffsetX;
    private int maxLvlOffsetY;

    // Player spawn location variable
    private Point playerSpawn;


    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createCrystals();
        createPotions();
        createMagicTraps();
        createMagicPools();
        createDartTraps();
        createPortal();
        calcLvlOffsets();
        calcPlayerSpawn();
    }


    // Create all objects in each stage
    private void createPortal() {
        portal = HelpMethods.GetPortal(img);
    }

    private void createMagicTraps() {
        magicSpikes = HelpMethods.GetMagicSpikes(img);
    }

    private void createMagicPools() {
        magicPools = HelpMethods.GetMagicPools(img);
    }

    private void createDartTraps() {
        dartTraps = HelpMethods.GetDartTraps(img);
    }

    private void createCrystals() {
        crystals = HelpMethods.GetCrystals(img);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
    }


    // Calculate where the player spawns in each stage
    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calcLvlOffsets() { // Calculate the offsets for each level to show parts of the stage instead of the entire stage of the game (Camera zoom in and panning effect)
        lvlTilesWide = img.getWidth(); // Get the size of the entire stage (Horizontal)
        lvlTilesHigh = img.getHeight();  // Get the size of the entire stage (Vertical)
        maxTilesXOffset = lvlTilesWide - Game.TILES_IN_WIDTH; // Calculate how many tiles of the level to show to the player (Horizontal)
        maxTilesYOffset = lvlTilesHigh - Game.TILES_IN_HEIGHT; // Calculate how many tiles of the level to show to the player (Vertical)

        maxLvlOffsetX = Game.TILES_SIZE * maxTilesXOffset; // Calculate the total size of the level to show to the player (Horizontal)
        maxLvlOffsetY = Game.TILES_SIZE * maxTilesYOffset; // Calculate the total size of the level to show to the player (Vertical)
    }

    private void createEnemies() { // Create enemies in a particular stage
        slimes = GetSlimes(img);
        golems = GetGolems(img);
        wolves = GetWolves(img);
        reaverHounds = GetReaverHounds(img);
        lightseekers = GetLightseekers(img);
        imperialMages = GetImperialMages(img);
    }

    // Create the level based on the pixel map
    private void createLevelData() {
        lvlData = GetLevelData(img);
    }


    // All getter methods


    // Gets the pixel map image of the level
    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }


    // Get the array containing the level data
    public int[][] getLevelData() {
        return lvlData;
    }


    // Getter for the offset of the level (used by other classes for enemy behavior and how entities and objects are properly shown/drawn)
    public int getLvlOffsetX() {
        return maxLvlOffsetX;
    }

    public int getLvlOffsetY() {
        return maxLvlOffsetY;
    }


    // Getter for enemies in a stage/level
    public ArrayList<Slime> getSlimes() {
        return slimes;
    }

    public ArrayList<Golem> getGolems() {
        return golems;
    }

    public ArrayList<Wolf> getWolves() {
        return wolves;
    }

    public ArrayList<Lightseeker> getLightseekers() {
        return lightseekers;
    }

    public ArrayList<ReaverHound> getReaverHounds() {
        return reaverHounds;
    }

    public ArrayList<ImperialMage> getImperialMages() {return imperialMages;}


    // Getter for all objects in a stage/level
    public ArrayList<Portal> getPortal() {
        return portal;
    }

    public ArrayList<Crystal> getCrystals() {
        return crystals;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<MagicSpikes> getMagicSpikes() {
        return magicSpikes;
    }

    public ArrayList<MagicPool> getMagicPools() {
        return magicPools;
    }

    public ArrayList<DartTrap> getDartTraps() {
        return dartTraps;
    }


    // Get player spawn from pixel map
    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
