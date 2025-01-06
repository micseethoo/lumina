package gamestates;

public enum Gamestate {

    PLAYING, MENU, OPTIONS, QUIT; // Various game states in the game

    public static Gamestate state = MENU; // Default game state to menu (On first startup)
}
