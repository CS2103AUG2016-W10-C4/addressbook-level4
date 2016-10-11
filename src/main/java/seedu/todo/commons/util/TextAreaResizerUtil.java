package seedu.todo.commons.util;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Automatically resize a text area object such that the height fits the content of the text area
 */
public class TextAreaResizerUtil {

    private Text pseudoText; //A text object that will imitate the text in the TextArea object
    private double singleLineHeight;

    /**
     * Set a particular TextArea object to have it's height automatically resize-d.
     */
    public void setResizable(TextArea textArea) {
        bindToMockText(textArea);
        setupTextArea(textArea);
    }

    private void bindToMockText(TextArea textArea) {
        pseudoText = new Text();
        pseudoText.textProperty().bind(textArea.textProperty());
        pseudoText.fontProperty().bind(textArea.fontProperty());
        pseudoText.wrappingWidthProperty().bind(textArea.widthProperty());
    }

    private void setupTextArea(TextArea textArea) {
        ChangeListener<Object> changeListener = (observable, oldValue, newValue) -> {
            double height = getCorrectedHeight() + 10;
            TimerTask action = new TimerTask() {
                @Override
                public void run() {
                    textArea.setMaxHeight(height);
                    textArea.setPrefHeight(height);
                }
            };

            Timer timer = new Timer();
            timer.schedule(action, 1);
        };
        textArea.widthProperty().addListener(changeListener);
        textArea.textProperty().addListener(changeListener);
    }

    /**
     * Gets a stable value of height by eliminating slight jitters in text area height.
     */
    private double getCorrectedHeight() {
        double rawHeight = pseudoText.getLayoutBounds().getHeight();
        if (singleLineHeight == 0) {
            singleLineHeight = rawHeight;
        }
        int numRows = (int) Math.ceil(rawHeight/singleLineHeight);
        return singleLineHeight * numRows;
    }
}
