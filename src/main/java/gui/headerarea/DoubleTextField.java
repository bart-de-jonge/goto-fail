package gui.headerarea;

import gui.styling.StyledTextfield;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;

/**
 * Created by Bart.
 */
public class DoubleTextField extends StyledTextfield {

    /**
     * Constructor of class.
     */
    public DoubleTextField() {
        super();
    }

    /**
     * Constructor of class.
     * @param text text to setup initially.
     */
    public DoubleTextField(String text) {
        super(text);
    }

    @Override
    public void replaceText(int start, int end, String text) {
        String newText = this.getText().substring(0, start) + text
                + this.getText().substring(end, this.getText().length());

        if (validate(newText)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(this.getText())) {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text) {
        return text.matches("[0-9]*[.]?[0-9]*");
    }
}
