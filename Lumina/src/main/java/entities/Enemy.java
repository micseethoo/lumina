package entities;

import audio.AudioPlayer;
import com.example.lumina.Game;

import java.awt.geom.Rectangle2D;

import static audio.AudioPlayer.*;
import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.GRAVITY;

public abstract class Enemy extends Entity{
    // Variables
    protected int enemyType; // What type of enemy for setting: attack damage, health, different animations, etc
    protected Player player; // Player instance to check for damage or being hit or conditions to check and initiate enemy attacks

    protected boolean firstUpdate = true; // Useful for loading enemies in stage

    protected int walkDir = LEFT; // Checks the direction of where the enemy is walking (default is set to left, meaning that enemies will first walk to the left first)
    protected int tileY; // Checks the tile the enemy is on top of (useful for determining whether the space they are walking is valid (solid) as well as checking if they can attack the player
    protected float attackDistance = Game.TILES_SIZE; // How far the enemy is able to initiate an attack on the player
    protected boolean active= true; // Checks if the enemy still exists in game
    protected boolean attackChecked; // Checking if an attack has been finished initialising (animation)

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType; // Getting the type of enemy
        maxHealth = GetMaxHealth(enemyType); // Getting the health of the enemy from 'Constant' class
        currentHealth = maxHealth; // Sets the current health of the enemy to the maximum on startup
        walkSpeed = 0.2f * Game.SCALE; // Sets the movement speed (horizontal) of the enemy
    }

    // Updates the animation ticks of the enemy based on in game updates and state of the enemy (either idle to initate new animation or attack to initiate new animation)
    protected void updateAnimationTick() {
        ANI_SPEED = GetAniSpeed(enemyType) / GetSpriteAmount(enemyType, state); // Gets the speed for each frame transition

        aniTick++; // Increment the animation tick per frame in game
        if (aniTick >= ANI_SPEED) {
            aniTick = 0; // Start from the start of the animation
            aniIndex++; // Iterate through each frame for the entire animation (based on sprite sheet)
            if (aniIndex >= GetSpriteAmount(enemyType, state)) { // If exceed (meaning end of animation), restart the animation until state changes
                aniIndex = 0; // Reset to the start of the animation

                if(enemyType != IMPERIAL_MAGE) // If enemy is not of type imperial mage, use default values for animation
                    switch(state) {
                        case ATTACK,HIT -> state = IDLE; // Set state of idle after an attack or after getting hit to ensure taht they do not get stuck in either of the aforementioned animations and states
                        case DEAD -> active = false; // Sets the existence of the enemy to false after dying animation
                    }
                else
                    switch(state) { // If enemy is of type imperial mage, then switch to using different constant callers (since the index of each animation in the imperial mage sprite sheet is different - it has its own dedicated constant callers)
                        case IMPERIAL_MAGE_ATTACK, IMPERIAL_MAGE_HIT -> state = IMPERIAL_MAGE_IDLE;
                        case IMPERIAL_MAGE_DEAD -> active = false;
                    }
            }
        }
    }

    protected void turnTowardsPlayer(Player player) { // Switch enemy direction if player is close (further explained below)
        if(player.hitbox.x > hitbox.x)
            walkDir = RIGHT; // Checks if player's hitbox is positioned more in the x direction in the game (stage/level) than the enemy, causing the enemy to face right when it indeed can detect the playuer
        else
            walkDir = LEFT; // Else set to left
    }

    // To detect whether enemy can see the player or not
    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE); // Gets the current tile the player is currently on
        if (playerTileY == tileY && !player.isInvisible() || enemyType == IMPERIAL_MAGE && playerTileY == tileY) { // If the player is on the same Y tile in the stage, execute the code below (unless they are an imperial mage, whereby the mage will attack regardless of if they are on the same tile lest it overlaps with its attackbox)
            if(isPlayerInRange(player)) { // Checks if the player is in range of the player
                if(IsSightClear(lvlData, hitbox, player.hitbox, tileY)) // Checks if there are no obstacles that blocks the line of sight of the player with the enemy
                    return true; // Return true for executing some code (Explained more below)
            }
        }
        return false; // Default return false if neither condition checks out
    }

    // Checks if player is in range of enemy attack
    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x); // Gets the difference in distance between the enemy's and player's hitbox
        return absValue <= (attackDistance * 5); // Should be 5 times the default attack distance of the enemy
    }

    // Checks if player is close enough for the enemy to land an attack on the player
    protected boolean isPlayerCloseForAttack(Player player, float attackRange, int attackOffset) {
        attackDistance = (int) (Game.TILES_SIZE * attackRange); // multiplier for attack range
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x); // Gets the difference in distance between the enemy's and player's hitbox
        if (this.walkDir == LEFT)
            return absValue <= attackDistance - attackOffset; // Adjust for offset
        else
            return absValue <= attackDistance + attackOffset; // Adjust for offset
    }

    // Used to initiate new state of the enemy when it changes state
    protected void newState(int state) {
        this.state = state; // sets to the new state
        aniTick = 0; // Restarts the animation tick to start at 0 (timer)
        aniIndex = 0; // Restarts the animation to start at the first frame
    }

    // Method for damaging the enemy and plays a sound when killed)
    public void hurt(int amount, Player player) {
        currentHealth -= amount; // Reduce the enemy health
        switch (enemyType) { // Play enemy hit sound
            case SLIME:
                player.getPlaying().getGame().getAudioPlayer().playEffect(SLIME_HIT);
                break;
            case WOLF:
                player.getPlaying().getGame().getAudioPlayer().playEffect(WOLF_HIT);
                break;
            case GOLEM:
                player.getPlaying().getGame().getAudioPlayer().playEffect(GOLEM_HIT);
                break;
            case LIGHTSEEKER:
                player.getPlaying().getGame().getAudioPlayer().playEffect(LIGHTSEEKER_HIT);
                break;
            case REAVER_HOUND:
                player.getPlaying().getGame().getAudioPlayer().playEffect(REAVER_HOUND_HIT);
                break;
            case IMPERIAL_MAGE:
                player.getPlaying().getGame().getAudioPlayer().playEffect(IMPERIAL_MAGE_HIT_SFX);
                break;
        } if (currentHealth <= 0 && enemyType != IMPERIAL_MAGE) {

            newState(DEAD);// Sets the state of enemy to dead (to play dying animation + sets it to not exist anymore)
            switch (enemyType) { // Play a death sound for the enemy
                case SLIME:
                    player.getPlaying().getGame().getAudioPlayer().playEffect(SLIME_KILLED);
                    break;
                case WOLF:
                    player.getPlaying().getGame().getAudioPlayer().playEffect(WOLF_KILLED);
                    break;
                case GOLEM:
                    player.getPlaying().getGame().getAudioPlayer().playEffect(GOLEM_KILLED);
                    break;
                case LIGHTSEEKER:
                    player.getPlaying().getGame().getAudioPlayer().playEffect(LIGHTSEEKER_KILLED);
                    break;
                case REAVER_HOUND:
                    player.getPlaying().getGame().getAudioPlayer().playEffect(REAVER_HOUND_KILLED);
                    break;
            }
        }
        else if (currentHealth <= 0) {
            newState(IMPERIAL_MAGE_DEAD); // Dedicated constant for imperial mage death state
            player.getPlaying().getGame().getAudioPlayer().playEffect(IMPERIAL_MAGE_KILLED_SFX); // play imperial mage death sound
        } else {
            if (enemyType != IMPERIAL_MAGE)
                newState(HIT); // Sets the state to just being hit and play animation + play hit sound
            else
                newState(IMPERIAL_MAGE_HIT); // Dedicated to the imperial due to differences in animation index placement in sprite sheet
        }
    }

    // Checks if enemy attack lands on player
    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if(attackBox.intersects(player.hitbox)) // Checks if player hitbox intersects with enemy attackbox
            if(player.getPlayerBuffs().isHasShadowflameArmor()) { // Checks if player has shadowflame armor buff (it helps to reduce damage taken)
                if (player.getOverhealHealth() > 0)
                    player.changeOverheal(-GetEnemyDmg(enemyType)); // If player has overheal health, only reduce the health here
                else
                    player.changeHealth(-(int) (GetEnemyDmg(enemyType) * 0.8)); // If player has shadowflame armor buff, reduce damage taken by 20%
                hurt((int) (GetEnemyDmg(enemyType) * 0.8f), player); // Reflect damage taken back to enemy
                System.out.println("Player reflected damage of " + (GetEnemyDmg(enemyType) * 0.2)); // Checks if buffs works
            } else
                player.changeHealth(-GetEnemyDmg(enemyType)); // If no buffs at all, player takes the full damage (reduce player health by 100% of damage of enemy)
        attackChecked = true; // Sets the attack checked to true to ensure that the player does not take damage again until the end and start of the attack animation (something like invincibility frames in games where it prevents players from taking damage infinitely)
    }

    // Method for ensuring that enemies are loaded and placed in the level correctly
    protected void firstUpdateCheck(int[][] lvlData) {
        if (firstUpdate) {
            if (!IsEntityOnFloor(hitbox, lvlData)) // Checks if enemy is on the floor
                inAir = true; // Sets to true which causes the game to ground the enemy to the stage
            firstUpdate = false; // Sets the firstUpdate variable to false to prevent further updates (Ensures the game doesn't lag due to unnecessary checks)
        }
    }

    // Ensures the enemy stays grounded to the level
    protected void updateInAir(int[][] lvlData) {
        if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed; // Changes the value of the position of the enemy's hitbox at the rate in accordance to the airSpeed of the enemy (explained in detail next line)
            airSpeed += GRAVITY; // Pulls the enemy down to the level by setting its airspeed to the gravity (basically settign the fall speed of the enemy)
        } else {
            inAir = false; // If the enemy already grounded, set the inAir variable to false since enemy is no longer in the air
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed); // Ensures that the enemy is only 1 pixel above the stage to prevent clipping
            tileY = (int) (hitbox.y / Game.TILES_SIZE); // Sets the tileY of the enemy according to the position of its hitbox and the vertical tile of the gane
        }
    }

    // Responsible for roaming and chasing of the enemy
    protected void move(int[][] lvlData) {
        float xSpeed; // Define the xSpeed variable

        if(walkDir == LEFT)
            xSpeed = -walkSpeed; // Set walkspeed to negative to walk from right to left (when facing left)
        else
            xSpeed = walkSpeed; // Set walkspeed to positive to walk from left to right (when facing right)

        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) // Checks if enemies can move to a specific area
            if(IsFloor(hitbox, xSpeed, lvlData)) { // Ensures that enemy is walking on solid floor
                hitbox.x += xSpeed;
                return;
            }

        changeWalkDir(); // Change the walk direction of the enemy based on some condition (e.g. Detecting players or reaching edge of tile)
    }

    // Method to invert walk directions
    protected void changeWalkDir() {
        if(walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    // Reset the state of an enemy in game to default values and positions in the stage
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive() {
        return active;
    }

    public int getEnemyState() {
        return state;
    }
}





// Most enemies (Except Imperial mage) have almost the same methods