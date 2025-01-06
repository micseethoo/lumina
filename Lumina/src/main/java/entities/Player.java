package entities;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static audio.AudioPlayer.*;
import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.PlayerConstants.JUMP;
import static utilz.HelpMethods.*;

import audio.AudioPlayer;
import com.example.lumina.Game;
import gamestates.Playing;
import objects.ObjectManager;
import save.SaveCrystalProgress;
import ui.InstructionWindow;
import utilz.Constants;
import utilz.LoadSave;
import utilz.PlayerBuffs;

public class Player extends Entity {

    // Arraylist for storing all player animatiosn for each player state
    private BufferedImage[][] animations;

    // Boolean variables for player directions (both vertical and horizontal)
    private boolean moving = false, attacking = false;
    private boolean facingLeft = false;
    private boolean left, right, jump;

    // Variable for getting player spawn from level data (per level)
    private int[][] lvlData;

    // Draw offset variable from the empty space in the sprite sheet
    private float xDrawOffset = 17 * Game.SCALE;
    private float yDrawOffset = 15 * Game.SCALE;

    // Gravity Variables
    private float jumpSpeed = -2.5f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    // Boolean variables for double jump buff
    private boolean doubleJump;
    private boolean alreadyDoubleJump;

    // Variables for overheal buff
    private int excessHealing;
    private int overhealHealth = 0;
    private int maxOverhealHealth = 50;

    // Status Bar + Crystal Counter GUI Variables
    private BufferedImage statusBarImg;
    private BufferedImage crystalFragmentImg;
    private BufferedImage crystalCounterBg;

    private int statusBarWidth = (int) (194 * Game.SCALE);
    private int statusBarHeight = (int) (61 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (5 * Game.SCALE);

    // Health Bar UI Variables
    private int healthBarWidth = (int) (143 * Game.SCALE);
    private int healthBarHeight = (int) (9 * Game.SCALE);
    private int healthBarX = (int) (38 * Game.SCALE);
    private int healthBarY = (int) (26 * Game.SCALE);
    private int healthWidth;

    // Overheal Health UI Variables
    private int overhealBarWidth = (int) (72 * Game.SCALE);
    private int overhealBarHeight = (int) (9 * Game.SCALE);
    private int overhealBarX = (int) (38 * Game.SCALE);
    private int overhealBarY = (int) (26 * Game.SCALE);
    private int overhealHealthWidth;

    // Stamina UI + Values Variables
    private int staminaBarWidth = (int) (131 * Game.SCALE);
    private int staminaBarHeight = (int) (6 * Game.SCALE);
    private int staminaBarX = (int) (50 * Game.SCALE);
    private int staminaBarY = (int) (38 * Game.SCALE);
    private int staminaWidth = staminaBarWidth;
    private float staminaMaxValue = 200;
    private float staminaValue = staminaMaxValue;

    // Initialised Class Instance for reference/access to specific methods found there
    private ObjectManager objectManager;
    private PlayerBuffs playerBuffs;
    private Playing playing;



    // Used for player directional flipping
    private int flipX = 0;
    private int flipW = 1;

    // Variables - For attacking check purposes
    private boolean attackChecked;

    // Variable for which tile the player is standing on
    private int tileY = 0;

    // Variables for dash
    private boolean dashActive;
    private int dashTick;

    // Variables for player stamina
    private int staminaRegenSpeed = 15;
    private int staminaRegenTick;

    // Variables for player buff (invisibility)
    private boolean invisibility;



    // private int numCrystalFragments = 30; // Dev test code
    private int numCrystalFragments;
    private int crystalCount;

    // Variables for portal message
    private boolean showPortalMessage = false;
    private long messageStartTime = 0;
    private boolean portalMessageAlreadyShown = false;


    // Player constructor
    public Player(float x, float y, int width, int height, Playing playing, ObjectManager objectManager) {
        super(x,y,width,height);
        this.playing = playing;
        this.objectManager = objectManager;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = 1.0f * Game.SCALE;
        this.numCrystalFragments = SaveCrystalProgress.loadCrystalProgress();

        loadAnimation();
        initHitbox( 26, 33, 0);

        this.playerBuffs = new PlayerBuffs(this);
    }

    // Set the spawn location of the player
    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    // Updates everything pertaining to the states, resources (health and stamina), animations, and attacks of the player
    public void update() {

        if (inAir)
            System.out.println("In air"); // Checks if player is in air
        playerBuffs.updateBuffs(); // Updates the player buffs after collecting certain amount of crystal fragments
        updateHealthBar(); // Update health bar of the player (visual)
        if (playerBuffs.isHasOverheal())
            updateOverhealHealthBar(); // Update overheal health bar of the player (visual) - When they have the relevant buff

        updateStaminaBar(); // Update stamina bar of the player (visual)

        if (currentHealth <= 0) {
            if(state != DEAD) {
                state = DEAD; // Sets player state to death when hp reaches 0
                aniTick = 0; // Resets animation tick to play death animation
                aniIndex = 0;
                playing.setPlayerDying(true); // Sets dying to true

                // Checks if player dies during tutorial stage to reset player spawn on the previous section before they die instead of placing them at the start of the tutorial
                if (playing.getLevelManager().getLevelIndex() == 0) {
                    if(playing.getInstructionIndex() == 3) {
                        Point setTutSpawn = new Point(Constants.InstructionWindowConstants.instructionAreaJump.x, Constants.InstructionWindowConstants.instructionAreaJump.y);
                        setSpawn(setTutSpawn);
                        InstructionWindow.setjumpWindowIndex(true);

                    }

                    else if(playing.getInstructionIndex() == 4) {
                        Point setTutSpawn = new Point(Constants.InstructionWindowConstants.instructionAreaDash.x, Constants.InstructionWindowConstants.instructionAreaDash.y);
                        setSpawn(setTutSpawn);
                        InstructionWindow.setDashWindowIndex(true);

                    }

                    else if(playing.getInstructionIndex() == 5) {
                        Point setTutSpawn = new Point(Constants.InstructionWindowConstants.instructionAreaAttack.x, Constants.InstructionWindowConstants.instructionAreaAttack.y);
                        setSpawn(setTutSpawn);
                        InstructionWindow.setAttackWindowIndex(true);

                    }


                }

                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE); // Play death sound of player

            } else if (aniIndex == GetSpriteAmount(DEAD) - 1) { // Offset aniIndex by 1 (due to how Java starts at 0, instead of 1)
                playing.setGameOver(true); // Set game over condition to true
                playing.getGame().getAudioPlayer().stopSong(); // Stop all songs currently being played
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER); // Play the game over effect sound
            } else
                updateAnimationTick(); // (If not dead) Else keep updating the animation of the player depending on player state (e.g. if player running, keep playing the running animation unless player changes states (e.g. to jump state to switch to jump animation instead))

            return;
        }

