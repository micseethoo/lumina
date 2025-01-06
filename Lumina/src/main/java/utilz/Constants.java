package utilz;

import com.example.lumina.Game;

import java.awt.*;


// This class is responsible for storing all the constant values (value that does not change) that are to be obtained static-ly from other classes (e.g. animation and combat values)
public class Constants {

    public static final float GRAVITY = 0.04f* Game.SCALE; // Gravity value of the game
    public static int ANI_SPEED; // Animation speed (the transition speed per animation frame in the game)

    // Stores all the messages in the instruction window in the tutorial stage/level (Stage 0)
    public static class InstructionWindowConstants
    {
        public static final String[] instructionIntro = {
                "Hello Traveller, welcome to the tutorial!",
                "In this tutorial, basic mechanics about the game",
                "will be taught throughout this level. Without further",
                "ado, let's go!"};

        public static final Rectangle instructionAreaIntro = new Rectangle(0, 0,100,100);

        public static final String[] instructionMovement = {
                "To Move Left or Right", "Press 'A' to Move Left", "Press 'D' to Move Right "};

        public static final Rectangle instructionAreaMovement = new Rectangle(0,0,100,100);

        public static final Rectangle instructionAreaCrystal = new Rectangle((int)(650 * Game.SCALE), 0,50,900);

        public static final String[] instructionCrystal= {
                "This is a Crystal Fragment! ", "Walk past it to Collect!"};

        public static final Rectangle instructionAreaPotion= new Rectangle((int)(1030 * Game.SCALE), 0,50,900);

        public static final String[] instructionPotion= {
                "This is a Potion!", "Walk past the Potion to Gain 25HP! "};

        public static final Rectangle instructionAreaJump= new Rectangle((int)(1730 * Game.SCALE), 0,50,900);

        public static final String[] instructionJump= {
                "Oh no! There is a big Ledge!", "Use the SPACE bar to Jump over big Ledges!"};

        public static final Rectangle instructionAreaDash= new Rectangle((int)(2560 * Game.SCALE), (int)(-252 * Game.SCALE),50,900);

        public static final String[] instructionDash= {
                "Oh no! There is a big gap between the Ledges.", "Press RIGHT Mouse Button to Dash!"};

        public static final Rectangle instructionAreaAttack= new Rectangle((int)(3568 * Game.SCALE), (int)(-252 * Game.SCALE),50,900);

        public static final String[] instructionAttack= {
                "Look Out! There is a Slime", "Press LEFT Mouse Button to Attack the Slime!"};

    }

    public static class Projectiles {
        public static final int ARROW_DEFAULT_WIDTH = 18; // (Drawing purposes) Default width of the arrow from the image
        public static final int ARROW_DEFAULT_HEIGHT = 3; // (Drawing purposes) Default height of the arrow from the image

        public static final int ARROW_WIDTH = (int) (ARROW_DEFAULT_WIDTH * Game.SCALE); // Actual height of the arrow after scaling it
        public static final int ARROW_HEIGHT = (int) (ARROW_DEFAULT_HEIGHT * Game.SCALE); // Actual width of the arrow after scaling it
        public static final float ARROW_SPEED = 1 * Game.SCALE; // Speed of arrow
        public static final float DART_SPEED = 2 * Game.SCALE; // Speed of dart (shot by dart trap)

        public static final int FIRE_RATE = 440; // Player fire rate
    }

    public static class ObjectConstants {
        public static final int CRYSTAL_FRAGMENT = 120;
        public static final int RED_POTION = 1;
        public static final int MAGIC_TRAP = 10;
        public static final int MAGIC_POOL = 11;
        public static final int DARTTRAP_LEFT = 12;
        public static final int DARTTRAP_RIGHT = 13;
        public static final int PORTAL = 240;

        public static final int RED_POTION_VALUE = 150; // For testing purposes (Admin code, apply this to the applyPotionEffects method in ObjectManager class)


