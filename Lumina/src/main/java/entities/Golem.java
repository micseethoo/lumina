package entities;

import audio.AudioPlayer;
import com.example.lumina.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;

public class Golem extends Enemy {


    private int attackBoxOffsetX;

    public Golem(float x, float y) {
        super(x, y, GOLEM_WIDTH, GOLEM_HEIGHT, GOLEM);
        initHitbox(24, 28, 0);
        initAttackBox();
        walkSpeed = 0.1f * Game.SCALE;
    }

    public void initAttackBox() {
        attackBoxOffsetX = (int) (40 * Game.SCALE);
        attackBox = new Rectangle2D.Float(x, y, (int) (40 * Game.SCALE * 1.5f), (int) (25 * Game.SCALE * 1.5f));
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
        attackBox.y = hitbox.y - 6;
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
                        if (isPlayerCloseForAttack(player, 1.5f, 0))
                            newState(ATTACK);
                    }
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
            return 0;
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
