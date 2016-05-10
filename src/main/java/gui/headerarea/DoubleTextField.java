package gui.headerarea;

import javafx.scene.control.TextField;
import java.text.DecimalFormat;

/**
 * Created by Bart.
 */
public class DoubleTextField extends TextField {

    /**
     * Replace text while typing if needed (when it is not a double).
     */
    @Override
    public void replaceText(int start, int end, String text) {
        String newText = this.getText().substring(0, start) + text
                + this.getText().substring(end, this.getText().length());

        if (validate(newText)) {
            super.replaceText(start, end, text);
        }
    }

    /**
     * Replace selected text if needed when it is not a double).
     */
    @Override
    public void replaceSelection(String text) {
        if (validate(this.getText())) {
            super.replaceSelection(text);
        }
    }

    /**
     * Validate the text to be a double.
     * @param text the text to validate
     * @return true if text is a double, false otherwise
     */
    private boolean validate(String text) {
        return text.matches("[0-9]*[.]?[0-9]*");
    }
}
