package entities;

import audio.AudioPlayer;
import com.example.lumina.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import static audio.AudioPlayer.IMPERIAL_MAGE_ATTACK_SFX;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;

public class ImperialMage extends Enemy {
    private int attackBoxOffsetX = (int)(138 * Game.SCALE);
    private int attackBoxOffsetY = (int)(126 * Game.SCALE);
    private Game game;

    private boolean expandingCircle;
    private float circleRadius;
    private float maxCircleRadius = (int) (150 * Game.SCALE); // Maximum radius of the expanding circle
    private float circleExpansionRate = 0.6f * Game.SCALE; // Rate at which the circle expands
    private Random rand;

    public ImperialMage(float x, float y) {
        super(x, y, IMPERIAL_MAGE_WIDTH, IMPERIAL_MAGE_HEIGHT, IMPERIAL_MAGE);
        initHitbox(20, 34, -80);
        initAttackBox();
        expandingCircle = false;
        circleRadius = 0;
        rand = new Random();
    }

    public void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(300 * Game.SCALE), (int)(300 * Game.SCALE));
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();

        // For drawing the attack indicator
        if (expandingCircle) {
            circleRadius += circleExpansionRate;
            if (circleRadius >= maxCircleRadius) {
                expandingCircle = false;
                circleRadius = 0;
            }
        }
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y - attackBoxOffsetY;
    }

    public void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (state) {
                case IMPERIAL_MAGE_IDLE:
                    newState(IMPERIAL_MAGE_RUNNING);
                    break;
                case IMPERIAL_MAGE_RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player, 4, 0)) {
                            newState(IMPERIAL_MAGE_ATTACK); // Initiate attack
                            expandingCircle = true; // To create the attack indicator
                            circleRadius = 0; // Initial radius 0, constantly increases as the attack is being charged up
                        }
                    }
                    move(lvlData);
                    break;
                case IMPERIAL_MAGE_ATTACK:
                    player.getPlaying().getGame().getAudioPlayer().playEffect(IMPERIAL_MAGE_ATTACK_SFX);

                    if (aniIndex == 0) {
                        attackChecked = false;
                    }
                    if (aniIndex == 10 && !attackChecked) {
                        checkEnemyHit(attackBox, player);
                    }
                    break;
                case IMPERIAL_MAGE_HIT:
                    break;
            }
        }
    }

    public int flipX() {
        if (walkDir == RIGHT)
            return 0;
        else
            return width + (int) (54 * Game.SCALE);
    }

    public int flipW() {
        if (walkDir == RIGHT)
            return 1;
        else
            return -1;
    }

    public void renderAttack(Graphics2D g, int xLvlOffset, int yLvlOffset) {
        // Render the expanding circle if the mage is attacking
        if (expandingCircle) {
            // Save the original stroke
            Stroke originalStroke = g.getStroke();

            // Create a dashed stroke
            float[] dashPattern = {10, 10}; // Length of dashes and gaps
            Stroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);

            // Set the dashed stroke
            g.setStroke(dashedStroke);

            // Draw multiple circles with varying colors and opacities to create a fiery effect
            for (int i = 0; i < 3; i++) {
                int flickerRadius = (int)(circleRadius + rand.nextInt(10) - 5); // Add some randomness to the radius
                float alpha = 0.5f - (0.1f * i); // Decrease opacity for inner circles

                g.setColor(new Color(1.0f, 0f, 0.0f, alpha)); // Orange color with varying opacity
                int centerX = (int) (hitbox.x + hitbox.width / 2 - xLvlOffset);
                int centerY = (int) (hitbox.y + hitbox.height / 2 - yLvlOffset);
                g.drawOval(centerX - flickerRadius, centerY - flickerRadius, flickerRadius * 2, flickerRadius * 2);
            }

            // Restore the original stroke
            g.setStroke(originalStroke);
        }
    }
}


