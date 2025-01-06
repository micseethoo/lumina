package objects;

public class MagicPool extends GameObject{

    public MagicPool(int x, int y, int objType) {
        super(x, y, objType);

        initHitbox(36, 36); // Initialise hitbox of magic pools in game (For player interation)
    }

    public void update() {
        updateAnimationTick(); // Updates animation of magic pools in game
    }
}
