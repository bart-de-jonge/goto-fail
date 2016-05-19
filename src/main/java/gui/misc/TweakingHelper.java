package gui.misc;

import javafx.scene.paint.Color;

/**
 * Statics to help with gui tweakability.
 */
public final class TweakingHelper {

    // four main colors used throughout application. Experiment a little!

    /*
     * Orange-y!
     */

//    public static final Color COLOR_PRIMARY = Color.rgb(255, 172, 70); // main bright color
//    public static final Color COLOR_SECONDARY = Color.rgb(255, 140, 0); // darker color
//    public static final Color COLOR_TERTIARY = Color.rgb(255, 235, 190); // lighter color
//    public static final Color COLOR_BACKGROUND = Color.WHITE; // main bg color
//    public static final Color COLOR_BACKGROUND_HIGH =
//            Color.rgb(245, 245, 245); // heightened bg color

    /*
     * Cool-blue!
     */

    public static final Color COLOR_PRIMARY = Color.rgb(69, 162, 217); // light color
    public static final Color COLOR_SECONDARY = Color.rgb(2, 80, 126); // very dark color
    public static final Color COLOR_TERTIARY = Color.rgb(9, 135, 210); // dark color
    public static final Color COLOR_QUADRATORY = Color.rgb(144, 199, 232); // very light color
    public static final Color COLOR_BACKGROUND = Color.WHITE; // main bg color
    public static final Color COLOR_BACKGROUND_HIGH =
            Color.rgb(245, 245, 245); // heightened bg color

    // string versions of main colors
    public static final String STRING_PRIMARY = getStringFromColor(COLOR_PRIMARY);
    public static final String STRING_SECONDARY = getStringFromColor(COLOR_SECONDARY);
    public static final String STRING_TERTIARY = getStringFromColor(COLOR_TERTIARY);
    public static final String STRING_QUADRATORY = getStringFromColor(COLOR_QUADRATORY);
    public static final String STRING_BACKGROUND = getStringFromColor(COLOR_BACKGROUND);
    public static final String STRING_BACKGROUND_HIGH = getStringFromColor(COLOR_BACKGROUND_HIGH);

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

    

}