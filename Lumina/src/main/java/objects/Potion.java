package objects;

import com.example.lumina.Game;

public class Potion extends GameObject {

    // Variables
    private float hoverOffset; // Hover distance
    private int maxHoverOffset, hoverDir = 1; // Maximum hover distance + direction

    public Potion(int x, int y, int objType) {
        super(x,y, objType);
        doAnimation = true; // Constantly do the hover and spinning animation
        initHitbox(19, 24); // Initialise the hitbox of the potion

        maxHoverOffset = (int) (10 * Game.SCALE); // Adjusts the maximum hover distance of the potion
    }

    // Updates the animations of the potion
    public void update() {
        updateAnimationTick();
        updateHover();
    }

    // Method responsible for hover animation of potion object
    private void updateHover() {
        hoverOffset += (0.1f * Game.SCALE * hoverDir);

        if(hoverOffset >= maxHoverOffset)
            hoverDir = -1;
        else if (hoverOffset <= 0)
            hoverDir = 1;

        hitbox.y = y + hoverOffset;
    }
}
