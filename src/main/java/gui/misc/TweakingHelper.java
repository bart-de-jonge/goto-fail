package gui.misc;

import java.util.Arrays;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Statics to help with gui tweakability.
 */
public final class TweakingHelper {

    @Getter @Setter
    private static int colorChoice = 0;

    /*
     * Constants used throughout application.
     */

    public static final int GENERAL_SIZE = 10000;
    public static final int GENERAL_SPACING = 10;
    public static final int GENERAL_PADDING = 20;

    /*
     * Main colors of application. Has 4 presets right now.
     */

    @Getter
    private static final Color[][] colors = {
            {
                    Color.rgb(69, 162, 217),
                    Color.rgb(9, 135, 210),
                    Color.rgb(2, 80, 126),
                    Color.rgb(144, 199, 232)
            },
            {
                    Color.rgb(255, 172, 70),
                    Color.rgb(255, 140, 0),
                    Color.rgb(198, 109, 0),
                    Color.rgb(255, 209, 152)
            },
            {
                    Color.rgb(123, 241, 67),
                    Color.rgb(77, 238, 0),
                    Color.rgb(55, 171, 0),
                    Color.rgb(179, 247, 147)
            },
            {
                    Color.rgb(255, 102, 70),
                    Color.rgb(255, 44, 0),
                    Color.rgb(198, 34, 0),
                    Color.rgb(255, 170, 152)
            }
    };

    /*
     * Color names for the main colors. Used by settings menu, for example.
     */

    @Getter
    private static final String[] colorNames = {
        "blue",
        "orange",
        "green",
        "red"
    };

    /**
     * @return amount of colors currently defined.
     */
    public static int getNumberOfColors() {
        return colors.length;
    }

    /*
     * Some whites and grays used throughout the application.
     */

    @Getter
    private static Color backgroundColor = Color.WHITE; // main bg color
    @Getter
    private static Color backgroundHighColor = Color.rgb(245, 245, 245); // heightened bg color
    @Getter
    private static Color backgroundContrastColor = Color.WHITE; // Color.BLACK;

    /*
     * Getters
     */
    
    public static String[] getColorNames() {
        String[] result;
        result = Arrays.copyOf(colorNames, colorNames.length);
        return result;
    }
    
    public static Color[][] getColors() {
        Color[][] result = new Color[colors.length][colors[0].length];
        for (int i=0;i<colors.length;i++) {
            result[i] = Arrays.copyOf(colors[i], colors[i].length);
        }
        return result;
    }

    /**
     * Returns a color of choice., specified by an index.
     * @param i index of the color.
     * @return the color of choice.
     */
    public static Color getColor(int i) {
        return colors[colorChoice][i < getNumberOfColors() ? i : 0];
    }

    /**
     * Returns string version of a color of choice, specified by index.
     * @param i index of the color.
     * @return the string version of the color of choice.
     */
    public static String getColorString(int i) {
        return getStringFromColor(getColor(i));
    }

    /**
     * @return background color as string.
     */
    public static String getBackgroundString() {
        return getStringFromColor(backgroundColor);
    }

    /**
     * @return background heightened color as string.
     */
    public static String getBackgroundHighString() {
        return getStringFromColor(backgroundHighColor);
    }

    /**
     * Parses color from a Color object to javafx-css-compatible string.
     * @param color the color to parse.
     * @return a representative string.
     */
    private static String getStringFromColor(Color color) {
        return "rgba(" + ((int) (color.getRed()   * 255)) + ","
                + ((int) (color.getGreen() * 255)) + ","
                + ((int) (color.getBlue()  * 255)) + ","
                + color.getOpacity() + ")";
    }
    
}