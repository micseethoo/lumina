package entities;

import com.example.lumina.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected float x,y; // Position of entity
    protected int width, height; // Width and height of entity (drawn)
    protected Rectangle2D.Float hitbox; // Hitbox of enemy
    protected int aniTick, aniIndex; // Animation Ticks and Animation frame of entity
    protected int state; // The states that an entity can have (e.g. dead or idle state)
    protected float airSpeed; // The vertical speed of the entity (jumping (player only) and falling (due to gravity))
    protected boolean inAir = false; // Checks if the entity is in the air in a particular stage and at the current time
    protected int maxHealth; // Maximum health that an entity has
    protected int currentHealth; // The current health of a particular entity at a specific moment (default is equal to max health on first load)
    protected Rectangle2D.Float attackBox; // (Enemy only) the attack area of the enemy
    protected float walkSpeed; // Horizontal speed of entity


    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    // Initialising the hitbox of the entity
    protected void initHitbox(int width, int height, int yHitboxOffset) {
        hitbox = new Rectangle2D.Float((int) x, (int) y + yHitboxOffset, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }


    // For testing hitbox (all entities) and attackbox (enemies only)

    protected void drawAttackBox(Graphics g, int xLvlOffset, int ylvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y - (5 - Game.SCALE) - ylvlOffset), (int) attackBox.width, (int) attackBox.height);
    }

    protected void drawHitbox(Graphics g, int xlvlOffset, int ylvlOffset) {
        g.setColor(Color.YELLOW);
        g.drawRect((int) hitbox.x - xlvlOffset, (int) hitbox.y - ylvlOffset, (int) hitbox.width, (int) hitbox.height);
    }
}
