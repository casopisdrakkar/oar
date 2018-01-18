package sk.drakkar.oar.gui.swing;

import java.io.File;

public interface FileDroppedListener {
    public static final FileDroppedListener EMPTY = file -> {};

    void onFileDropped(File droppedFile);
}
