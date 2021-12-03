package jiux.net.plugin.restful.highlight;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

/**
 * Text highlighting
 */
public class JTextAreaHighlight {

    /* highlighting */
    public static void highlightTextAreaData(JTextArea jTextArea) {


        Highlighter highLighter = jTextArea.getHighlighter();
        DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.DARK_GRAY);
        highLighter.removeAllHighlights();
        String text = jTextArea.getText().trim();
        //TODO: isKeyValueFormat  highlighting param , value

        //TODO :isJson  highlighting  value
        if (text.startsWith("[") || text.startsWith("{")) {
            return;
        }

        int start = 0;
        String[] lines = text.split("\n");
        for (String line : lines) {
            int index = line.indexOf(":");
            if (index < 0) {
                continue;
            }
            start += index;
            int end = start + 1;
            try {
                highLighter.addHighlight(start, end, highlightPainter);
                start += line.substring(index).length() + 1;
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
}