        // Size of portal
        public static final int PORTAL_WIDTH_DEFAULT = 32;
        public static final int PORTAL_HEIGHT_DEFAULT = 32;
        public static final int PORTAL_WIDTH = (int) (PORTAL_WIDTH_DEFAULT * Game.SCALE);
        public static final int PORTAL_HEIGHT = (int) (PORTAL_HEIGHT_DEFAULT * Game.SCALE);

        // Size of crystal fragment
        public static final int CRYSTAL_FRAGMENT_WIDTH_DEFAULT = 16;
        public static final int CRYSTAL_FRAGMENT_HEIGHT_DEFAULT = 16;
        public static final int CRYSTAL_FRAGMENT_WIDTH = (int) (CRYSTAL_FRAGMENT_WIDTH_DEFAULT * Game.SCALE);
        public static final int CRYSTAL_FRAGMENT_HEIGHT = (int) (CRYSTAL_FRAGMENT_HEIGHT_DEFAULT * Game.SCALE);


        // Size of potion
        public static final int POTION_WIDTH_DEFAULT = 16;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (POTION_WIDTH_DEFAULT * Game.SCALE);
        public static final int POTION_HEIGHT = (int) (POTION_HEIGHT_DEFAULT * Game.SCALE);

        // Size of magic trap
        public static final int MAGIC_SPIKE_WIDTH_DEFAULT = 36;
        public static final int MAGIC_SPIKE_HEIGHT_DEFAULT = 36;
        public static final int MAGIC_SPIKE_WIDTH = (int) (MAGIC_SPIKE_WIDTH_DEFAULT * Game.SCALE);
        public static final int MAGIC_SPIKE_HEIGHT = (int) (MAGIC_SPIKE_HEIGHT_DEFAULT * Game.SCALE);

        // Size of magic pool
        public static final int MAGIC_POOL_WIDTH_DEFAULT = 36;
        public static final int MAGIC_POOL_HEIGHT_DEFAULT = 36;
        public static final int MAGIC_POOL_WIDTH = (int) (MAGIC_POOL_WIDTH_DEFAULT * Game.SCALE);
        public static final int MAGIC_POOL_HEIGHT = (int) (MAGIC_POOL_HEIGHT_DEFAULT * Game.SCALE);

        // Size of dart trap
        public static final int DART_TRAP_WIDTH_DEFAULT = 36;
        public static final int DART_TRAP_HEIGHT_DEFAULT = 26;
        public static final int DART_TRAP_WIDTH = (int) (DART_TRAP_WIDTH_DEFAULT * Game.SCALE);
        public static final int DART_TRAP_HEIGHT = (int) (DART_TRAP_HEIGHT_DEFAULT * Game.SCALE);

        public static int GetSpriteAmount(int object_type) { // Getting the number of frames for each object in the game (Animation purposes)
            switch (object_type) {
                case RED_POTION:
                    return 15;
                case DARTTRAP_LEFT, DARTTRAP_RIGHT:
                    return 15;
                case CRYSTAL_FRAGMENT:
                    return 4;
                case MAGIC_TRAP:
                    return 25;
                case MAGIC_POOL:
                    return 4;
                case PORTAL:
                    return 6;
            }
            return 1;
        }
    }

    public static class EnemyConstants {
        // Values for how they are placed in the level (Refer to HelpMethods class for more info)
        public static final int SLIME = 10;
        public static final int WOLF = 220;
        public static final int GOLEM = 210;
        public static final int LIGHTSEEKER = 200;
        public static final int REAVER_HOUND = 120;
        public static final int IMPERIAL_MAGE = 150;

        // State of each enemy
        public static final int IDLE = 0;
        public static final int ATTACK = 1;
        public static final int RUNNING = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        // Stage of imperial mage only
        public static final int IMPERIAL_MAGE_IDLE = 0;
        public static final int IMPERIAL_MAGE_RUNNING = 1;
        public static final int IMPERIAL_MAGE_ATTACK = 2;
        public static final int IMPERIAL_MAGE_HIT = 5;
        public static final int IMPERIAL_MAGE_DEAD = 6;

