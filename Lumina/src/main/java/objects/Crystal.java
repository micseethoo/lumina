package objects;

import com.example.lumina.Game;

import java.awt.*;

public class Crystal extends GameObject{

    private float hoverOffset;
    private int maxHoverOffset, hoverDir = 1;

    public Crystal(int x, int y, int objType) {
        super(x, y, objType);
        createHitBox();
    }

    private void createHitBox() { // Initialise hitbox of crystal fragment
        initHitbox(13, 11);
        xDrawOffset = (int) (2 * Game.SCALE); // Offset from original sprite sheet (Horizontal)
        yDrawOffset = (int) (5 * Game.SCALE); // Offset from original sprite sheet (Vertical)

        maxHoverOffset = (int) (10 * Game.SCALE);
    }

    public void update() {
        updateAnimationTick(); // Update animation of crystal fragment (Glimmering effect)
        updateHover(); // Update hover animation of crystal fragment
    }

    // Update hover animation of crystal
    private void updateHover() {
        hoverOffset += (0.1f * Game.SCALE * hoverDir);

        if(hoverOffset >= maxHoverOffset)
            hoverDir = -1;
        else if (hoverOffset <= 0)
            hoverDir = 1;

        hitbox.y = y + hoverOffset;
    }
}
