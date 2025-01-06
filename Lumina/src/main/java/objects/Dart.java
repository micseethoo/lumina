package objects;

import com.example.lumina.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Projectiles.*;

public class Dart {
    private Rectangle2D.Float hitbox; // Hitbox of the dart
    private int dir; // Direction of the dart
    private boolean active = true; // Checks if dart is active (default is active(true) when first created)

    public Dart(int x, int y, int dir) {
        int xOffset = (int) (0 * Game.SCALE);// Offset of the dart from the dart trap being shot from the left (Horizontal)
        int yOffset = (int) (10 * Game.SCALE); // Offset of the dart from the dart trap being shot from (Vertical)

        if (dir==1)
            xOffset = (int) (29 * Game.SCALE); // Offset of the dart being shot from the right (Horizontal)

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, ARROW_WIDTH, ARROW_HEIGHT); // Initialising new hitbox for the dart
        this.dir = dir;
    }


    // Updates the position of the dart based on the travel direction and speed of the dart
    public void updatePos() {
        hitbox.x += dir * DART_SPEED;
    }

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
}

