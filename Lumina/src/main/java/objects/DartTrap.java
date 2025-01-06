package objects;

import com.example.lumina.Game;

public class DartTrap extends GameObject{

    private int tileY; // Variable for getting current tile of dart trap

    public DartTrap(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.TILES_SIZE;
        this.initHitbox(36,26);
        hitbox.x -= (int) (36 * Game.SCALE); // Hitbox of dart trap (Horizontal)
        hitbox.y += (int) (26 * Game.SCALE - (int) (13.2 * Game.SCALE)); // Hitbox of dart trap (Vertical)
    }

    public void update() {
        if(doAnimation)
            updateAnimationTick(); // Initialise shooting animation of dart trap
    }

    // Method for determining to shoot player if on the same tile
    public int getTileY() {
        return tileY;
    }
}