        // Size of Slime Enemy
        public static final int SLIME_WIDTH_DEFAULT = 32;
        public static final int SLIME_HEIGHT_DEFAULT = 32;
        public static final int SLIME_WIDTH = (int) (SLIME_WIDTH_DEFAULT * Game.SCALE * 1.5f);
        public static final int SLIME_HEIGHT = (int) (SLIME_HEIGHT_DEFAULT * Game.SCALE * 1.5f);

        public static final int SLIME_DRAWOFFSET_X = (int) (8* Game.SCALE * 1.5f);
        public static final int SLIME_DRAWOFFSET_Y = (int) (22* Game.SCALE * 1.5f);


        // Size of Wolf Enemy
        public static final int WOLF_WIDTH_DEFAULT = 64;
        public static final int WOLF_HEIGHT_DEFAULT = 32;
        public static final int WOLF_WIDTH = (int) (WOLF_WIDTH_DEFAULT * Game.SCALE);
        public static final int WOLF_HEIGHT = (int) (WOLF_HEIGHT_DEFAULT * Game.SCALE);

        public static final int WOLF_DRAWOFFSET_X = (int) (7* Game.SCALE);
        public static final int WOLF_DRAWOFFSET_Y = (int) (5* Game.SCALE);


        // Size of Golem Enemy
        public static final int GOLEM_WIDTH_DEFAULT = 32;
        public static final int GOLEM_HEIGHT_DEFAULT = 32;
        public static final int GOLEM_WIDTH = (int) (GOLEM_WIDTH_DEFAULT * Game.SCALE * 1.5f);
        public static final int GOLEM_HEIGHT = (int) (GOLEM_HEIGHT_DEFAULT * Game.SCALE * 1.5f);

        public static final int GOLEM_DRAWOFFSET_X = (int) (11* Game.SCALE);
        public static final int GOLEM_DRAWOFFSET_Y = (int) (16* Game.SCALE);


        // Size of Reaver Hound Enemy
        public static final int REAVER_HOUND_WIDTH_DEFAULT = 64;
        public static final int REAVER_HOUND_HEIGHT_DEFAULT = 64;
        public static final int REAVER_HOUND_WIDTH = (int) (REAVER_HOUND_WIDTH_DEFAULT * Game.SCALE);
        public static final int REAVER_HOUND_HEIGHT = (int) (REAVER_HOUND_HEIGHT_DEFAULT * Game.SCALE);

        public static final int REAVER_HOUND_DRAWOFFSET_X = (int) (11* Game.SCALE);
        public static final int REAVER_HOUND_DRAWOFFSET_Y = (int) (30* Game.SCALE);


        // Size of Lightseeker Enemy
        public static final int LIGHTSEEKER_WIDTH_DEFAULT = 64;
        public static final int LIGHTSEEKER_HEIGHT_DEFAULT = 64;
        public static final int LIGHTSEEKER_WIDTH = (int) (LIGHTSEEKER_WIDTH_DEFAULT * Game.SCALE * 2);
        public static final int LIGHTSEEKER_HEIGHT = (int) (LIGHTSEEKER_HEIGHT_DEFAULT * Game.SCALE * 2);

        public static final int LIGHTSEEKER_DRAWOFFSET_X = (int) (30* Game.SCALE);
        public static final int LIGHTSEEKER_DRAWOFFSET_Y = (int) (65* Game.SCALE);


        // Size of Imperial Mage Enemy
        public static final int IMPERIAL_MAGE_WIDTH_DEFAULT = 64;
        public static final int IMPERIAL_MAGE_HEIGHT_DEFAULT = 64;
        public static final int IMPERIAL_MAGE_WIDTH = (int) (IMPERIAL_MAGE_WIDTH_DEFAULT * Game.SCALE * 0.9f);
        public static final int IMPERIAL_MAGE_HEIGHT = (int) (IMPERIAL_MAGE_HEIGHT_DEFAULT * Game.SCALE * 0.85f);

