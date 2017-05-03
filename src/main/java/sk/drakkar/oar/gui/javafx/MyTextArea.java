package sk.drakkar.oar.gui.javafx;

import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.util.Collections;

public class MyTextArea extends TextArea {
    public MyTextArea() {
    }

    public MyTextArea(String text) {
        super(text);
    }

    @Override
    public void paste() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            final String text = clipboard.getString();
            if (text != null) {
                String sanitized = text.replaceAll("\r", "\n");
                clipboard.setContent(Collections.singletonMap(DataFormat.PLAIN_TEXT, sanitized));
                System.out.println(text);
                super.paste();
            }
        }
    }
}
