package gui.styling;

import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

/**
 * Represents a stylized listview.
 * @author Mark
 */
public class StyledListview extends ListView {

    public StyledListview() {

    }

    /**
     * Parses color from a Color object to javafx-css-compatible string.
     * @param color the color to parse.
     * @return a representative string.
     */
    private String getStringFromColor(Color color) {
        return "rgba(" + ((int) (color.getRed()   * 255)) + ","
                + ((int) (color.getGreen() * 255)) + ","
                + ((int) (color.getBlue()  * 255)) + ","
                + color.getOpacity() + ")";
    }

}