        public static final int IMPERIAL_MAGE_DRAWOFFSET_X = (int) (45 * Game.SCALE);
        public static final int IMPERIAL_MAGE_DRAWOFFSET_Y = (int) (65 * Game.SCALE);

        public static int GetSpriteAmount(int enemy_type, int enemy_state) { // Number of animation frames for each enemy (Animation purposes)
            switch(enemy_type) {
                case SLIME:
                    switch (enemy_state) {
                        case IDLE:
                            return 1;
                        case RUNNING:
                            return 6;
                        case DEAD:
                            return 6;
                        case ATTACK:
                            return 7;
                        case HIT:
                            return 1;
                    }
                case LIGHTSEEKER:
                    switch (enemy_state) {
                        case IDLE:
                            return 6;
                        case RUNNING:
                            return 6;
                        case DEAD:
                            return 9;
                        case ATTACK:
                            return 12;
                        case HIT:
                            return 5;
                    }
                case WOLF:
                    switch (enemy_state) {
                        case IDLE:
                            return 4;
                        case RUNNING:
                            return 6;
                        case DEAD:
                            return 8;
                        case ATTACK:
                            return 7;
                        case HIT:
                            return 4;
                    }
                case GOLEM:
                    switch (enemy_state) {
                        case IDLE:
                            return 4;
                        case RUNNING:
                            return 4;
                        case DEAD:
                            return 9;
                        case ATTACK:
                            return 11;
                        case HIT:
                            return 5;
                    }
                case REAVER_HOUND:
                    switch(enemy_state) {
                        case IDLE:
                            return 6;
                        case RUNNING:
                            return 5;
                        case DEAD:
                            return 7;
                        case ATTACK:
                            return 5;
                        case HIT:
                            return 4;
                    }
                case IMPERIAL_MAGE:
                    switch(enemy_state) {
                        case IMPERIAL_MAGE_IDLE:
                        case IMPERIAL_MAGE_RUNNING:
                            return 8;
                        case IMPERIAL_MAGE_ATTACK:
                            return 11;
                        case IMPERIAL_MAGE_HIT:
                            return 5;
                        case IMPERIAL_MAGE_DEAD:
                            return 8;
                    }
            }
            return 5;
        }

        // Animation Speed for each enemy
        public static int GetAniSpeed(int enemy_type) {
            switch(enemy_type) {
                case IMPERIAL_MAGE:
                    return 360;
                default:
                    return 120;
            }
        }

        // Max health for each enemy
        public static int GetMaxHealth(int enemy_type) {
            switch(enemy_type) {
                case SLIME:
                    return 60;
                case WOLF:
                    return 150;
                case GOLEM:
                    return 500;
                case LIGHTSEEKER:
                    return 200;
                case REAVER_HOUND:
                    return 200;
                case IMPERIAL_MAGE:
                    return 120;
                default:
                    return 0;
            }
        }

        // Damage for each enemy
        public static int GetEnemyDmg(int enemy_type) {
            switch(enemy_type) {
                case SLIME:
                    return 15;
                case WOLF:
                    return 30;
                case GOLEM:
                    return 70;
                case LIGHTSEEKER:
                    return 50;
                case REAVER_HOUND:
                    return 40;
                case IMPERIAL_MAGE:
                    return 1000;
                default:
                    return 0;
            }
        }
    }

    public static class UI{
        public static class Buttons {

            // Default Button Size
            public static final int B_WIDTH_DEFAULT = 188;
            public static final int B_HEIGHT_DEFAULT = 20;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);

            // Pause Screen Button Size
            public static final int P_WIDTH_DEFAULT = 325;
            public static final double P_HEIGHT_DEFAULT = 58.5;
            public static final int P_WIDTH = (int) (P_WIDTH_DEFAULT * Game.SCALE);
            public static final int P_HEIGHT = (int) (P_HEIGHT_DEFAULT * Game.SCALE);
        }


