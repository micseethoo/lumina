package save;

import java.io.*;

public class SaveProgress {

    private static final String FILE_NAME = "progress.dat";

    // Data structure to hold both level and crystal progress
    public static class ProgressData implements Serializable {
        private static final long serialVersionUID = 1L;
        int levelIndex;
        int crystalCount;

        public ProgressData(int levelIndex, int crystalCount) {
            this.levelIndex = levelIndex;
            this.crystalCount = crystalCount;
        }
    }

    // Save both level and crystal progress
    public static void saveProgress(int levelIndex, int crystalCount) {
        ProgressData progressData = new ProgressData(levelIndex, crystalCount);
        try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(progressData);
            System.out.println("Progress saved: Level Index " + levelIndex + ", Crystal Count " + crystalCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load both level and crystal progress
    public static ProgressData loadProgress() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

                ProgressData progressData = (ProgressData) objectIn.readObject();
                System.out.println("Progress loaded: Level Index " + progressData.levelIndex + ", Crystal Count " + progressData.crystalCount);
                return progressData;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Progress file not found. Using default values.");
        }
        return new ProgressData(0, 0); // Default values if file not found or error occurs
    }

    // Check if the progress file exists
    public static boolean doesProgressFileExist() {
        File file = new File(FILE_NAME);
        return file.exists(); // Return true if the file exists, false otherwise
    }

    public static void deleteProgress() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Progress file deleted successfully.");
            } else {
                System.out.println("Failed to delete the progress file.");
            }
        } else {
            System.out.println("Progress file does not exist.");
        }
    }

}
