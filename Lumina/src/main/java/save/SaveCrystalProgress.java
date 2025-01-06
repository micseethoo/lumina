package save;

public class SaveCrystalProgress {

    // Save the current crystal count
    public static void saveCrystalProgress(int crystalCount) {
        // Load the existing progress data (to retain the current level index)
        SaveProgress.ProgressData progressData = SaveProgress.loadProgress();

        // Save the new crystal count along with the existing level index
        SaveProgress.saveProgress(progressData.levelIndex, crystalCount);

        System.out.println("Crystal count " + crystalCount + " saved.");
    }

    // Load the saved crystal count
    public static int loadCrystalProgress() {
        // Load the existing progress data
        SaveProgress.ProgressData progressData = SaveProgress.loadProgress();

        System.out.println("Crystal count " + progressData.crystalCount + " loaded.");

        // Return the saved crystal count
        return progressData.crystalCount;
    }
}