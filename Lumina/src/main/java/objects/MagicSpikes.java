package objects;

public class MagicSpikes extends GameObject {
    public MagicSpikes(int x, int y, int objType) {
        super(x, y, objType);

        initHitbox(36, 36); // Initialise hitbox of magic spikes
    }

    public void update() {
        updateAnimationTick(); // Updates animation of magic spikes in game
    }
}
