package gui.styling;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class StyledElementHelper {
    
    /**
     * Helper function for binding a fill color. Creates a string property used
     * to modify the style at runtime.
     * @param colorProperty the colorProperty whose color we want to show.
     * @return The StringProperty which we'll use to set the style.
     */
    public StringProperty createColorStringProperty(ObjectProperty<Color> colorProperty) {
        StringProperty stringProperty = new SimpleStringProperty();
        stringProperty.set(getStringFromColor(colorProperty.get()));
        colorProperty.addListener(
            e -> {
                stringProperty.set(getStringFromColor(colorProperty.get()));
            });
        return stringProperty;
    }

    /**
     * Parses color from a Color object to javafx-css-compatible string.
     * @param color the color to parse.
     * @return a representative string.
     */
    public String getStringFromColor(Color color) {
        return "rgba(" + ((int) (color.getRed()   * 255)) + ","
                + ((int) (color.getGreen() * 255)) + ","
                + ((int) (color.getBlue()  * 255)) + ","
                + color.getOpacity() + ")";
    }

   

}
