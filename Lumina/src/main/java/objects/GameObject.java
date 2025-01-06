package objects;

import com.example.lumina.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.ObjectConstants.*;

public class GameObject {

    protected int x, y, objType; // Position of objects in game & type of object (Variables)
    protected Rectangle2D.Float hitbox; // Hitbox of objects in the game (Variable)
    protected boolean doAnimation, active = true; // Checks whether an animation has commenced & whether object is active (Default for both is true) (Variables)
    protected int aniTick, aniIndex; // For animation (Variables)
    protected int xDrawOffset, yDrawOffset; // Draw offset from actual image (Variable)

    public GameObject(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }


    protected void updateAnimationTick() {
        if (GetSpriteAmount(objType) > 0)
            ANI_SPEED = 240 / GetSpriteAmount(objType);
        else
            ANI_SPEED = 12; // Default animation speed of all objects (If above condition fails)

        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objType)) {
                aniIndex = 0;
                if (objType == DARTTRAP_LEFT || objType == DARTTRAP_RIGHT)
                    doAnimation = false;
                }
            }
    }

    // Method for resetting objects due to certain conditions (e.g. player restarts game)
    public void reset() {
        // Animation start over again
        aniIndex = 0;
        aniTick = 0;
        active = true; // Item reappears again

        // Reset animation to false (= not shooting) after reset
        if (objType == DARTTRAP_LEFT || objType == DARTTRAP_RIGHT)
            doAnimation = false;
        else
            doAnimation = true;
    }

    // Initialise hitbox of object
    public void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float((int) x, (int) y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    // (For testing only) Draw hitbox of object
    public void drawHitbox(Graphics g, int xlvlOffset, int yLvlOffset) {
        g.setColor(Color.YELLOW);
        g.drawRect((int) hitbox.x - xlvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }


    // Getter methods
    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getObjType() {
        return objType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getAniTick() {
        return aniTick;
    }
}
