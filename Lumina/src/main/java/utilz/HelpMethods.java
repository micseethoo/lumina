package utilz;

import audio.AudioPlayer;
import com.example.lumina.Game;
import entities.*;
import gamestates.Playing;
import levels.Level;
import objects.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;

public class HelpMethods {
    private static Level level;
    private static AudioPlayer audioPlayer;
    //    private ObjectManager objectManager;
    private static Playing playing;



    // Method for determining whether an entity can move to a certain area in a level
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {

        // The below 4 checks for all 4 corners
        if(!IsSolid(x,y,lvlData))
            if(!IsSolid(x+width,y+height,lvlData))
                if(!IsSolid(x+width,y,lvlData))
                    if(!IsSolid(x,y+height,lvlData))
                        return true;
        return false;

    }

    // Method for checking whether an entity is moving out of the level
    private static boolean IsSolid(float x, float y, int[][] lvlData) {

        // Check if entity is in the edge of the level (Horizontal direction)
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if(x < 0 || x >= maxWidth)
            return true;

        // Check if entity is in the edge of the level (Vertical direction)
        int maxHeight = lvlData.length * Game.TILES_SIZE;
        if (y < 0 || y >= maxHeight)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData); // Pass
    }

    // Checks if arrow is hitting level tiles
    public static boolean IsArrowHittingLevel(Arrow a, int[][] lvlData) {
        return IsSolid(a.getHitbox().x + a.getHitbox().width / 2, a.getHitbox().y + a.getHitbox().height / 2, lvlData);

    }

    // Checks if dart is hitting level tiles
    public static boolean IsProjectileHittingLevel(Dart d, int[][] lvlData) {
        return IsSolid(d.getHitbox().x + d.getHitbox().width / 2, d.getHitbox().y + d.getHitbox().height / 2, lvlData);

    }

    // Checks if the tiles in the game are solid or not
    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile]; // Gets the value of the tile used

        if (value != 10) // tile value 10 is the only tile that is an air tile
            return true;
        return false; // Anything else is solid
    }

    // Checks if entity is next to a wall (Horizontal)
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {

        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            // Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;

        } else {
            // Left
            return currentTile * Game.TILES_SIZE;
        }
    }

    // Checks if entity is jumping towards a roof or falling to a floor
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            // Falling
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            // Jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    // Checks if entity is on the floor
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check pixel below bottom left and right
        if(!IsSolid(hitbox.x, hitbox.y + hitbox.height+1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;

        return true;
    }

    // Checks if the entity is standing on is solid (is a floor)
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if(xSpeed > 0)
            return IsSolid(hitbox.x +hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
        else
            return IsSolid(hitbox.x +xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    // Checks if dart trap can see player
    public static boolean CanDartTrapSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
    }

    // Checks if the tile the entity is on can be walked on
    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        if (IsAllTilesClear(xStart, xEnd, y, lvlData))
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
            }
        return true;
    }

    // Checks if there are any air blocks in the level
    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
        return true;
    }

    // (For enemies) Checks if the enemy can see the player
    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float enemyHitbox, Rectangle2D.Float playerHitbox, int yTile) {
        int firstXTile = (int) (enemyHitbox.x / Game.TILES_SIZE);
        int secondXTile;

        if (IsSolid(playerHitbox.x, playerHitbox.y + playerHitbox.height + 1, lvlData))
            secondXTile = (int) (playerHitbox.x / Game.TILES_SIZE);
        else
            secondXTile = (int) ((playerHitbox.x + playerHitbox.width) / Game.TILES_SIZE);

        if(firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
    }

    // Gets the tiles placed based on the pixel map of the level
    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                System.out.print(value);
                if(value >= 165)
                    value = 10;
                lvlData[j][i] = value;
            }
        }

        return lvlData;
    }

    // Gets the slimes' spawn for each level based on its placement in the level
    public static ArrayList<Slime> GetSlimes(BufferedImage img) {
        ArrayList<Slime> list = new ArrayList<>(); // New array list for storing enemy instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                System.out.print(value);
                if (value == SLIME)
                    list.add(new Slime(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    // Gets the golems' spawn for each level based on its placement in the level
    public static ArrayList<Golem> GetGolems(BufferedImage img) {
        ArrayList<Golem> list = new ArrayList<>(); // New array list for storing enemy instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                System.out.print(value);
                if (value == GOLEM)
                    list.add(new Golem(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    // Gets the wolves' spawn for each level based on its placement in the level
    public static ArrayList<Wolf> GetWolves(BufferedImage img) {
        ArrayList<Wolf> list = new ArrayList<>(); // New array list for storing enemy instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                System.out.print(value);
                if (value == WOLF)
                    list.add(new Wolf(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    // Gets the lightseekers' spawn for each level based on its placement in the level
    public static ArrayList<Lightseeker> GetLightseekers(BufferedImage img) {
        ArrayList<Lightseeker> list = new ArrayList<>(); // New array list for storing enemy instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                System.out.print(value);
                if (value == LIGHTSEEKER)
                    list.add(new Lightseeker(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    // Gets the reaver hounds' spawn for each level based on its placement in the level
    public static ArrayList<ReaverHound> GetReaverHounds(BufferedImage img) {
        ArrayList<ReaverHound> list = new ArrayList<>(); // New array list for storing enemy instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                System.out.print(value);
                if (value == REAVER_HOUND)
                    list.add(new ReaverHound(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    // Gets the imperial mages' spawn for each level based on its placement in the level
    public static ArrayList<ImperialMage> GetImperialMages(BufferedImage img) {
        ArrayList<ImperialMage> list = new ArrayList<>(); // New array list for storing enemy instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                System.out.print(value);
                if (value == IMPERIAL_MAGE)
                    list.add(new ImperialMage(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    // Get the player's spawn based on its placement in the level
    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                System.out.print(value);
                if (value == 50) // Value 50 is the player's spawn point (green)
                    return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
            }
        return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
    }

    // Gets the portal location in the level based on its placement in the level pixel map
    public static ArrayList<Portal> GetPortal(BufferedImage img) {
        ArrayList<Portal> list = new ArrayList<>(); // New array list for storing portal instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                System.out.print(value);
                if (value == PORTAL)
                    list.add(new Portal(i * Game.TILES_SIZE, j * Game.TILES_SIZE, PORTAL));
                System.out.println("Portal created/spawned");
            }

        return list;
    }




    // Collectibles

    // Gets the crystal fragment location in the level based on its placement in the level pixel map
    public static ArrayList<Crystal> GetCrystals(BufferedImage img) {
        ArrayList<Crystal> list = new ArrayList<>(); // New array for storing object instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                System.out.print(value);
                if (value == CRYSTAL_FRAGMENT) {
                    list.add(new Crystal(i * Game.TILES_SIZE + 10, j * Game.TILES_SIZE + 10, CRYSTAL_FRAGMENT));
                }
            }

        return list;
    }

    // Gets the potion location in the level based on its placement in the level pixel map
    public static ArrayList<Potion> GetPotions(BufferedImage img) {
        ArrayList<Potion> list = new ArrayList<>(); // New array for storing object instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                System.out.print(value);
                if (value == RED_POTION)
                    list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, RED_POTION));
            }
        return list;
    }



    // TRAPS

    // Gets the magic spike location in the level based on its placement in the level pixel map
    public static ArrayList<MagicSpikes> GetMagicSpikes(BufferedImage img) {
        ArrayList<MagicSpikes> list = new ArrayList<>(); // New array for storing traps instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                System.out.print(value);
                if (value == MAGIC_TRAP)
                    list.add(new MagicSpikes(i * Game.TILES_SIZE, j * Game.TILES_SIZE, MAGIC_TRAP));
            }
        return list;
    }

    // Gets the magic pool location in the level based on its placement in the level pixel map
    public static ArrayList<MagicPool> GetMagicPools(BufferedImage img) {
        ArrayList<MagicPool> list = new ArrayList<>(); // New array for storing traps instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                System.out.print(value);
                if (value == MAGIC_POOL)
                    list.add(new MagicPool(i * Game.TILES_SIZE, j * Game.TILES_SIZE, MAGIC_POOL));
            }
        return list;
    }

    // Gets the dart trap location in the level based on its placement in the level pixel map
    public static ArrayList<DartTrap> GetDartTraps(BufferedImage img) {
        ArrayList<DartTrap> list = new ArrayList<>(); // New array for storing traps instance

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                System.out.print(value);
                if (value == DARTTRAP_LEFT || value == DARTTRAP_RIGHT)
                    list.add(new DartTrap(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }
        return list;
    }
}
