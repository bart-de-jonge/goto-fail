package gui.misc;

import com.sun.xml.internal.ws.developer.Serialization;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Statics to help with gui tweakability.
 */
public final class TweakingHelper {

    private static final int colorChoice = 0;

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

    @Getter @Setter
    private static Color primaryColor = colors[colorChoice][0]; // main (light) color
    @Getter @Setter
    public static Color secondaryColor = colors[colorChoice][1]; // dark color
    @Getter @Setter
    public static Color tertiaryColor = colors[colorChoice][2]; // very dark color
    @Getter @Setter
    public static Color quadratoryColor = colors[colorChoice][3]; // very light color

    /*
     * Some whites and grays used throughout the application.
     */

    @Getter @Setter
    private static Color backgroundColor = Color.WHITE; // main bg color
    @Getter @Setter
    private static Color backgroundHighColor =
            Color.rgb(245, 245, 245); // heightened bg color

    // string versions of main colors
    @Getter
    private static String primaryString = getStringFromColor(primaryColor);
    @Getter
    private static String secondaryString = getStringFromColor(secondaryColor);
    @Getter
    private static String tertiaryString = getStringFromColor(tertiaryColor);
    @Getter
    private static String quadratoryString = getStringFromColor(quadratoryColor);
    @Getter
    private static String backgroundString = getStringFromColor(backgroundColor);
    @Getter
    private static String backgroundHighString = getStringFromColor(backgroundHighColor);

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