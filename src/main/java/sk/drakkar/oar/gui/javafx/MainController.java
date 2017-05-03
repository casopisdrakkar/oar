package sk.drakkar.oar.gui.javafx;

import javafx.beans.binding.When;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import sk.drakkar.oar.conversion.Summary;
import sk.drakkar.oar.conversion.SummaryExporter;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    @FXML
    public Label dropZoneArea;

    @FXML
    public Label targetFolderLabel;

    @FXML
    public Label drakkarLabel;

    @FXML
    public Label drakkarNumberLabel;

    @FXML
    public Button exportButton;

    @FXML
    private TableView<SummaryAdapter> articleSummaryTableView;

    @FXML
    public TableColumn shortSummaryTableColumn;

    @FXML
    private SummaryController summaryController;

    private ObservableList<SummaryAdapter> summaryAdapters = FXCollections.observableArrayList();


    // -- SimpleModel
    private ObjectProperty<File> issueFile = new SimpleObjectProperty<>();

    private ObjectProperty<File> targetFolder = new SimpleObjectProperty<>();

    private StringProperty issueNumber = new SimpleStringProperty();

    // -- Services

    private SummaryExporter summaryExporter = new SummaryExporter();

    @FXML
    public void initialize() {
        this.articleSummaryTableView.setItems(summaryAdapters);
        this.articleSummaryTableView.getSelectionModel().selectedItemProperty().addListener(this::onArticleSummaryViewSelectedItemChanged);

        this.shortSummaryTableColumn.setCellValueFactory(new PropertyValueFactory("shortSummary"));


        this.summaryController.setSummaryAddedListener(this::onSummaryAdded);

        this.dropZoneArea.setOnDragOver(this::onDropZoneAreaDragOver);
        this.dropZoneArea.setOnDragDropped(this::onDropZoneAreaDragDropped);

        this.targetFolderLabel.textProperty().bind(new When(this.targetFolder.isNotNull()).then(this.targetFolder.asString()).otherwise(""));

        this.drakkarNumberLabel.textProperty().bind(new When(this.issueNumber.isNotNull()).then(this.issueNumber).otherwise("???"));

        this.exportButton.disableProperty().bind(this.targetFolder.isNull());

        configureTableContextMenu();
    }

    private void configureTableContextMenu() {
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(this::onDeleteMenuItemActionPerformed);

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(deleteMenuItem);
        this.articleSummaryTableView.setContextMenu(menu);
    }

    private void onDeleteMenuItemActionPerformed(ActionEvent actionEvent) {
        SummaryAdapter selectedItem = this.articleSummaryTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            this.summaryAdapters.remove(selectedItem);
        }
    }

    public void onArticleSummaryViewSelectedItemChanged(ObservableValue<? extends SummaryAdapter> observable, SummaryAdapter oldValue, SummaryAdapter newValue) {
        this.summaryController.setSummary(newValue.getWrappedSummary());
    }

    public void onSummaryAdded(Summary summary) {
        this.summaryAdapters.add(new SummaryAdapter(summary));
    }

    public void onExportButtonClick(ActionEvent actionEvent) {
        if(this.issueFile == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Soubor nebyl zvolen");
            alert.setHeaderText("Soubor nebyl zvolen");
            alert.setContentText("PDF soubor Drakkaru nebyl specifikován");

            alert.showAndWait();
        }


        File targetFolder = new File(this.targetFolder.get() + File.separator + this.issueNumber.get());
        if(! targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        List<Summary> summaries = this.articleSummaryTableView
                .getItems()
                .stream()
                .map(SummaryAdapter::getWrappedSummary)
                .collect(Collectors.toList());
        //addEditorial(summaries);

        summaryExporter.writeSummaries(summaries, targetFolder);
        summaryExporter.writeIssueMetadata(summaries, this.issueFile.get(), targetFolder);
    }

    private void onDropZoneAreaDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
        dragEvent.consume();
    }

    private void onDropZoneAreaDragDropped(DragEvent dragEvent) {
        List<File> files = dragEvent.getDragboard().getFiles();
        File file = files.get(0);
        dragEvent.setDropCompleted(true);
        dragEvent.consume();

        handleDroppedFile(file);
    }

    protected void handleDroppedFile(File droppedFile) {
        try {
            this.issueFile.set(droppedFile);
            this.targetFolder.set(this.issueFile.get().getParentFile().getParentFile());
            this.issueNumber.set(parseIssueNumber(this.issueFile.get()));

            this.dropZoneArea.setText("Číslo bylo načteno");
        } catch (ParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nesprávný název souboru");
            alert.setHeaderText("Formát souboru není ve správném tvaru");
            alert.setContentText("Formát souboru není ve správném tvaru");

            alert.showAndWait();
        }
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

    public File getIssueFile() {
        return issueFile.get();
    }

    public ObjectProperty<File> issueFileProperty() {
        return issueFile;
    }

    public void setIssueFile(File issueFile) {
        this.issueFile.set(issueFile);
    }
}
