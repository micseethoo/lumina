package entities;

import audio.AudioPlayer;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

    // Variables
    private Playing playing;

    // Variables to store the sprite sheet animations and states all for each enemy
    private BufferedImage[][] slimeArr;
    private BufferedImage[][] golemArr;
    private BufferedImage[][] wolfArr;
    private BufferedImage[][] reaverHoundArr;
    private BufferedImage[][] lightseekerArr;
    private BufferedImage[][] imperialMageArr;


    // Create a new arraylist to store all enemy instances
    private ArrayList<Slime> slimes = new ArrayList<>();
    private ArrayList<Wolf> wolves = new ArrayList<>();
    private ArrayList<Golem> golems = new ArrayList<>();
    private ArrayList<ReaverHound> reaverHounds = new ArrayList<>();
    private ArrayList<Lightseeker> lightseekers = new ArrayList<>();
    private ArrayList<ImperialMage> imperialMages = new ArrayList<>();


    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    // Load enemies based on where they are placed in the level pixel map
    public void loadEnemies(Level level) {
        slimes = level.getSlimes();
        golems = level.getGolems();
        wolves = level.getWolves();
        reaverHounds = level.getReaverHounds();
        lightseekers = level.getLightseekers();
        imperialMages = level.getImperialMages();

        System.out.println("Size of slimes: " + slimes.size());
        System.out.println("Size of golems: " + golems.size());
        System.out.println("Size of wolves: " + wolves.size());
        System.out.println("Size of skull wolves: " + reaverHounds.size());
        System.out.println("Size of Lightseekers: " + lightseekers.size());
        System.out.println("Size of imperial mages: " + imperialMages.size());
    }

    // Update the position and state of each enemy that are still alive (only update when enemy is still active)
    public void update(int[][] lvlData, Player player) {
        for (Slime s : slimes)
            if (s.isActive()) {
                s.update(lvlData, player);
            }

        for (Wolf w : wolves)
            if (w.isActive()) {
                w.update(lvlData, player);
            }

        for (Golem g : golems)
            if (g.isActive()) {
                g.update(lvlData, player);
            }

        for(ReaverHound rh : reaverHounds)
            if (rh.isActive())
                rh.update(lvlData, player);

        for (Lightseeker fa : lightseekers)
            if(fa.isActive()) {
                fa.update(lvlData, player);
            }

        for (ImperialMage im : imperialMages)
            if(im.isActive()) {
                im.update(lvlData, player);
            }
    }

    // Draws all enemies for each stage
    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        drawSlimes(g, xLvlOffset, yLvlOffset);
        drawGolems(g, xLvlOffset, yLvlOffset);
        drawWolves(g, xLvlOffset, yLvlOffset);
        drawReaverHounds(g, xLvlOffset, yLvlOffset);
        drawLightseekers(g, xLvlOffset, yLvlOffset);
        drawImperialMages(g, xLvlOffset, yLvlOffset);
    }

    private void drawSlimes(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Slime s : slimes) {
            if(s.isActive()) {
                g.drawImage(slimeArr[s.getEnemyState()][s.getAniIndex()], (int) s.getHitbox().x - xLvlOffset - SLIME_DRAWOFFSET_X + s.flipX(), (int) s.getHitbox().y - yLvlOffset - SLIME_DRAWOFFSET_Y, SLIME_WIDTH * s.flipW(), SLIME_HEIGHT, null);
//                s.drawHitbox(g, xLvlOffset, yLvlOffset); // For Testing Purposes
//                s.drawAttackBox(g, xLvlOffset, yLvlOffset); // For Testing Purposes
            }
        }
    }

    private void drawWolves(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Wolf w : wolves) {
            if(w.isActive()) {
                g.drawImage(wolfArr[w.getEnemyState()][w.getAniIndex()], (int) w.getHitbox().x - xLvlOffset - WOLF_DRAWOFFSET_X + w.flipX(), (int) w.getHitbox().y - yLvlOffset - WOLF_DRAWOFFSET_Y, WOLF_WIDTH * w.flipW(), WOLF_HEIGHT, null);
//                w.drawHitbox(g, xLvlOffset, yLvlOffset); // For Testing Purposes
//                w.drawAttackBox(g, xLvlOffset, yLvlOffset); // For Testing Purposes
            }
        }
    }

    private void drawGolems(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Golem gm : golems) {
            if(gm.isActive()) {
                g.drawImage(golemArr[gm.getEnemyState()][gm.getAniIndex()], (int) gm.getHitbox().x - xLvlOffset - GOLEM_DRAWOFFSET_X + gm.flipX(), (int) gm.getHitbox().y - yLvlOffset - GOLEM_DRAWOFFSET_Y, GOLEM_WIDTH * gm.flipW(), GOLEM_HEIGHT, null);
//                gm.drawHitbox(g, xLvlOffset, yLvlOffset); // For Testing Purposes
//                gm.drawAttackBox(g, xLvlOffset, yLvlOffset); // For Testing Purposes
            }
        }
    }

    private void drawReaverHounds(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (ReaverHound rh : reaverHounds) {
            if(rh.isActive()) {
                g.drawImage(reaverHoundArr[rh.getEnemyState()][rh.getAniIndex()], (int) rh.getHitbox().x - xLvlOffset - REAVER_HOUND_DRAWOFFSET_X + rh.flipX(), (int) rh.getHitbox().y - yLvlOffset - REAVER_HOUND_DRAWOFFSET_Y, (int) (REAVER_HOUND_WIDTH * rh.flipW()), (int) (REAVER_HOUND_HEIGHT), null);
//                rh.drawHitbox(g, xLvlOffset, yLvlOffset); // For Testing Purposes
//                rh.drawAttackBox(g, xLvlOffset, yLvlOffset); // For Testing Purposes
            }
        }
    }

    private void drawLightseekers(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Lightseeker l : lightseekers) {
            if(l.isActive()) {
                g.drawImage(lightseekerArr[l.getEnemyState()][l.getAniIndex()], (int) l.getHitbox().x - xLvlOffset - LIGHTSEEKER_DRAWOFFSET_X + l.flipX(), (int) l.getHitbox().y - yLvlOffset - LIGHTSEEKER_DRAWOFFSET_Y, (int) (LIGHTSEEKER_WIDTH * l.flipW()), (int) (LIGHTSEEKER_HEIGHT), null);
//                l.drawHitbox(g, xLvlOffset, yLvlOffset);  // For Testing Purposes
//                l.drawAttackBox(g, xLvlOffset, yLvlOffset); // For Testing Purposes
            }
        }
    }

    private void drawImperialMages(Graphics g, int xLvlOffset, int yLvlOffset) {
        Graphics2D g2d = (Graphics2D) g;

        for (ImperialMage im : imperialMages) {
            if(im.isActive()) {
                g.drawImage(imperialMageArr[im.getEnemyState()][im.getAniIndex()], (int) im.getHitbox().x - xLvlOffset - IMPERIAL_MAGE_DRAWOFFSET_X + im.flipX(), (int) im.getHitbox().y - yLvlOffset - IMPERIAL_MAGE_DRAWOFFSET_Y, (int) (IMPERIAL_MAGE_WIDTH * im.flipW() * 2f), (int) (IMPERIAL_MAGE_HEIGHT * 2f), null);
                im.renderAttack(g2d, xLvlOffset, yLvlOffset); // Draw Special AOE Attack by this enemy class (indicator for player)
//                im.drawHitbox(g, xLvlOffset, yLvlOffset);  // For Testing Purposes
//                im.drawAttackBox(g, xLvlOffset, yLvlOffset); // For Testing Purposes
            }
        }
    }





    public boolean checkEnemyHit (Rectangle2D.Float arrowBox, Player player) { // Arrows shot by player hitting enemies, dealing damage and playing sound effect of enemy being hit
        for(Slime s : slimes)
            if(s.isActive())
                if(s.getCurrentHealth() > 0) {
                    if(arrowBox.intersects(s.getHitbox())) {
                        s.hurt(25, player);
                        enemyHitSound(SLIME);
                        return true;
                    }
                }


        for(Wolf w : wolves)
            if(w.isActive())
                if(w.getCurrentHealth() > 0) {
                    if(arrowBox.intersects(w.getHitbox())) {

                        w.hurt(20, player);
                        enemyHitSound(WOLF);
                        return true;
                    }
                }

        for(Golem g : golems)
            if(g.isActive())
                if(g.getCurrentHealth() > 0) {
                    if(arrowBox.intersects(g.getHitbox())) {
                        g.hurt(0, player);
                        enemyHitSound(GOLEM);
                        return true;
                    }
                }

        for(Lightseeker l : lightseekers)
            if(l.isActive())
                if(l.getCurrentHealth() > 0) {
                    if(arrowBox.intersects(l.getHitbox())) {
                        l.hurt(30, player);
                        enemyHitSound(LIGHTSEEKER);
                        return true;
                    }
                }

        for(ReaverHound rh : reaverHounds)
            if(rh.isActive())
                if(rh.getCurrentHealth() > 0) {
                    if(arrowBox.intersects(rh.getHitbox())) {
                        rh.hurt(10, player);
                        enemyHitSound(REAVER_HOUND);
                        return true;
                    }
                }

        for(ImperialMage im : imperialMages)
            if(im.isActive())
                if(im.getCurrentHealth() > 0) {
                    if(arrowBox.intersects(im.getHitbox())) {
                        im.hurt(30, player);
                        enemyHitSound(IMPERIAL_MAGE);
                        return true;
                    }
                }

        return false;

        //Pass in player argument to solely get sound of enemies
    }

    // Storing and playing enemy getting hit sounds by enemy type
    public void enemyHitSound(int enemyType) {
        switch(enemyType) {
            case SLIME:
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.SLIME_HIT);
                break;
            case WOLF:
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.WOLF_HIT);
                break;
            case GOLEM:
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GOLEM_HIT);
                break;
            case LIGHTSEEKER:
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.LIGHTSEEKER_HIT);
                break;
            case REAVER_HOUND:
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.REAVER_HOUND_HIT);
                break;
            case IMPERIAL_MAGE:
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.IMPERIAL_MAGE_HIT_SFX);
                break;
        }
    }

    // Load the enemy images from res folder
    private void loadEnemyImgs() {

        this.slimeArr = this.getImgArr(LoadSave.GetSpriteAtlas("slime.png"), 7, 6, 32, 32);
        this.wolfArr = this.getImgArr(LoadSave.GetSpriteAtlas("wolf.png"), 8, 5, 64, 32);
        this.reaverHoundArr = this.getImgArr(LoadSave.GetSpriteAtlas("reaver_hound.png"), 7, 5, 64, 64);
        this.golemArr = this.getImgArr(LoadSave.GetSpriteAtlas("golem.png"), 11, 5, 42, 42);
        this.lightseekerArr = this.getImgArr(LoadSave.GetSpriteAtlas("lightseeker.png"), 12, 5, 80, 80);
        this.imperialMageArr = this.getImgArr(LoadSave.GetSpriteAtlas("imperial_mage.png"), 13, 7, 160, 128);

    }

    // Method for storing the sprite sheet of enemy using a 2D array and reading each pixel
    private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
        BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];

        for(int j = 0; j < tempArr.length; ++j) {
            for(int i = 0; i < tempArr[j].length; ++i) {
                tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
            }
        }

        return tempArr;
    }

    // Reset the states and position of each enemy
    public void resetAllEnemies() {
        for (Slime s : slimes)
            s.resetEnemy();

        for (Wolf w : wolves)
            w.resetEnemy();

        for (Golem g : golems)
            g.resetEnemy();

        for (ReaverHound rh : reaverHounds)
            rh.resetEnemy();

        for (Lightseeker l : lightseekers)
            l.resetEnemy();

        for (ImperialMage im : imperialMages)
            im.resetEnemy();
    }
}
