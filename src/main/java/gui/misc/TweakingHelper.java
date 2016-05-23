package gui.misc;

import com.sun.xml.internal.ws.developer.Serialization;
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
    private static Color backgroundHighColor =
            Color.rgb(245, 245, 245); // heightened bg color

    /*
     * Getters
     */

    /**
     * @return primary color.
     */
    public static Color getPrimaryColor() {
        return colors[colorChoice][0];
    }

    /**
     * @return secondary color.
     */
    public static Color getSecondaryColor() {
        return colors[colorChoice][1];
    }

    /**
     * @return tertiary color.
     */
    public static Color getTertiaryColor() {
        return colors[colorChoice][2];
    }

    /**
     * @return quadratory color.
     */
    public static Color getQuadratoryColor() {
        return colors[colorChoice][3];
    }

    /**
     * @return primary color as string.
     */
    public static String getPrimaryString() {
        return getStringFromColor(getPrimaryColor());
    }

    /**
     * @return secondary color as string.
     */
    public static String getSecondaryString() {
        return getStringFromColor(getSecondaryColor());
    }

    /**
     * @return tertiary color as string.
     */
    public static String getTertiaryString() {
        return getStringFromColor(getTertiaryColor());
    }

    /**
     * @return quadratory color as string.
     */
    public static String getQuadratoryString() {
        return getStringFromColor(getQuadratoryColor());
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