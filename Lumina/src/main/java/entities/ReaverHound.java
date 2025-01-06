package entities;

import com.example.lumina.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;

public class ReaverHound extends Enemy {

    public ReaverHound(float x, float y) {
        super(x, y, REAVER_HOUND_WIDTH, REAVER_HOUND_HEIGHT, REAVER_HOUND);
        initHitbox((int) (44), (int) (32), 0);
        initAttackBox();
    }

    public void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (44 * Game.SCALE), (int) (32 * Game.SCALE));
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();

        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x;
        attackBox.y = hitbox.y + 10;
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
                    if(canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        this.setWalkSpeed(2.5f); // Run faster towards player when player is seen
                        if (isPlayerCloseForAttack(player, 1, 17))
                            newState(ATTACK);
                    } else
                        this.setWalkSpeed(0.5f); // Reset back to normal running speed
                    move(lvlData);
                    break;
                case ATTACK:
                    if(aniIndex == 0)
                        attackChecked = false;

                    if(aniIndex == 4 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    break;
                case HIT:
                    break;
            }
        }
    }



    public int flipX() {
        if(walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;
    }
}
