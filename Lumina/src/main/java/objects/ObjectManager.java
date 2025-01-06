package objects;

import com.example.lumina.Game;
import entities.EnemyManager;
import entities.Player;
import gamestates.Playing;
import levels.Level;
import utilz.Constants;
import utilz.LoadSave;

import static audio.AudioPlayer.DART_SHOT;
import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.ObjectConstants.DARTTRAP_LEFT;
import static utilz.Constants.Projectiles.*;
import static utilz.Constants.Projectiles.ARROW_HEIGHT;
import static utilz.HelpMethods.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ObjectManager {
    // Variabailising class instances for accessing methods from the below classes
    private Playing playing;
    private EnemyManager enemyManager;

    // Arraylist to store animation images of objects in the game
    private BufferedImage[] portalImg;
    private BufferedImage[] crystalImg;
    private BufferedImage[] potionImgs;
    private BufferedImage[] magicTrapImg;
    private BufferedImage[] magicPoolImg;
    private BufferedImage[] dartTrapImg;

    // Arraylist to store static images of objects in the game
    private BufferedImage arrowImg;
    private BufferedImage dartImg;

    // Arraylist to store object instances in the game
    private ArrayList<Arrow> arrows = new ArrayList<>();
    private ArrayList<Portal> portal = new ArrayList<>();;
    private ArrayList<Potion> potions = new ArrayList<>();;
    private ArrayList<Crystal> crystals = new ArrayList<>();
    private ArrayList<MagicSpikes> magicSpikes = new ArrayList<>();
    private ArrayList<MagicPool> magicPools = new ArrayList<>();
    private ArrayList<DartTrap> dartTraps = new ArrayList<>();
    private ArrayList<Dart> darts = new ArrayList<>();

    // Poison DOT from Dart Trap
    private Timer damageTimer; // Timer instance
    private int damageDuration = 5000; // DOT Duration
    private boolean poisonDOT; // Checks if player is affected by the poison DOT

    boolean isInPortal = false; // Checks if player is in portal or not
    private long lastShotTime = 0; // Time of last arrow fired

    public ObjectManager(Playing playing, EnemyManager enemyManager) {
        this.playing = playing;
        this.enemyManager = enemyManager;
        loadImgs(); // Load object images into the game
    }


    // Method to check whether player hitbox is touching the hitbox of traps in the game
    public void checkTrapsTouched(Player p) {
        for (MagicSpikes ms : magicSpikes)
            if (ms.getHitbox().intersects(p.getHitbox()))
                p.kill();

        for (MagicPool mp : magicPools)
            if (mp.getHitbox().intersects(p.getHitbox())) {
                p.changeOverheal(-2);
                if(p.getCurrentHealth() < 10)
                    p.setCurrentHealth(10);
                else
                    p.changeHealth(-2);
            }

    }


    // Method to check whether player hitbox is intersecting/touching the hitbox of other objects in game
    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Crystal c : crystals) {
            if(c.isActive()) {
                if(hitbox.intersects(c.getHitbox())) {
                    c.setActive(false); // Set to inactive
                    playing.getPlayer().addCrystalCount(); // Increments the crystal counter when picking up a crystal fragment
                    playing.getPlayer().displayCrystalCount(); // For testing purposes, prints out the current number of crystal the player has
                }
            }
        }

        for (Potion p : potions) {
            if(p.isActive()) {
                if(hitbox.intersects(p.getHitbox())) {
                    p.setActive(false); // Set to inactive after collection
                    applyEffectToPlayer(p); // Apply some effect to player
                }
            }
        }

        // Checks if player is touching the portal
        for (Portal p : portal) {
            if(p.isActive()) {
                if(playing.getPlayer().getHitbox().intersects(p.getHitbox())) {
                    System.out.println("Press ENTER to enter portal");
                    isInPortal = true; // Sets to true when player touches portal (is on the portal)
                    break;
                } else
                    isInPortal = false; // Set to false if player is no longer on the portal
            }
        }
    }


    // Method that let's the player complete the stage as long as they are in the portal and meets the sufficient requirements to proceed to the next stage
    public void enterPortal() {
        if (isInPortal && playing.getPlayer().getCrystalCounter() >= playing.getLevelManager().getCurrentLevel().getCrystals().size() ) {
            playing.setLevelCompleted(true); // Sets level completed to true
            isInPortal = false; // Sets the isInPortal variable to false (reset) to ensure that the player will not be able to enter the 'portal' without being physically there, even with the sufficient requirements
        }
    }


    // Apply effect to player based on type of potion collected (Right now only health potion so this method may be redundant
    public void applyEffectToPlayer(Potion p) {
        if (p.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(25); // Heal player by 25HP when potion picked up
    }


    // Method to load all object into the game based on how they are placed in the level
    public void loadObjects(Level newLevel) {
        crystals = newLevel.getCrystals();
        potions = newLevel.getPotions();
        magicSpikes = newLevel.getMagicSpikes();
        magicPools = newLevel.getMagicPools();
        dartTraps = newLevel.getDartTraps();
        portal = newLevel.getPortal();
        System.out.println("Loaded " + crystals.size() + " crystals");
        System.out.println("Number of dart traps: " + dartTraps.size());
        arrows.clear();
    }

    // Method to load images of objects in the game (some have animations which require an array while some are just static images which can just be stored as a single variable)
    private void loadImgs() {
        BufferedImage portalSprite = LoadSave.GetSpriteAtlas(LoadSave.PORTAL_ATLAS);
        portalImg = new BufferedImage[6];
        for (int j = 0; j < portalImg.length; j++) {
            portalImg[j] = portalSprite.getSubimage(j * 32, 0, 32, 32);
        }

        BufferedImage crystalSprite = LoadSave.GetSpriteAtlas(LoadSave.CRYSTAL_ATLAS);
        crystalImg = new BufferedImage[4];
        for (int j = 0; j < crystalImg.length; j++) {
            crystalImg[j] = crystalSprite.getSubimage(j * 16, 0, 16, 16);
        }

        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[15];
        for (int j = 0; j < potionImgs.length; j++) {
            potionImgs[j] = potionSprite.getSubimage(j * 19, 0, 19, 24);
        }

        BufferedImage magicTrapSprite = LoadSave.GetSpriteAtlas(LoadSave.MAGICTRAP_ATLAS);
        magicTrapImg = new BufferedImage[25];
        for (int j = 0; j < magicTrapImg.length; j++) {
            magicTrapImg[j] = magicTrapSprite.getSubimage(j * 32, 0, 32, 32);
        }

        BufferedImage magicPoolSprite = LoadSave.GetSpriteAtlas(LoadSave.MAGICPOOL_ATLAS);
        magicPoolImg = new BufferedImage[4];
        for (int j = 0; j < magicPoolImg.length; j++) {
            magicPoolImg[j] = magicPoolSprite.getSubimage(j * 32, 0, 32, 32);
        }

        BufferedImage dartTrapSprite = LoadSave.GetSpriteAtlas(LoadSave.DARTTRAP_ATLAS);
        dartTrapImg = new BufferedImage[15];
        for (int j = 0; j < dartTrapImg.length; j++) {
            dartTrapImg[j] = dartTrapSprite.getSubimage((j * 96) + 34, 12, 28, 20);
        }

        arrowImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYERARROW);
        dartImg = LoadSave.GetSpriteAtlas(LoadSave.DART);
    }

    // Update the states of the objects in the game
    public void update(int[][] lvlData, Player player) {
        for(Crystal c : crystals)
            if(c.isActive())
                c.update();
        for(Potion p : potions)
            if(p.isActive())
                p.update();
        for(MagicSpikes ms : magicSpikes)
            ms.update();
        for(MagicPool mp : magicPools)
            mp.update();
        for (Portal p : portal)
            p.update();

        updateDartTraps(lvlData, player); // Update animation, state and action of dart traps in game

        updateArrows(lvlData, enemyManager, playing.getPlayer());
        updateDarts(lvlData, playing.getPlayer());
    }


    // Method that updates the state and animation of the dart trap
    public void updateDartTraps(int[][] lvlData, Player player) {
        for(DartTrap dt : dartTraps) {
            if (!dt.doAnimation)
                if (dt.getTileY() == player.getTileY())
                    if (isPlayerInRange(dt, player))
                        if (isPlayerInFrontOfDartTrap(dt, player))
                            if (CanDartTrapSeePlayer(lvlData, player.getHitbox(), dt.getHitbox(), dt.getTileY()) && !player.isInvisible())
                                dt.setAnimation(true);

            dt.update(); // Update the dart trap to check for any conditions, actions, or events that occur in game (e.g. constantly checking if player is close to the trap)

            if (dt.getAniIndex() == 8 && dt.getAniTick() == 0) {
                shootDart(dt); // Shoot a dart at the 8th animation frame
                player.getPlaying().getGame().getAudioPlayer().playEffect(DART_SHOT); // Play sound of dart being shot
            }
        }
    }

    // Method that creates a new dart instance when being shot by a dart trap
    private void shootDart(DartTrap dt) {
        int dir = 1;
        if (dt.getObjType() == DARTTRAP_LEFT)
            dir = -1;


        darts.add(new Dart((int) dt.getHitbox().x, (int) dt.getHitbox().y, dir));
    }

    // Method that checks if player is in front of the dart trap
    private boolean isPlayerInFrontOfDartTrap(DartTrap dt, Player player) {
        if(dt.getObjType() == DARTTRAP_LEFT) {
            if(dt.getHitbox().x > player.getHitbox().x)
                return true;

        } else if(dt.getHitbox().x < player.getHitbox().x) {
            return true;
        }
        return false;
    }

    // Method that checks whether the player is in range of the dart trap (method borrowed from HelpMethods class)
    private boolean isPlayerInRange(DartTrap dt, Player player) {
        int absValue = (int) Math.abs(player.getHitbox().x - dt.getHitbox().x);
        return absValue <= (Game.TILES_SIZE * 5);
    }

    // Method that creates a new arrow instance when player shoots an arrow
    public void shootArrows(Player player) {
        int dir = player.isLeft() ? -1 : 1;

        // Adjust the fire rate of the arrow by the player
        if (System.currentTimeMillis() - lastShotTime > Constants.Projectiles.FIRE_RATE) { // Check firing rate
            arrows.add(new Arrow((int) player.getHitbox().x, (int) player.getHitbox().y, dir));
            lastShotTime = System.currentTimeMillis();
        }

    }


    // Draw the objects based on the location in the level (apply level offset as well if player cannot see the object/player is moving closer to the object to the point they are able to see it appear on screen)
    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        drawCrystals(g, xLvlOffset, yLvlOffset);
        drawPotions(g, xLvlOffset, yLvlOffset);
        drawMagicSpikes(g, xLvlOffset, yLvlOffset);
        drawPools(g, xLvlOffset, yLvlOffset);
        drawDartTraps(g, xLvlOffset, yLvlOffset);
        drawDarts(g, xLvlOffset, yLvlOffset);
        drawArrows(g, xLvlOffset, yLvlOffset);
        if(playing.getPlayer().getCrystalCounter() == playing.getLevelManager().getCurrentLevel().getCrystals().size()) {
            System.out.println("Get in the portal now"); // For testing purposes
            drawPortal(g, xLvlOffset, yLvlOffset); // Only draw the portal when player collects all crystals in a stage
        }
    }

    // Draw the dart traps based on the direction it is placed in
    private void drawDartTraps(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (DartTrap dt : dartTraps) {
            int x = (int) (dt.getHitbox().x - xLvlOffset);
            int y = (int) (dt.getHitbox().y - yLvlOffset);

            int width = DART_TRAP_WIDTH;
            if (dt.getObjType() == DARTTRAP_RIGHT) {
                x += width;
                width *= -1;

            }

            g.drawImage(dartTrapImg[dt.getAniIndex()], x, y, width, DART_TRAP_HEIGHT, null);
//            dt.drawHitbox(g, xLvlOffset, yLvlOffset);
        }
    }

    // Draw darts created by dart trap
    private void drawDarts(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(Dart d : darts) {
            if (d.isActive()) {
                if (d.getDir() == 1)
                    g.drawImage(dartImg, (int)(d.getHitbox().x - xLvlOffset), (int) (d.getHitbox().y - yLvlOffset), ARROW_WIDTH, ARROW_HEIGHT, null);
                else
                    g.drawImage(dartImg, (int)(d.getHitbox().x - xLvlOffset + ARROW_WIDTH), (int) (d.getHitbox().y - yLvlOffset), ARROW_WIDTH * -1, ARROW_HEIGHT, null);
            }
        }
    }

    // Draw arrows created by player
    private void drawArrows(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(Arrow a : arrows) {
            if (a.isActive()) {
                if (a.getDir() == 1)
                    g.drawImage(arrowImg, (int)(a.getHitbox().x - xLvlOffset), (int) (a.getHitbox().y - yLvlOffset), ARROW_WIDTH, ARROW_HEIGHT, null);
                else
                    g.drawImage(arrowImg, (int)(a.getHitbox().x - xLvlOffset + ARROW_WIDTH), (int) (a.getHitbox().y - yLvlOffset), ARROW_WIDTH * -1, ARROW_HEIGHT, null);
            }

        }
    }

    // Draw portal and animation
    private void drawPortal(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(Portal p : portal)
            g.drawImage(portalImg[p.getAniIndex()], (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset() - yLvlOffset), PORTAL_WIDTH, PORTAL_HEIGHT, null);

    }

    // Draw magic spikes and animations
    private void drawMagicSpikes(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(MagicSpikes ms : magicSpikes)
            g.drawImage(magicTrapImg[ms.getAniIndex()], (int) (ms.getHitbox().x - xLvlOffset), (int) (ms.getHitbox().y - ms.getyDrawOffset() - yLvlOffset), MAGIC_SPIKE_WIDTH, MAGIC_SPIKE_HEIGHT, null);

    }

    // Draw magic pools and animations
    private void drawPools(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(MagicPool mp : magicPools)
            g.drawImage(magicPoolImg[mp.getAniIndex()], (int) (mp.getHitbox().x - xLvlOffset), (int) (mp.getHitbox().y - mp.getyDrawOffset() - yLvlOffset), MAGIC_POOL_WIDTH, MAGIC_POOL_HEIGHT, null);

    }

    // Draw crystal fragment and animations
    private void drawCrystals(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(Crystal c : crystals)
            if(c.isActive()) {
                g.drawImage(crystalImg[c.getAniIndex()], (int) (c.getHitbox().x - c.getxDrawOffset() - xLvlOffset),
                        (int) (c.getHitbox().y - c.getyDrawOffset() - yLvlOffset),
                        CRYSTAL_FRAGMENT_WIDTH,
                        CRYSTAL_FRAGMENT_HEIGHT,
                        null);
//                c.drawHitbox(g, xLvlOffset, yLvlOffset);
            }
    }

    // Draw potions and its animations
    private void drawPotions(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(Potion p : potions)
            if(p.isActive())
                g.drawImage(potionImgs[p.getAniIndex()], (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
                        (int) (p.getHitbox().y - p.getyDrawOffset() - yLvlOffset),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
    }

    // Update arrow state and position in the level
    private void updateArrows(int[][] lvlData, EnemyManager enemyManager, Player player) {
        long currentTime = System.currentTimeMillis();

        for (Arrow a : arrows) {
            if (a.isActive()) {
                a.updatePos();

                if (enemyManager.checkEnemyHit(a.getHitbox(), player)) { // Checks if arrow hits enemy
                    if (playing.getPlayer().getPlayerBuffs().isHasOverheal()) // Checks if player has overheal buff
                        playing.getPlayer().changeHealth(2); // Heal the player when arrow hits enemies by 2hp
                    a.setActive(false);
                } else if (IsArrowHittingLevel(a, lvlData)) // If arrow hits arrow, then arrow will disappear
                    a.setActive(false);
                else if (currentTime - a.getCreationTime() > 2000) // If arrow travels for too long, it will disappear
                    a.setActive(false);
            }
        }
    }

    // Update dart state and position in the level
    private void updateDarts(int[][] lvlData, Player player) {
        for (Dart d : darts)
            if(d.isActive()) {
                d.updatePos();
                if (d.getHitbox().intersects(player.getHitbox())) { // Checks if dart hits player
                    if (player.getPlayerBuffs().isHasShadowflameArmor()) { // Checks if player has shadowflame armor buff for damage reduction
                        if (player.getOverhealHealth() > 0) { // Checks if player has overheal health
                            player.changeOverheal(-25);} // reduce the overheal health by 25 per dart hit (dart hits ignore damage reduction for only overheal health)
                        else
                            player.changeHealth(-(int) (25 * 0.8)); // Reduce player health by 80% of the 25 damage dealt by the dart


                        // DOT
                        if (damageTimer != null) {
                            damageTimer.cancel(); // cancel previous timer if it exists
                        }

                        damageTimer = new Timer();
                        poisonDOT = true; // Set poison tick to true
                        damageTimer.scheduleAtFixedRate(new TimerTask() {
                            private int elapsedTime = 0; // Start time for DOT effect and to check if 5 seconds has passed

                            @Override
                            public void run() {
                                if (elapsedTime >= damageDuration) {
                                    damageTimer.cancel();
                                    poisonDOT = false; // Remove poison tick after timer
                                    return;
                                }
                                if (poisonDOT) {
                                    player.changeHealth(-5);
                                    elapsedTime += 1000; // increment by 1 second (1000 ms)
                                }
                            }
                        }, 0, 1000);
                    }

                        d.setActive(false);
                    } else if (IsProjectileHittingLevel(d, lvlData)) // If darts hit level, it will disappear
                        d.setActive(false);
                }
    }


    // Resets all objects to its original position
    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());
        for(Crystal c : crystals)
            c.reset();
        for(Potion p : potions)
            p.reset();
        for(DartTrap dt : dartTraps)
            dt.reset();

        arrows.clear(); // Clears all arrow created by the player
        darts.clear(); // Clears all darts created by dart traps
        poisonDOT = false;
    }
}
