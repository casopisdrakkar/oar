package sk.drakkar.oar.gui.javafx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import sk.drakkar.oar.ColorGenerator;
import sk.drakkar.oar.conversion.Summary;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SummaryController {
    public static final ObservableList<String> COLORS = FXCollections.observableArrayList(
            Arrays
                    .stream(ColorGenerator.Color.values())
                    .map(color -> color.name().toLowerCase())
                    .collect(Collectors.toList())
    );

    @FXML Button addButton;

    @FXML RadioButton articleRadioButton;

    @FXML RadioButton haikuRadioButton;

    @FXML RadioButton editorialRadioButton;

    private ToggleGroup articleTypeToggleGroup = new ToggleGroup();

    @FXML Label titleLabel;
    @FXML TextField titleTextField;

    @FXML Label authorsLabel;
    @FXML TextField authorsTextField;

    @FXML Label summaryLabel;
    @FXML TextArea summaryTextArea;

    @FXML Label colorLabel;
    @FXML ComboBox<String> colorComboBox;

    @FXML Label tagsLabel;
    @FXML TextField tagsTextField;

    private SummaryAdapter summaryAdapter;

    private SummaryAddedListener summaryAddedListener = summaryAdapter -> { };

    private BooleanProperty editMode = new SimpleBooleanProperty(false);

    public void initialize() {
        initializeSummaryAdapter();
        addBindings();

        this.addButton.disableProperty().bind(editMode);
    }

    private void addBindings() {
        this.articleRadioButton.setToggleGroup(this.articleTypeToggleGroup);
        this.haikuRadioButton.setToggleGroup(this.articleTypeToggleGroup);
        this.editorialRadioButton.setToggleGroup(this.articleTypeToggleGroup);

        this.articleTypeToggleGroup.selectedToggleProperty().addListener(this::onArticleTypeToggleGroupSelectedToggle);

        titleTextField.textProperty().bindBidirectional(summaryAdapter.titleProperty());
        authorsTextField.textProperty().bindBidirectional(summaryAdapter.authorsProperty());
        summaryTextArea.textProperty().bindBidirectional(summaryAdapter.summaryProperty());

        colorComboBox.setItems(COLORS);
        colorComboBox.valueProperty().bindBidirectional(summaryAdapter.colorProperty());

        tagsTextField.textProperty().bindBidirectional(summaryAdapter.tagsProperty());
    }

    private void initializeSummaryAdapter() {
        this.summaryAdapter = new SummaryAdapter(new Summary());
    }

    public void onNewButtonClick(ActionEvent actionEvent) {
        editMode.set(false);
        removeBindings();
        initialize();
    }

    public void onAddButtonClick(ActionEvent actionEvent) {
        removeBindings();
        this.summaryAddedListener.onSummaryAdded(summaryAdapter.getWrappedSummary());
        initializeSummaryAdapter();
        addBindings();
    }

    private void removeBindings() {
        titleTextField.textProperty().unbindBidirectional(summaryAdapter.titleProperty());
        authorsTextField.textProperty().unbindBidirectional(summaryAdapter.authorsProperty());
        summaryTextArea.textProperty().unbindBidirectional(summaryAdapter.summaryProperty());
        colorComboBox.valueProperty().unbindBidirectional(summaryAdapter.colorProperty());
        tagsTextField.textProperty().unbindBidirectional(summaryAdapter.tagsProperty());
    }

    public void onArticleTypeToggleGroupSelectedToggle(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        RadioButton selectedRadioButton = (RadioButton) newValue;
        switch (selectedRadioButton.getId()) {
            case "articleRadioButton" :
                this.summaryAdapter.setShortSummary(null);
                break;
            case "haikuRadioButton" :
                this.summaryAdapter.setShortSummary("Úvodní haiku");
                break;
            case "editorialRadioButton" :
                this.summaryAdapter.setShortSummary("Úvodní slovo");
                break;
            default:
                throw new IllegalStateException("Nepodporované ID RadioButton-u");
        }
    }

    public void setSummary(Summary summary) {
        editMode.set(true);
        removeBindings();
        this.summaryAdapter = new SummaryAdapter(summary);
        addBindings();
    }

    public void setSummaryAddedListener(SummaryAddedListener summaryAddedListener) {
        this.summaryAddedListener = summaryAddedListener;
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
        removeBindings();
        initializeSummaryAdapter();
    }

    public void onTitleTextFieldActionPerformed(ActionEvent actionEvent) {
        this.authorsTextField.requestFocus();
    }

    public void onAuthorsTextFieldActionPerformed(ActionEvent actionEvent) {
        this.colorComboBox.requestFocus();
    }

    public void onColorComboBoxActionPerformed(ActionEvent actionEvent) {
        this.tagsTextField.requestFocus();
    }

    public void onTagsTextFieldActionPerformed(ActionEvent actionEvent) {
        this.summaryTextArea.requestFocus();
    }

    public interface SummaryAddedListener {
        void onSummaryAdded(Summary summary);
    }
}
