package sk.drakkar.oar.gui.javafx;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class SummaryTableCell extends TableCell<SummaryAdapter, String> {
    public SummaryTableCell() {
        TableCell thisCell = this;

        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }
            int index = ((SummaryTableCell) event.getTarget()).getTableRow().getIndex();

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(index));
            dragboard.setDragView(new Image("drakkar-logo.png"));
            dragboard.setContent(content);

            event.consume();
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(0.3);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(1);
            }
        });

        setOnDragDropped(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ObservableList<SummaryAdapter> items = getTableView().getItems();

                int targetIndex = ((SummaryTableCell) event.getTarget()).getTableRow().getIndex();
                int draggedIndex = Integer.parseInt(db.getString());

                SummaryAdapter tmp = items.get(draggedIndex);
                items.set(draggedIndex, items.get(targetIndex));
                items.set(targetIndex, tmp);
                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        setOnDragDone(DragEvent::consume);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        if(item == null) {
            super.updateItem("článek", empty);
        } else {
            super.updateItem(item, empty);
        }
    }
}