        updatePos(); // Update position of the player (Both vertical and horizontal direction) in real time (Based on the UPS of the game)
        if(moving) {
            tileY = (int) (hitbox.y / Game.TILES_SIZE); // Get and set the current tile the player is standing on
            if(dashActive) {
                dashTick++; // Increment based on UPS
                if(dashTick >= 35) { // The time it takes for the dash action to be completed - Mainly for dash animation (35 ticks or updates)
                    dashTick = 0; // Sets it back to 0
                    dashActive = false; // Sets dash to inactive
                }
            }
        }
        if(attacking) // Checks if the player wants to attack
            checkAttack(); // For checking whether can the player attack (shoot an arrow)


        checkPotionTouched(); // Checks if player touches health potion
        checkTrapsTouched(); // Checks if player touched traps

        updateAnimationTick();
        setAnimation(); // Sets the animation of the player base on player state

        if (invisibility) { // Checks if invisibility is enabled
            if (staminaValue <= 0) { // When there is no more stamina or when it is too low
                playing.getGame().getAudioPlayer().playEffect(INVI_OFF); // Play a sound to indicate invisibility is turned off when not enough stamina
                invisibility = false; // Sets the invisibility boolean to false (Enemies and certain traps can now detect player)
            } else {
                changeStamina(-0.25f); // Reduces player at fixed rate per second
                System.out.println("Invisibility Enabled"); // (For testing) Checks if invisibility is enabled for stamina reduction
            }
        }
    }

    // Checks if player has touched a trap in game (level/stage)
    private void checkTrapsTouched() {
        playing.checkTrapsTouched(this); // Passes in the player's hitbox to check intersection with trap hitbox
    }

    // Checks if player has collected health potion in game (level/stage)
    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox); // Passes in the player's hitbox to check intersection with potion hitbox
    }

    // Method for checking whether the player has attacked
    private void checkAttack() {
        if(attackChecked || aniIndex != 6) // Shoot an arrow at index 6 of the attacking animation
            return;
        attacking = true; // Set the attacking boolean to true to
        objectManager.shootArrows(this);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    // Method to update the health bar (UI)
    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    // Method to update the overheal health bar (UI)
    private void updateOverhealHealthBar() {
        overhealHealthWidth = (int) ((overhealHealth / (float) maxOverhealHealth) * overhealBarWidth);
    }

    // Method to update the stamina of the player
    private void updateStaminaBar() {
        staminaWidth = (int) ((staminaValue / staminaMaxValue) * staminaBarWidth); // Update the stamina bar UI

        staminaRegenTick++; // Increment the stamina value
        if(staminaRegenTick >= staminaRegenSpeed) {
            staminaRegenTick = 0; // Resets the tick back to 0
            changeStamina(1); // Increases the value of the stamina of the player by 1 per stamina regen tick defined (15 ticks)
        }
    }

    public void render(Graphics g, int xlvlOffset, int ylvlOffset) {
        // Set the font size of the message (drawMessage() method)
        playing.getGame().setCustomFont(g, 12f * Game.SCALE);


        Graphics2D g2d = (Graphics2D) g;
        AlphaComposite originalComposite = (AlphaComposite) g2d.getComposite(); // Initialising the variable for setting opacity of the image
        float alpha = 0.5f; // Variable to set the transparency of the player to 50% (when they have invisibility buff enabled)

        if (invisibility) { // Checks if the player is invisible
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); // Set the opacity of the image drawn for the play below to 50% - Visual for invisibility buff)
            g2d.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset - xlvlOffset + flipX), (int) (hitbox.y - yDrawOffset - ylvlOffset), width * flipW, height, null);
            g2d.setComposite(originalComposite);
        } else // Render the image normally
            g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset - xlvlOffset + flipX), (int) (hitbox.y - yDrawOffset - ylvlOffset), width * flipW, height, null);




