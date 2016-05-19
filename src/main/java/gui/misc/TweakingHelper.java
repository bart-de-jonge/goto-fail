package gui.misc;

import javafx.scene.paint.Color;

/**
 * Statics to help with gui tweakability.
 */
public final class TweakingHelper {

    private static final int colorChoice = 1;

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

    public static final Color COLOR_PRIMARY = colors[colorChoice][0]; // main (light) color
    public static final Color COLOR_SECONDARY = colors[colorChoice][1]; // dark color
    public static final Color COLOR_TERTIARY = colors[colorChoice][2]; // very dark color
    public static final Color COLOR_QUADRATORY = colors[colorChoice][3]; // very light color

    /*
     * Some whites and grays used throughout the application.
     */

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