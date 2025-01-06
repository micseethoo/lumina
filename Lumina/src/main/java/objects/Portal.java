package objects;

import com.example.lumina.Game;

public class Portal extends GameObject{
    public Portal(int x, int y, int objType) {
        super(x, y, objType);
        createHitBox();
    }

    private void createHitBox() {
        initHitbox(18, 32); // Initialise hitbox of portal
        xDrawOffset = (int) (8 * Game.SCALE); // Offset from actual sprite sheet of portal (Horizontal)
        yDrawOffset = 0;
    }

    // Update animation of portal
    public void update() {
        updateAnimationTick();
    }
}