//        drawHitbox(g, xlvlOffset, ylvlOffset); // (Testing only) Draw hitbox of the player

        drawUI(g); // Draw the UI of the player that shows the health and stamina of the player
        playerBuffs.drawBuffs(g); // Draw the buff icons obtained by the player after collecting enough crystal fragments
    }

    private void drawUI(Graphics g) {

        // Background for health and stamina bar
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        // Health Bar
        g.setColor(Color.red);
        g.fillRect(healthBarX + statusBarX, healthBarY + statusBarY, healthWidth, healthBarHeight);

        //Overheal Health Bar
        g.setColor(new Color(128, 0, 128));
        g.fillRect(overhealBarX + statusBarX, overhealBarY + statusBarY, overhealHealthWidth, overhealBarHeight);

        // Stamina Bar
        g.setColor(Color.yellow);
        g.fillRect(staminaBarX + statusBarX, staminaBarY + statusBarY, staminaWidth, staminaBarHeight);


        // Crystal Counter
        g.setColor(Color.white);
        g.drawImage(crystalCounterBg, (int) (210 * Game.SCALE), (int)(5* Game.SCALE), (int) (130 * Game.SCALE),(int) (60 * Game.SCALE), null);
        g.drawImage(crystalFragmentImg, (int) (228 * Game.SCALE), (int)(23* Game.SCALE), Constants.UI.InGameUIElements.STATIC_CRYSTAL_FRAGMENT_WIDTH, Constants.UI.InGameUIElements.STATIC_CRYSTAL_FRAGMENT_HEIGHT, null);
        g.drawString( crystalCount + "/" + playing.getLevelManager().getCurrentLevel().getCrystals().size(),(int) (260 * Game.SCALE),(int)(33* Game.SCALE));
        g.drawString(String.valueOf(getNumCrystalFragments()), (int) (275 * Game.SCALE), (int)(50* Game.SCALE));

        // For showing in game messages delivered to players (currently only for portal text)
        drawMessage(g);
    }

    public int getCrystalCounter() {
        return crystalCount;
    }


    public void drawMessage(Graphics g) {
        if (crystalCount == playing.getLevelManager().getCurrentLevel().getCrystals().size() && !showPortalMessage) {
            showPortalMessage = true; // Make the showing portal message condition to true
            messageStartTime = System.currentTimeMillis(); // Record the start time of message
        }

        // Draw the portal message if the condition is met and it is within the 5 seconds window
        if (showPortalMessage) {
            // Calculate the elapsed time since the message started displaying
            long elapsedTime = System.currentTimeMillis() - messageStartTime;

            // If 5 seconds have passed, hide the message
            if (elapsedTime >= 5000) {
                showPortalMessage = false; // Ensure that the message is not played randomly
                portalMessageAlreadyShown = true; // To indicate that the portal message has already been shown
            } else if (!portalMessageAlreadyShown) {
                // Calculate the alpha value based on elapsed time to fade out gradually
                int alpha = (int) (255 - (255 * (elapsedTime / 5000.0))); // Alpha value that gradually decreases based on miliseconds passed
                g.setColor(new Color(255, 165, 0, alpha)); // Orange color with calculated alpha
                g.drawString("A portal has appeared", 100, 800); // The message
            }
        }
    }

    // Method used to increase/decrease player health
    public void changeHealth(int value) {
        excessHealing = value - (maxHealth - currentHealth); // Converting excess healing into overheal health (when value of health after healing exceeds player maximum health)
        currentHealth += value; // Increase/Decrease player health based on certain conditions

        if(currentHealth <= 0) {
            currentHealth = 0; // Sets health to 0 if damage taken is more than maximum health/attack damage received more than current health
        } else if (currentHealth >= maxHealth) {
            if (playerBuffs.isHasOverheal()) { // Checks if player has overheal buff
                overhealHealth += excessHealing; // Increase overheal health based on excess healing
                if (overhealHealth >= 50)
                    overhealHealth = maxOverhealHealth; // Caps the overheal health
            }

            currentHealth = maxHealth; // Caps the normal maximum health
        }
    }

    // Method to change the overheal health of player (when player has the overheal buff)
    public void changeOverheal(int value) {
        overhealHealth += value; // Increase the overheal health of the player based on the value of variable 'value' passed
        int outstandingHealth; // Initialise this variable to prevent overheal from absorbing life-threatening amounts of damage (refer to comment below)

        if(overhealHealth <= 0) {
            outstandingHealth = value - overhealHealth;
            overhealHealth = 0;
            changeHealth(outstandingHealth); // Since only the overheal bar is depleted even though the damage may be able to one shot the player, we carry over the remaining health outstanding by taking the damage minus the remaining overheal amount then reducing the player's health based on the outstanding damage
        }
    }

    public void kill() {
        currentHealth = 0;
    }

    // Method to change (decrease) the stamina value of the player based on certain actions
    public void changeStamina(float value) {
        staminaValue += value; // Stamina regeneration
        if(staminaValue >= staminaMaxValue)
            staminaValue = staminaMaxValue; // Caps the stamina amount after reaching more than the intended amount
        else if (staminaValue <= 0)
            staminaValue = 0; // Sets the stamina to 0 when the stamina reduction goes below 0
    }

    // Increases the value of the counters in game for crystals whenever a crystal is picked up
    public void addCrystalCount() {
        numCrystalFragments++; // Increases the number of crystals collected counter (stage-wide)
        crystalCount++; // Increases the number of crystals collected counter (all game-wide (all 3 stages))

        setNumCrystalFragments(numCrystalFragments); // Updates the number of crystals collected after iterating
        playerBuffs.updateBuffs();
    }

    // For testing purposes (prints out whenever a crystal is being picked up)
    public void displayCrystalCount() {
        System.out.println("A new crystal fragment has been collected, now you have: " + numCrystalFragments);
    }

    private void updatePos() {

        moving = false; // Player is not moving on default

        // Vertical movement check (If player has double jump buff or single jump only)
        if(jump)
            if (playerBuffs.isHasDoubleJump())
                doubleJump();
            else
                jump();


        // Conditions where the player isn't moving
        if(!inAir)
            if(!dashActive)
                if(!left && !right || (right && left))
                    return;


        float xSpeed = 0;
        if (playerBuffs.isHasIncreasedMovementSpeed()) // Increase horizontal speed of player when they have the relevatn buffs
            walkSpeed = 1.2f * Game.SCALE;


        // Set the player speed and image direction depending on player direction
        if (left) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }


        // Dash
        if(dashActive) {
            if(!left && !right) {
                if(facingLeft)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;
            }

            xSpeed *=3; // Move forward x3 their horizontal speed
            playing.getGame().getAudioPlayer().playEffect(DASH); // PLay dash sound effect
        }

        // Check if player is in the air
        if(!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        // Check if player is colliding with terrain above
        if (inAir && !dashActive) {
            if(CanMoveHere(hitbox.x, hitbox.y +airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed>0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
            }
        }

        updateXPos(xSpeed); // Method to change X position of player when player moves
        moving = true;

    }

    // Method for allowing player to jump (normal jump, before player gets the double jump buff)
    private void jump() {
        if(inAir)
            return;
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }


    // Method for double jump buff
    private void doubleJump() {
        // On ground
        if (!inAir) {
            airSpeed = jumpSpeed;
            inAir = true;
        }

        // While in the air
        if (doubleJump) {
            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
            airSpeed = jumpSpeed + (int) (0.7 * Game.SCALE);
            alreadyDoubleJump = true;
            doubleJump = false;

        }

    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;

        //Reset while on the ground
        if (!inAir)
            alreadyDoubleJump = false;
    }

    public void setJump(boolean jumping) {

        //When key released jump = false
        //While still inAir
        //if the player has already performed the double jump
        jump = jumping;

        // Checks conditions that allow double jump
        if (!jump && inAir && !alreadyDoubleJump) {
            doubleJump = true;
        }

        // Ensures that the second jump is not applied after jumping only once and landing
        if (!inAir)
            doubleJump = false;
    }


    // Update the horizontal (X) position of the player and checks if player is colliding with wall beside
    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            if(dashActive) {
                dashActive = false;
                dashTick = 0;
            }
        }
    }

    // Sets the different animations for each state the player is in
    // e.g. moving to attack
    // resets the animation frame when a new action is performed (new state is engaged)
    private void setAnimation() {

        int startAni = state; // Initialises new variable that stores the player state

        if(moving) {
            state = RUNNING; // Change state of player to RUNNING when player is moving (for animation)
        }
        else {
            state = IDLE; // Change state of player when player is not doing anything (for animation)
        }

        if(inAir) {
            state = JUMP; // Changes state of player when they are in the air (for animation)
        }

        if(alreadyDoubleJump) {
            state = DOUBLE_JUMP; // Changes state of player when they double jump (for animation)
        }

        if(dashActive) {
            state = JUMP; // Changes state of player when they dashes (animation shared with jumping animation)
            aniIndex = 1; // Starts from the second frame of the jumping animation
            aniTick = 0; // Start from the first animation tick
            return;
        }

        if(attacking) {
                state = ATTACK; // Changes state of player to when they are attacking (for animation)
                if (startAni != ATTACK) {
                    aniIndex = 1; // Start from the second frame of the attacking animation
                    aniTick = 0; // Restart from the start of the animation
                    return;
                }
        }

        if(startAni != state) { // If player is not in any of the states, then restart from the default state
            resetAniTick(); // Reset the animation ticks and frame back to the beginning (Restart animation)
        }
    }

    // Refreshes the ticks of an animation when player changes state
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    // Updates the animation of the player depending on the state of the player
    private void updateAnimationTick() {
        ANI_SPEED = 120/GetSpriteAmount(state); // Sets the animation speed depending on the number of frames the animation has

        aniTick++; // Increase the animation tick (Based on UPS of game)
        if (aniTick >= ANI_SPEED) { // If animation tick is equal or more than the animation tick being iterated, switch the animation frame of the state the player is in
            aniTick = 0;
            aniIndex++; // Switch to next animation frame being shown
            if (aniIndex >= GetSpriteAmount(state)) { // Reset the animation from the first frame after finishing the animation
                aniIndex = 0;
                attacking = false; // (When player is in attacking state) Sets attacking to false to prevent more arrows being shot afterwards
                attackChecked = false; // (When
            }
        }
    }


    // Load all animation into a 2D buffered image array from player sprite sheet
    private void loadAnimation() {

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS); // Load the player sprite sheet into a buffered image variabl, named 'img'

        animations = new BufferedImage[8][8]; // Create a new 2D array list
        for (int j = 0; j <animations.length; j++) // Iterate through sprite sheet in the horizontal direction (animation frames)
            for (int i = 0; i <animations[j].length; i++) // Iterate through sprite sheet in the vertical direction (animation states - types of animations based on state)
                animations[j][i] = img.getSubimage( i * 64, j * 64, 64, 64); // Extract each animation frame from each state (64x64 pixel image per frame)

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR); // Get status bar background from resources image folder
        crystalFragmentImg = LoadSave.GetSpriteAtlas(LoadSave.STATIC_CRYSTAL); // Get crystal fragment image from resources image folder
        crystalCounterBg = LoadSave.GetSpriteAtlas(LoadSave.CRYSTAL_COUNTER_BORDER); // Get Crystal counter background/border from resources image folder
    }

    // Gets the coordinates of the player spawn per stage
    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }

    // Resets all directional booleans of players to default value (false on default)
    public void resetDirBooleans() {
        left = false;
        right = false;
        jump = false;
    }

    // Sets the attacking state to true when player initiates an attack
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    // Checks if player is facing left
    public boolean isLeft() {
        return facingLeft;
    }

    // Sets the player moving direction to left (allow player to move left)
    public void setLeft(boolean left) {
        this.left = left;
        if (left) {
            facingLeft = true;  // Update facing direction
        }
    }

    // Checks if player is facing right
    public boolean isRight() {
        return right;
    }

    // Sets the player moving direction to right (allow player to move right)
    public void setRight(boolean right) {
        this.right = right;
        if (right) {
            facingLeft = false;  // Update facing direction to false due to no longer facing left
        }
    }

    // Resets all player values back to default values
    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;
        overhealHealth = 0;
        staminaValue = staminaMaxValue;

        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;


        numCrystalFragments = numCrystalFragments - crystalCount; // Total count reduced to original amount before entering stage
        crystalCount = 0; // Reset current stage count back to 0

        invisibility = false; // Sets player invisibility to 0

        // Do not show portal message anymore if there is any
        showPortalMessage = false;
        portalMessageAlreadyShown = false;
    }

    // Allows other classes the get the number of crystals the player has collected in a specific stage (Primarily for checking conditions that allow player to go to next level)
    // Allows other classes the get the number of crystals the player in total
    public int getNumCrystalFragments() {
        return numCrystalFragments;
    }

    // Updates the number of crystals the player has
    public void setNumCrystalFragments(int numCrystalFragments) {
        this.numCrystalFragments = numCrystalFragments;
    }

    public int getTileY() {
        return tileY;
    }

    // Method for dash mechanic of player
    public void dash() {
        if(dashActive)
            return; // Ensures that dash is not repeated infinitely
        if(staminaValue >= 50) {
            dashActive = true; // Sets dash to active (true), allowing player to dash through relevant methods above for movement
            changeStamina(-50); // Reduces stamina by 50 after a dash
        }
    }

    // Getter for player buffs to check if player has a specific buff
    public PlayerBuffs getPlayerBuffs() {
        return playerBuffs;
    }

    // Allows players to toggle invisibility buff
    public void toggleInvisibility() {
        if(playerBuffs.isHasInvisibility()) { // Checks if player has invisibility buff
            invisibility = !invisibility; // Toggle invisibility buff (if no buff, then enable it; vice versa)
            if (invisibility) {
                playing.getGame().getAudioPlayer().playEffect(INVI_ON); // Play an audio cue for enabling invisibility buff
                changeStamina(-20); // Reduce stamina each time buff is toggled to prevent spamming
            } else
                playing.getGame().getAudioPlayer().playEffect(INVI_OFF); // Play an audio cue for disabling invisibility buff
        }
    }

    // Checks if invisibility is turned on
    public boolean isInvisible() {
        return invisibility;
    }

    // Gets the amount of overheal health the player has
    public int getOverhealHealth() {
        return overhealHealth;
    }

    // Gets the playing instance via the player class
    public Playing getPlaying() {
        return playing;
    }
}
