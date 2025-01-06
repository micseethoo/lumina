package entities;

import audio.AudioPlayer;
import com.example.lumina.Game;
import gamestates.Playing;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static audio.AudioPlayer.SLIME_KILLED;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.*;

public class Slime extends Enemy {

    private int attackBoxOffsetX; // Variable for offset of attack box of slime from slime hitbox

    public Slime(float x, float y) {
        super(x, y, SLIME_WIDTH, SLIME_HEIGHT, SLIME);
        initHitbox(25, 15, 0); // Initialise hitbox of slime
        initAttackBox();
    }


    // Initialise attackbox of slime
    public void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (24 * Game.SCALE * 1.5f), (int) (16 * Game.SCALE * 1.5f));
        attackBoxOffsetX = (int) (12 * Game.SCALE); // Offset from slime
    }

    // Update the animation, behavior and attack box direction of the slime
    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();

        updateAttackBox();
    }

    // Update attack box placement of slime based on walked direction
    private void updateAttackBox() {
        if(walkDir == RIGHT)
            attackBox.x = hitbox.x;
        else
            attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y-6;
    }

    // Update behavior of slime (based on state)
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
                        if (isPlayerCloseForAttack(player, 1, 3))
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


    // Flip drawing of slime based on faced direction

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
