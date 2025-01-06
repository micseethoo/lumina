package utilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class LoadSave {

    // Variables for storing all the names of the images in the resources folderr
    public static final String ICON = "static_crystal.png";

    public static final String PLAYER_ATLAS = "archer.png";
    public static final String LEVEL_ATLAS = "level_tileset.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_TITLE = "title.png";
    public static final String SETTINGS = "settings.png";
    public static final String OVERRIDE_SCREEN = "saveOverride.png";


    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String BACK_BUTTONS = "back_buttons.png";

    // Save override buttons
    public static final String YES_BUTTON = "yesButton.png";
    public static final String NO_BUTTON = "noButton.png";

    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String MENU_BACKGROUND = "menu_background.jpg";
    public static final String STAGE_BACKGROUND = "stage_1_background.png";
    public static final String STATUS_BAR = "new_status_bar.png";
    public static final String STATIC_CRYSTAL = "static_crystal.png";
    public static final String CRYSTAL_COUNTER_BORDER = "crystal_counter_bg.png";
    public static final String LEVEL_COMPLETED_IMG = "level_completed_sprite.png";

    public static final String CRYSTAL_ATLAS = "crystal.png";
    public static final String POTION_ATLAS = "health_potion_sprites.png";

    public static final String MAGICTRAP_ATLAS = "magic_trap_alt.png";
    public static final String MAGICPOOL_ATLAS = "magic_pool_sprite.png";
    public static final String DARTTRAP_ATLAS = "dart_trap_sprite.png";


    public static final String PORTAL_ATLAS = "portal.png";

    public static final String PLAYERARROW = "smol_arrow.png";
    public static final String DART = "dart.png";



    // Buff icons
    public static final String SPIRIT_OF_TESSELATION = "spirit_of_tesselation.png";
    public static final String SPIRIT_OF_ASCENT = "spirit_of_ascent.png";
    public static final String SPIRIT_OF_SHADOWFLAME = "spirit_of_shadowflame.png";
    public static final String SPIRIT_OF_HASTE = "spirit_of_haste.png";
    public static final String SPIRIT_OF_GREED = "spirit_of_greed.png";
    public static final String SPIRIT_OF_OBSCURITY = "spirit_of_obscurity.png";


    public static final String PAUSE_BACKGROUND = "pause_screen.png";
    public static final String PAUSE_BUTTON = "pause_overlay_button.png";



    // Method for getting all image sprite sheets from the resources img folder
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/images/" + fileName);
        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    // Method for getting all the levels pixel map from the resources img folder
    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++)
            for(int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];
            }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imgs;
    }

    // Method for getting all icon images from resouces img folder
    public static BufferedImage GetIcons(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/buff_icons/" + fileName);
        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }
}
