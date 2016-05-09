package gui.headerarea;

import java.util.regex.Pattern;

import javafx.scene.control.TextField;

public class DoubleTextField extends TextField {
    
    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }
    
    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }
    
    private boolean validate(String text) {
        Pattern pattern = Pattern.compile("[0-9]*[.][0-9]+");
        return pattern.matcher(text).matches();
    }

}
