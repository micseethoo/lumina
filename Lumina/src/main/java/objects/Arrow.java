package objects;

import com.example.lumina.Game;
import entities.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static utilz.Constants.Projectiles.*;

public class Arrow {

    private Rectangle2D.Float hitbox;
    private int dir;
    private boolean active = true;
    private long creationTime; // Variable to check how long the arrow has been created


    public Arrow(int x, int y, int dir) {
        this.dir = dir; // Direction of arrow travelling
        this.creationTime = System.currentTimeMillis();

        int xOffset; // Offset of where the arrow is being drawn from the player (Horizontal)
        int yOffset = (int) (18 * Game.SCALE); // Offset of where the arrow is being drawn from the player (Vertical)

        if (dir == 1) {
            xOffset = (int) (32 * Game.SCALE);
        } else
            xOffset = (int) (-20 * Game.SCALE);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, ARROW_WIDTH, ARROW_HEIGHT); // Initialise new hitbox of the arrow
    }

    // Update position of the arrow travelling (Based on the direction of the arrow it is travelling and the speed of the arrow)
    public void updatePos() {
        hitbox.x += dir * ARROW_SPEED;
    }

    // Used by the 'ObjectManager' class instance to draw the arrow based on the size of the hitbox (Including its change in position when travelling)
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getDir() {
        return dir;
    }

    public long getCreationTime() {
        return creationTime;
    }
}
