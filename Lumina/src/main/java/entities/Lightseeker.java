package entities;

import com.example.lumina.Game;

import java.awt.geom.Rectangle2D;

import static audio.AudioPlayer.IMPERIAL_MAGE_ATTACK_SFX;
import static audio.AudioPlayer.LIGHTSEEKER_ATTACK_SFX;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;

public class Lightseeker extends Enemy{
    private int attackBoxOffsetX;
    private int attackBoxOffsetY;

    public Lightseeker(float x, float y) {
        super(x, y, LIGHTSEEKER_WIDTH, LIGHTSEEKER_HEIGHT, LIGHTSEEKER);
        initHitbox(34, 34, 0);
        initAttackBox();
    }

    public void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(60 * Game.SCALE), (int)(90 * Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE * 12);
        attackBoxOffsetY = (int) (Game.SCALE * 50);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        if(walkDir == RIGHT)
            attackBox.x = hitbox.x;
        else
            attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y - attackBoxOffsetY;
    }

    public void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir)
            updateInAir(lvlData);
        else {
            switch (state) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        setWalkSpeed(10f); // Rushes towards player at lightning speed when player is seen
                        if (isPlayerCloseForAttack(player, 1.05f, 0))
                            newState(ATTACK);
                    } else
                        setWalkSpeed(0.5f); // Resets back to default walking speed
                    move(lvlData);
                    break;
                case ATTACK:
                    player.getPlaying().getGame().getAudioPlayer().playEffect(LIGHTSEEKER_ATTACK_SFX); // Play sound effect of attack
                    if (aniIndex == 0)
                        attackChecked = false;

                    if (aniIndex == 11 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    break;
                case HIT:
                    break;
            }
        }
    }

    public int flipX() {
        if(walkDir == RIGHT)
            return -30; // Account for offset
        else
            return width;
    }

    public int flipW() {
        if(walkDir == RIGHT)
            return 1;
        else
            return -1;
    }
}
