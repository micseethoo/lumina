package utilz;

import com.example.lumina.Game;
import entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static utilz.Constants.UI.BuffIcons.BUFF_ICONS_HEIGHT;
import static utilz.Constants.UI.BuffIcons.BUFF_ICONS_WIDTH;

public class PlayerBuffs {

    // Checks if player has the buff
    private boolean hasInfiniteQuiver;
    private boolean hasDoubleJump;
    private boolean hasShadowflameArmor;
    private boolean hasIncreasedMovementSpeed;
    private boolean hasOverheal;
    private boolean hasInvisibility;

    private int nCF;


    private Player player; // Variabilise player instance to return values back and to check player crystal amount

    // Images for each buff icon
    private Image iconInfiniteQuiver;
    private Image iconDoubleJump;
    private Image iconShadowflameArmor;
    private Image iconIncreasedMovementSpeed;
    private Image iconOverheal;
    private Image iconInvisibility;

    private String tooltipText = ""; // Tooltip text for each buff
    private Timer tooltipTimer;
    private boolean showTooltip = false; // Checks if tooltip is supposed to be shown
    private Point tooltipPosition; // Position of tooltip
    private Image tooltipIcon; // Icon found inside the tooltip

    // Tooltip constants
    private static final int TOOLTIP_OFFSET_X = 10;
    private static final int TOOLTIP_OFFSET_Y = 20;

    public PlayerBuffs(Player player) {
        this.player = player;

        // Load icon images
        iconInfiniteQuiver = LoadSave.GetIcons(LoadSave.SPIRIT_OF_TESSELATION);
        iconDoubleJump = LoadSave.GetIcons(LoadSave.SPIRIT_OF_ASCENT);
        iconShadowflameArmor = LoadSave.GetIcons(LoadSave.SPIRIT_OF_SHADOWFLAME);
        iconIncreasedMovementSpeed = LoadSave.GetIcons(LoadSave.SPIRIT_OF_HASTE);
        iconOverheal = LoadSave.GetIcons(LoadSave.SPIRIT_OF_GREED);
        iconInvisibility = LoadSave.GetIcons(LoadSave.SPIRIT_OF_OBSCURITY);


        // Timer for showing tooltip on hover
        tooltipTimer = new Timer(100, e -> {
            showTooltip = true;
            System.out.println("Tooltip activated: " + tooltipText);
        });
        tooltipTimer.setRepeats(false);
    }

    public void updateBuffs() {
        nCF = player.getNumCrystalFragments(); // Number of crystal fragments the player has in total

        // Reset all buffs
        hasInfiniteQuiver = false;
        hasDoubleJump = false;
        hasShadowflameArmor = false;
        hasIncreasedMovementSpeed = false;
        hasOverheal = false;
        hasInvisibility = false;

        // Apply buffs based on crystal fragment count
        if (nCF >= 1)
            hasInfiniteQuiver = true;
        if (nCF >= 3)
            hasDoubleJump = true;
        if (nCF >= 5)
            hasShadowflameArmor = true;
        if (nCF >= 10)
            hasIncreasedMovementSpeed = true;
        if (nCF >= 15)
            hasOverheal = true;
        if (nCF >= 20)
            hasInvisibility = true;
    }

