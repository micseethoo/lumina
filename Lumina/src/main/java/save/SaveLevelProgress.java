package save;

public class SaveLevelProgress {

    // Save index of level
    public static void saveLevelProgress(int lvlIndex) {
        SaveProgress.ProgressData progressData = SaveProgress.loadProgress();
        SaveProgress.saveProgress(lvlIndex, progressData.crystalCount);
        System.out.println("Level Index " + lvlIndex + " saved.");
    }

    // Load index of previous save
    public static int loadLevelProgress() {
        SaveProgress.ProgressData progressData = SaveProgress.loadProgress();
        System.out.println("Level Index " + progressData.levelIndex + " loaded.");
        return progressData.levelIndex;
    }
}