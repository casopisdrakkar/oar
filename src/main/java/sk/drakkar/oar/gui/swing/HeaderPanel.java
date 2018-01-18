package sk.drakkar.oar.gui.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Panel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class HeaderPanel extends JPanel {
    private JLabel drakkarLabel = new JLabel("Drakkar #");
    private JLabel drakkarNumberLabel = new JLabel("???");
    private JLabel targetFolderLabel = new JLabel();
    private JLabel dropZoneLabel = new JLabel("Přetáhněte sem PDF Drakkaru");
    private JButton exportButton = new JButton("Exportovat");

    private FileDroppedListener fileDroppedListener = FileDroppedListener.EMPTY;

    private File issueFile;

    private String targetFolder;

    private String issueNumber;

    public HeaderPanel() {
        setLayout(new BorderLayout());

        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dropZoneLabel.setBorder(border);


        add(dropZoneLabel, BorderLayout.LINE_END);

        Panel drakkarPanel = new Panel(new MigLayout("insets 5"));
        drakkarPanel.add(drakkarLabel);
        drakkarLabel.setFont(drakkarLabel.getFont().deriveFont(20.0f));

        drakkarPanel.add(drakkarNumberLabel, "wrap");
        drakkarPanel.add(targetFolderLabel, "wrap");
        drakkarPanel.add(exportButton, BorderLayout.CENTER);

        add(drakkarPanel, BorderLayout.LINE_START);

        enableDropSupport();

    }

    private void enableDropSupport() {
        DropTargetListener listener = new DropTargetAdapter() {
            @Override
            public void dragOver(DropTargetDragEvent event) {
                if (!event.getTransferable().isDataFlavorSupported(
                        DataFlavor.javaFileListFlavor)) {
                    event.rejectDrag();
                }
            }

            @Override
            public void drop(DropTargetDropEvent event) {
                Transferable transferable = event.getTransferable();

                try {
                    DataFlavor dataFlavor = DataFlavor.javaFileListFlavor;
                    // see
                    // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6759788
                    event.acceptDrop(event.getDropAction());
                    List<File> files = (List<File>) transferable
                            .getTransferData(dataFlavor);
                    File droppedFile = files.get(0);

                    handleDroppedFile(droppedFile);

                    event.dropComplete(true);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Component component = this.dropZoneLabel;
        DropTarget dropTarget = new DropTarget(component, listener);
        component.setDropTarget(dropTarget);
    }


    protected void handleDroppedFile(File droppedFile) {
        try {
            this.issueFile = droppedFile;
            this.targetFolder = this.issueFile.getParentFile().getParent();
            this.issueNumber = parseIssueNumber(this.issueFile);

            this.drakkarNumberLabel.setText(this.issueNumber);
            this.targetFolderLabel.setText(this.targetFolder);

            this.fileDroppedListener.onFileDropped(droppedFile);
        } catch (ParseException e) {
            JOptionPane.showConfirmDialog(this, "Zlý názov súboru s PDF", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    public File getIssueFile() {
        return issueFile;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    private String parseIssueNumber(File issueFile) throws ParseException {
        //drakkar_2012_35_prosinec.pdf
        String fileName = issueFile.getName();
        String[] components = fileName.split("_");
        if(components.length < 3) {
            throw new ParseException("Illegal filename", 0);
        }

        return components[2];
    }

    public void setOnExportButtonActionListener(ActionListener listener) {
        this.exportButton.addActionListener(listener);
    }

    public void setFileDroppedListener(FileDroppedListener fileDroppedListener) {
        this.fileDroppedListener = fileDroppedListener;
    }
}