    // Border for each buff icon
    private void drawBorder(Graphics g, int bWidth) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(245,  245,  220, 100));
        g2d.fillRoundRect((int)(bWidth * Game.SCALE), (int) (70 * Game.SCALE),BUFF_ICONS_WIDTH + 4, BUFF_ICONS_HEIGHT + 4, 15, 15);
        g2d.setColor(new Color(90, 90, 90));
        g2d.drawRoundRect((int)(bWidth * Game.SCALE), (int) (70 * Game.SCALE),BUFF_ICONS_WIDTH + 4, BUFF_ICONS_HEIGHT + 4, 15, 15);
    }

    // Draw the buff icons based on player available buffs + tooltips
    public void drawBuffs(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        drawBuffIcon(g, iconInfiniteQuiver, (int) (8 * Game.SCALE), (int) (72 * Game.SCALE), 8, hasInfiniteQuiver);
        drawBuffIcon(g, iconDoubleJump, (int) (50 * Game.SCALE), (int) (72 * Game.SCALE), 50, hasDoubleJump);
        drawBuffIcon(g, iconShadowflameArmor,  (int) (92 * Game.SCALE), (int) (72 * Game.SCALE), 92, hasShadowflameArmor);
        drawBuffIcon(g, iconIncreasedMovementSpeed,  (int) (134 * Game.SCALE), (int) (70 * Game.SCALE), 134, hasIncreasedMovementSpeed);
        drawBuffIcon(g, iconOverheal,  (int) (176 * Game.SCALE), (int) (72 * Game.SCALE), 176, hasOverheal);
        drawBuffIcon(g, iconInvisibility,  (int) (218 * Game.SCALE), (int) (72 * Game.SCALE), 218, hasInvisibility);


        if (showTooltip) {
            System.out.println("Showing tooltip: " + tooltipText); // Debugging statement

            player.getPlaying().getGame().setCustomFont(g, 7f * Game.SCALE);;
            Font customFont = g.getFont();
            FontMetrics metrics = g2d.getFontMetrics(customFont);
            int lineHeight = metrics.getHeight() + 3;

            java.util.List<String> lines = getWrappedText(tooltipText, metrics, 400);
            int tooltipHeight = lines.size() * lineHeight + 10;
            int tooltipWidth = 400 + BUFF_ICONS_WIDTH + 20;

            Composite originalComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f)); // 50% transparent

            // Draw tooltip background
            g.setColor(Color.BLACK);
            g.fillRoundRect(tooltipPosition.x + TOOLTIP_OFFSET_X, tooltipPosition.y - TOOLTIP_OFFSET_Y - tooltipHeight, tooltipWidth, tooltipHeight, 10, 10);

            g2d.setComposite(originalComposite);
            // Draw tooltip border
            g.setColor(Color.WHITE);
            g.drawRoundRect(tooltipPosition.x + TOOLTIP_OFFSET_X, tooltipPosition.y - TOOLTIP_OFFSET_Y - tooltipHeight, tooltipWidth, tooltipHeight, 10, 10);

            // Draw icon
            g.drawImage(tooltipIcon, tooltipPosition.x + TOOLTIP_OFFSET_X + 5, tooltipPosition.y - TOOLTIP_OFFSET_Y - tooltipHeight + (tooltipHeight - BUFF_ICONS_HEIGHT) / 2, (int) BUFF_ICONS_WIDTH, BUFF_ICONS_HEIGHT, null);

            // Draw text
            g.setColor(Color.WHITE);
            int textX = tooltipPosition.x + TOOLTIP_OFFSET_X + 15 + BUFF_ICONS_WIDTH;
            int textY = tooltipPosition.y - TOOLTIP_OFFSET_Y - tooltipHeight + 5 + lineHeight - 3;
            for (String line : lines) {
                g.drawString(line, textX, textY);
                textY += lineHeight;
            }
            // Draw text
            //g.drawString(tooltipText, tooltipPosition.x + TOOLTIP_OFFSET_X + 15 + (int) BUFF_ICONS_WIDTH, tooltipPosition.y - TOOLTIP_OFFSET_Y - (tooltipHeight / 2) + (textHeight / 2));
        }
    }

    // Method for drawing icon of buffs
    private void drawBuffIcon(Graphics g, Image icon, int x, int y, int bX, boolean isActive) {
        Graphics2D g2d = (Graphics2D) g;

        if (!isActive) {
            Composite originalComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // 50% transparent

            g2d.setComposite(originalComposite);
        } else {
            drawBorder(g, bX);
            g2d.drawImage(icon, x, y, BUFF_ICONS_WIDTH, BUFF_ICONS_HEIGHT, null);
        }
    }

    // Wrap the text within the boundaries of the tooltip
    private java.util.List<String> getWrappedText(String text, FontMetrics metrics, int maxWidth) {
        java.util.List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (metrics.stringWidth(line.toString() + word) < maxWidth) {
                line.append(word).append(" ");
            } else {
                lines.add(line.toString());
                line = new StringBuilder(word).append(" ");
            }
        }
        if (!line.toString().isEmpty()) {
            lines.add(line.toString());
        }

        return lines;
    }





    // Method for getting the coordinate of the mouse and to show the tooltip if the mouse coordinates align with the position of the buff icons
    public void getMouseCoords(MouseEvent e) {
        Point mousePosition = e.getPoint(); // Variable to get mouse position
        tooltipPosition = mousePosition;
        showTooltip = false;
        tooltipTimer.stop(); // Timer for tooltip (to check if 100ms has elapsed to show tooltip of buff)
        int nCF = player.getNumCrystalFragments();

        // Bounds for the tooltip box and text for each tooltip buff
        if (new Rectangle((int) (6 * Game.SCALE), (int) (72 * Game.SCALE), BUFF_ICONS_WIDTH, BUFF_ICONS_HEIGHT).contains(mousePosition) && nCF >= 1) {
            tooltipText = "Spirit of Tesselation (Infinite Quiver): You shoot arrows infinitely.";
            tooltipIcon = iconInfiniteQuiver;
            tooltipTimer.restart();
            System.out.println("Hovering over Infinite Quiver");

        } else if (new Rectangle((int) (48 * Game.SCALE), (int) (72 * Game.SCALE), BUFF_ICONS_WIDTH, BUFF_ICONS_HEIGHT).contains(mousePosition) && nCF >= 3) {
            tooltipText = "Spirit of Ascent (Double Jump): You can now jump in air & again after a jump.";
            tooltipIcon = iconDoubleJump;
            tooltipTimer.restart();
            System.out.println("Hovering over Double Jump");

        } else if (new Rectangle((int) (90 * Game.SCALE), (int) (72 * Game.SCALE), BUFF_ICONS_WIDTH, BUFF_ICONS_HEIGHT).contains(mousePosition) && nCF >= 5) {
            tooltipText = "Spirit of Shadowflame (Shadowflame Armor): Damage reduction +20%, you now reflect 80% of damage taken."; // Damage reflected increased to 80%
            tooltipIcon = iconShadowflameArmor;
            tooltipTimer.restart();
            System.out.println("Hovering over Shadowflame Spirit");

        } else if (new Rectangle((int) (132 * Game.SCALE), (int) (72 * Game.SCALE), BUFF_ICONS_WIDTH, BUFF_ICONS_HEIGHT).contains(mousePosition) && nCF >= 10) {
            tooltipText = "Spirit of Haste (Increased Movement Speed): Movement Speed +20%.";
            tooltipIcon = iconIncreasedMovementSpeed;
            tooltipTimer.restart();
            System.out.println("Hovering over Increased Movement Speed");

        } else if (new Rectangle((int) (174 * Game.SCALE), (int) (72 * Game.SCALE), BUFF_ICONS_WIDTH, BUFF_ICONS_HEIGHT).contains(mousePosition) && nCF >= 15) {
            tooltipText = "Spirit of Greed (Overheal): Arrow hits heal you for 2hp. Additionally, excess healing is converted into overheal health, overhealing up to 50hp.";
            tooltipIcon = iconOverheal;
            tooltipTimer.restart();
            System.out.println("Hovering over Overheal");

        } else if (new Rectangle((int) (216 * Game.SCALE), (int) (72 * Game.SCALE), (int) BUFF_ICONS_WIDTH, BUFF_ICONS_HEIGHT).contains(mousePosition) && nCF >= 20) {
            tooltipText = "Spirit of Obscurity (Invisibility): Press F to go invisible, obscuring yourself from enemies (-4 stamina/s).";
            tooltipIcon = iconInvisibility;
            tooltipTimer.restart();
            System.out.println("Hovering over Invisibility");
        }

        // Testing code
//        else {
//            System.out.println("Not hovering over any buff icon");
//        }
    }

    public boolean isHasDoubleJump() {
        return hasDoubleJump;
    }

    public boolean isHasShadowflameArmor() {
        return hasShadowflameArmor;
    }

    public boolean isHasIncreasedMovementSpeed() {
        return hasIncreasedMovementSpeed;
    }

    public boolean isHasOverheal() {
        return hasOverheal;
    }

    public boolean isHasInvisibility() {
        return hasInvisibility;
    }
}