        // Size of Pause Buttons (Remove later)
        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE * 0.75f);
        }


        // Size of UI Buttons
        public static class UIButtons {
            public static final int UIBUTTON_SIZE_DEFAULT = 56;
            public static final int UIBUTTON_SIZE = (int) (UIBUTTON_SIZE_DEFAULT * Game.SCALE * 1.5f);
        }


        // Size of Back Buttons
        public static class BackButtons {
            public static final int BACK_BUTTON_DEFAULT_WIDTH = 53;
            public static final int BACK_BUTTON_WIDTH = (int) (BACK_BUTTON_DEFAULT_WIDTH * Game.SCALE * 1.1f);
            public static final int BACK_BUTTON_DEFAULT_HEIGHT = 19;
            public static final int BACK_BUTTON_HEIGHT = (int) (BACK_BUTTON_DEFAULT_HEIGHT * Game.SCALE * 1.1f);

            public static final int YES_BUTTON_DEFAULT_WIDTH = 29;
            public static final int YES_BUTTON_WIDTH = (int) (YES_BUTTON_DEFAULT_WIDTH * Game.SCALE * 4.5f);
            public static final int YES_BUTTON_DEFAULT_HEIGHT = 11;
            public static final int YES_BUTTON_HEIGHT = (int) (YES_BUTTON_DEFAULT_HEIGHT * Game.SCALE * 4.5f);
            public static final int NO_BUTTON_DEFAULT_WIDTH = 29;
            public static final int NO_BUTTON_WIDTH = (int) (NO_BUTTON_DEFAULT_WIDTH * Game.SCALE * 4.5f);
            public static final int NO_BUTTON_DEFAULT_HEIGHT = 11;
            public static final int NO_BUTTON_HEIGHT = (int) (NO_BUTTON_DEFAULT_HEIGHT * Game.SCALE * 4.5f);
        }


        // Size of Volume Buttons
        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE * 0.75f);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE * 0.75f);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE * 0.6f);
        }


        // Size of In game UI elements
        public static class InGameUIElements {
            public static final int STATIC_CRYSTAL_FRAGMENT_DEFAULT_WIDTH = 26;
            public static final int STATIC_CRYSTAL_FRAGMENT_DEFAULT_HEIGHT = 22;
            public static final int STATIC_CRYSTAL_FRAGMENT_WIDTH = (int) (STATIC_CRYSTAL_FRAGMENT_DEFAULT_WIDTH * Game.SCALE);
            public static final int STATIC_CRYSTAL_FRAGMENT_HEIGHT = (int) (STATIC_CRYSTAL_FRAGMENT_DEFAULT_HEIGHT * Game.SCALE);
        }

        // Size of Buff Icons in game
        public static class BuffIcons {
            public static final int BUFF_ICONS_DEFAULT_WIDTH = 32;
            public static final int BUFF_ICONS_DEFAULT_HEIGHT = 32;
            public static final int BUFF_ICONS_WIDTH = (int) (BUFF_ICONS_DEFAULT_WIDTH * Game.SCALE);
            public static final int BUFF_ICONS_HEIGHT = (int) (BUFF_ICONS_DEFAULT_HEIGHT * Game.SCALE);
        }
    }

    // Directional static values
    public static class Directions {
        public static final int LEFT = 0;
        public static final int RIGHT = 2;
    }


    public static class PlayerConstants {
        // Int values for each player state
        public static final int RUNNING = 0;
        public static final int DEAD = 1;
        public static final int IDLE = 5;
        public static final int JUMP = 7;
        public static final int DOUBLE_JUMP = 6;
        public static final int ATTACK = 3;

        // Number of animation frames of each state of the player
        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case RUNNING:
                case DEAD:
                    return 8;
                case ATTACK:
                    return 7;
                case IDLE:
                case DOUBLE_JUMP:
                    return 4;
                case JUMP:
                    return 2;
                default:
                    return 1;
            }
        }
    }
}
