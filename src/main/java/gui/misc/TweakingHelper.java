package gui.misc;

import javafx.scene.paint.Color;

/**
 * statics to help with gui tweakability.
 * @author Mark
 */
public final class TweakingHelper {

    // four main colors used throughout application. Experiment a little!
    public static Color COLOR_PRIMARY = Color.rgb(255, 172, 70); // main bright color
    public static Color COLOR_SECONDARY = Color.rgb(255, 140, 0); // darker color
    public static Color COLOR_TERTIARY = Color.rgb(255, 235, 190); // lighter color
    public static Color COLOR_BACKGROUND = Color.WHITE;

    // string versions of main coors
    public static String STRING_PRIMARY = getStringFromColor(COLOR_PRIMARY);
    public static String STRING_SECONDARY = getStringFromColor(COLOR_SECONDARY);
    public static String STRING_TERTIARY = getStringFromColor(COLOR_TERTIARY);
    public static String STRING_BACKGROUND = getStringFromColor(COLOR_BACKGROUND);

    // general layout variables used throughout application.
    public static final int GENERAL_SIZE = 10000;
    public static final int GENERAL_SPACING = 10;
    public static final int GENERAL_PADDING = 20;

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

    /**
     * Set primary application color to new value.
     * @param color to set.
     */
    public static void setPrimaryColor(Color color) {
        COLOR_PRIMARY = color;
        STRING_PRIMARY = getStringFromColor(color);
    }

    /**
     * Set secondary application color to new value.
     * @param color to set.
     */
    public static void setSecondaryColor(Color color) {
        COLOR_SECONDARY = color;
        STRING_SECONDARY = getStringFromColor(color);
    }

    /**
     * Set tertiary application color to new value.
     * @param color to set.
     */
    public static void setTertiaryColor(Color color) {
        COLOR_TERTIARY = color;
        STRING_TERTIARY = getStringFromColor(color);
    }

    /**
     * Set background application color to new value.
     * @param color to set.
     */
    public static void setBackroundColor(Color color) {
        COLOR_BACKGROUND = color;
        STRING_BACKGROUND = getStringFromColor(color);
    }

}