//package audio;
//
//import javax.sound.sampled.*;
//import java.io.IOException;
//import java.net.URL;
//import java.util.Random;
//
//public class AudioPlayer {
//
//    public static int MENU_1 = 0;
//    public static int LEVEL_1 = 1;
//    public static int LEVEL_2 = 2;
//
//    public static int DIE = 0;
//    public static int JUMP = 1;
//    public static int GAMEOVER = 2;
//    public static int LVL_COMPLETED = 3;
//    public static int SHOOT = 4;
//    public static int DASH = 5;
//    public static int PORTAL = 6;
//
//    public static int SLIME_HIT = 7;
//    public static int SLIME_KILLED = 8;
//    public static int WOLF_HIT = 9;
//    public static int WOLF_KILLED = 10;
//    public static int GOLEM_HIT = 11;
//    public static int GOLEM_KILLED = 12;
//    public static int LIGHTSEEKER_HIT = 13;
//    public static int LIGHTSEEKER_KILLED = 14;
//    public static int REAVER_HOUND_HIT = 15;
//    public static int REAVER_HOUND_KILLED = 16;
//    public static int IMPERIAL_MAGE_HIT_SFX = 17;
//    public static int IMPERIAL_MAGE_KILLED_SFX = 18;
//
//
//
//    public static int INVI_ON = 19;
//    public static int INVI_OFF = 20;
//
//    public static int LIGHTSEEKER_ATTACK_SFX = 21;
//    public static int IMPERIAL_MAGE_ATTACK_SFX = 22;
//    public static int DART_SHOT = 23;
//
//    private Clip[] songs, effects;
//    private int currentSongID;
//    private float volume = 0.5f;
//    private boolean songMute, effectMute;
//
//    public AudioPlayer() {
//        loadSongs();
//        loadEffects();
//        playSong(MENU_1);
//    }
//
//    private void loadSongs() {
//        String[] names = {"mainmenu", "level1", "level2"};
//        songs = new Clip[names.length];
//        for (int i = 0; i < names.length; i++) {
//            songs[i] = getClip(names[i]);
//        }
//    }
//
//    private void loadEffects() {
//        String[] effectNames = {"death", "jump", "gameover", "levelcompleted", "shoot",
//                "dash", "portal", "slime_hit", "slime_killed", "wolf_hit", "wolf_killed",
//                "golem_hit", "golem_killed", "lightseeker_hit", "lightseeker_killed",
//                "reaver_hound_hit", "reaver_hound_killed", "imperial_mage_hit",
//                "imperial_mage_killed", "invi_on", "invi_off", "lightseeker_attack",
//                "imperial_mage_attack", "dart_shot"};
//        effects = new Clip[effectNames.length];
//        for (int i = 0; i < effects.length; i++)
//            effects[i] = getClip(effectNames[i]);
//
//        updateEffectsVolume();
//
//    }
//
//    private Clip getClip(String name) {
//        URL url = getClass().getResource("/audio/" + name + ".wav");
//        AudioInputStream audio;
//
//        try {
//            audio = AudioSystem.getAudioInputStream(url);
//            Clip c = AudioSystem.getClip();
//            c.open(audio);
//
//            return c;
//        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void setVolume(float volume) {
//        this.volume = volume;
//        updateSongVolume();
//        updateEffectsVolume();
//    }
//
//    public void stopSong() {
//        if (songs[currentSongID].isActive())
//            songs[currentSongID].stop();
//    }
//
//    public void stopEffect(int effect) {
//        if (effects[effect].isActive())
//            effects[effect].stop();
//    }
//
//    public void setLevelSong(int lvlIndex) {
//        switch(lvlIndex) {
//            case 0 -> playSong(LEVEL_1);
//            case 1 -> playSong(LEVEL_2);
//        }
////        if(lvlIndex % 2 ==0)
////            playSong(LEVEL_1);
////        else
////            playSong(LEVEL_2);
//    }
//
//    public void lvlCompleted() {
//        stopSong();
//        playEffect(LVL_COMPLETED);
//    }
//
//    public void playAttackSound() {
//        playEffect(SHOOT);
//    }
//
//    public void playEffect(int effect) {
//        effects[effect].setMicrosecondPosition(0);
//        effects[effect].start();
//    }
//
////    public static void PlayEffect(int effect) {
////        effects[effect].setMicrosecondPosition(0);
////        effects[effect].start();
////    }
//
//    public void playSong(int song) {
//        stopSong();
//
//        currentSongID = song;
//        updateSongVolume();
//        songs[currentSongID].setMicrosecondPosition(0);
//        songs[currentSongID].loop(Clip.LOOP_CONTINUOUSLY);
//    }
//
//    public void toggleSongMute() {
//        this.songMute = !songMute;
//        for(Clip c : songs) {
//            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
//            booleanControl.setValue(songMute);
//        }
//    }
//
//    public void toggleEffectMute() {
//        this.effectMute = !effectMute;
//        for(Clip c : effects) {
//            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
//            booleanControl.setValue(effectMute);
//        }
//        if (!effectMute) {
//            playEffect(JUMP);
//        }
//    }
//
//    private void updateSongVolume() {
//        FloatControl gainControl = (FloatControl) songs[currentSongID].getControl(FloatControl.Type.MASTER_GAIN);
//        float range = gainControl.getMaximum() - gainControl.getMinimum();
//        float gain = (range * volume) + gainControl.getMinimum();
//        gainControl.setValue(gain);
//    }
//
//    private void updateEffectsVolume() {
//        for (Clip c : effects) {
//            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
//            float range = gainControl.getMaximum() - gainControl.getMinimum();
//            float gain = (range * volume) + gainControl.getMinimum();
//            gainControl.setValue(gain);
//        }
//    }
//}

package audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {

    // Static variables for storing audio clip based on index
    public static int MENU_1 = 0;
    public static int TUTORIAL = 1;
    public static int LEVEL_1 = 2;
    public static int LEVEL_2 = 3;
    public static int LEVEL_3 = 4;

    // PLayer sounds
    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int SHOOT = 4;
    public static int DASH = 5;
    public static int PORTAL = 6;
    public static int INVI_ON = 19;
    public static int INVI_OFF = 20;

    // Enemy sounds
    public static int SLIME_HIT = 7;
    public static int SLIME_KILLED = 8;

    public static int WOLF_HIT = 9;
    public static int WOLF_KILLED = 10;

    public static int GOLEM_HIT = 11;
    public static int GOLEM_KILLED = 12;

    public static int LIGHTSEEKER_HIT = 13;
    public static int LIGHTSEEKER_KILLED = 14;

    public static int REAVER_HOUND_HIT = 15;
    public static int REAVER_HOUND_KILLED = 16;

    public static int IMPERIAL_MAGE_HIT_SFX = 17;
    public static int IMPERIAL_MAGE_KILLED_SFX = 18;

    public static int LIGHTSEEKER_ATTACK_SFX = 21;
    public static int IMPERIAL_MAGE_ATTACK_SFX = 22;

    // Dart Trap sound
    public static int DART_SHOT = 23;

    private Clip[] songs, effects; // Array list of clip (for storing sound files)
    private int currentSongID; // ID of current song being played
    private float volume_MUSIC = 0.5f; // default volume on startup
    private float volume_SFX = 0.5f; // Default volume on startup
    private boolean songMute, effectMute; // Boolean variables for checking whether sounds are muted (default false)

    public AudioPlayer() {
        loadSongs();
        loadEffects();
        playSong(MENU_1); // Play the main menu song on startup
    }

    // Load songs from songs array
    private void loadSongs() {
        String[] songNames = {"mainmenu", "tutorial", "level1", "level2", "level3"};
        songs = new Clip[songNames.length]; // Create a new array that stores all song files
        for (int i = 0; i < songNames.length; i++) {
            songs[i] = getClip(songNames[i]);
        }
    }

    // Load sound effects from effects array
    private void loadEffects() {
        String[] effectNames = {"death", "jump", "gameover", "levelcompleted", "shoot", "dash", "portal",
                "slime_hit", "slime_killed", "wolf_hit", "wolf_killed", "golem_hit", "golem_killed",
                "lightseeker_hit", "lightseeker_killed", "reaver_hound_hit", "reaver_hound_killed",
                "imperial_mage_hit", "imperial_mage_killed", "invi_on", "invi_off",
                "lightseeker_attack", "imperial_mage_attack", "dart_shot"};
        effects = new Clip[effectNames.length]; // Create a new array that stores all sound effect audio files
        for (int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectNames[i]);

        updateEffectsVolume();

    }

    // Getting all the audio files from resources folder
    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav"); // Get audio from audio folder
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url); // files loaded from audio folder
            Clip c = AudioSystem.getClip();
            c.open(audio); // store to clip variable

            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Print error if can't find file
        }
        return null;
    }

    // Setter for volume of sound effect
    public void setVolumeSFX(float volume_SFX) {
        this.volume_SFX = volume_SFX;
        updateEffectsVolume();
    }

    // Setter for volume of music
    public void setVolumeMusic(float volume_MUSIC) {
        this.volume_MUSIC = volume_MUSIC;
        updateSongVolume();
    }

    // Method for stopping all songs in game
    public void stopSong() {
        if (songs[currentSongID].isActive())
            songs[currentSongID].stop();
    }

    // Method for stopping all effects in game
    public void stopEffect(int effect) {
        if (effects[effect].isActive())
            effects[effect].stop();
    }

    // Set the current stage's music
    public void setLevelSong(int lvlIndex) {
        switch(lvlIndex) {
            case 0 -> playSong(TUTORIAL);
            case 1 -> playSong(LEVEL_1);
            case 2 -> playSong(LEVEL_2);
            case 3 -> playSong(LEVEL_3);
        }
    }

    // Play level completed sound effect
    public void lvlCompleted() {
        stopSong(); // Stop the current stage music
        playEffect(LVL_COMPLETED); // Play level completed sound effect
    }

    // Play the attack sound
    public void playAttackSound() {
        playEffect(SHOOT);
    }

    // Play the sound effect based on argument passed
    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    // Play a song/music based on argument passed
    public void playSong(int song) {
        stopSong(); // Stops the currently played song (Prevent overlapping/multiple songs playing at the same time)

        currentSongID = song; // Set current song id to the argument passed
        updateSongVolume();
        songs[currentSongID].setMicrosecondPosition(0); // Start from 0:00
        songs[currentSongID].loop(Clip.LOOP_CONTINUOUSLY); // Loop song until stopped by other method (stopSong)
    }

    // Toggles between sound and no sound (for music/songs in game)
    public void toggleSongMute() {
        this.songMute = !songMute;
        for(Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    // Toggles between sound and no sound (for sound effects in game)
    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for(Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute) {
            playEffect(JUMP); // Play jump effect when sound effect unmuted
        }
    }

    // Update the current volume for music in game
    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongID].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume_MUSIC) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    // Update the current volume for sound effects in game
    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume_SFX) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}

