package sk.drakkar.oar.gui.swing;

import net.miginfocom.swing.MigLayout;
import sk.drakkar.oar.ColorGenerator;
import sk.drakkar.oar.conversion.Summary;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;

public class ArticleSummaryPanel extends JPanel {
    private JLabel articleTypeLabel = new JLabel("Šablona");

    private JComboBox<Summary> templateComboBox = new JComboBox<>();

    private JLabel titleLabel = new JLabel("Název");

    private JTextField titleTextField = new JTextField();

    private JLabel authorsLabel = new JLabel("Autoři");

    private JTextField authorsTextField = new JTextField();

    private JLabel colorLabel = new JLabel("Barva");

    private JComboBox<ColorGenerator.Color> colorComboBox = new JComboBox<>(new ColorComboBoxModel());

    private JLabel tagsLabel = new JLabel("Tagy");

    private JTextField tagsTextField = new JTextField();

    private JLabel summaryLabel = new JLabel("Sumář");

    private JTextArea summaryTextArea = new JTextArea();

    private JScrollPane summaryScrollPane = new JScrollPane(summaryTextArea);

    private JButton saveSummaryButton = new JButton("Zaevidovat článek");

    private JButton newSummaryButton = new JButton("Vyčistit formulář");

    private JButton updateSummaryButton = new JButton("Uložit změny");

    //-- domain entity

    private Summary summary;

    private SummaryAddedListener summaryAddedListener = summary -> { };

    private SummaryUpdatedListener summaryUpdatedListener = summary -> { };

    private EditMode editMode = EditMode.CREATE;

    public ArticleSummaryPanel() {
        setLayout(new MigLayout("wrap 2", "[][grow, fill]", "[][][][][][grow, fill][nogrid]"));

        add(this.articleTypeLabel);

        ArticleSummaryTemplateComboBoxModel templateComboBoxModel = new ArticleSummaryTemplateComboBoxModel();
        this.templateComboBox.setModel(templateComboBoxModel);
        this.templateComboBox.setRenderer(templateComboBoxModel);
        this.templateComboBox.addActionListener(this::templateComboBoxItemSelected);
        this.templateComboBox.setSelectedItem(templateComboBoxModel.getDefaultSummary());

        add(this.templateComboBox);

        add(titleLabel);
        add(titleTextField);
        onEnterSwitchBetween(titleTextField, authorsTextField);

        add(authorsLabel);
        add(authorsTextField);
        onEnterSwitchBetween(authorsTextField, colorComboBox);

        add(colorLabel);

        add(colorComboBox);
        colorComboBox.setSelectedItem(ColorGenerator.Color.GRAY);
        colorComboBox.setRenderer(new ColorComboBoxModel());
        onEnterSwitchBetween(colorComboBox, tagsTextField);

        add(tagsLabel);
        add(tagsTextField);
        onEnterSwitchBetween(tagsTextField, summaryTextArea);

        add(summaryLabel);

        add(summaryScrollPane, "hmin 200");

        add(saveSummaryButton, "tag ok");
        saveSummaryButton.addActionListener(this::onSaveSummaryButtonActionPerformed);

        add(newSummaryButton);
        newSummaryButton.addActionListener(this::onNewSummaryButtonActionPerformed);

        add(updateSummaryButton);
        updateSummaryButton.addActionListener(this::onUpdateSummaryButtonActionPerformed);

        reset();
    }

    private void onEnterSwitchBetween(JTextField from, JTextArea to) {
        from.addActionListener(event -> to.requestFocus());
    }

    private void onEnterSwitchBetween(JTextField from, JTextField to) {
        from.addActionListener(event -> to.requestFocus());
    }

    private void onEnterSwitchBetween(JComboBox from, JTextField to) {
        from.addActionListener(event -> to.requestFocus());
    }

    private void onEnterSwitchBetween(JTextField from, JComboBox to) {
        from.addActionListener(event -> to.requestFocus());
    }

    private void templateComboBoxItemSelected(ActionEvent event) {
        Summary selectedSummary = new Summary((Summary) this.templateComboBox.getSelectedItem());
        setSummary(selectedSummary);
        setEditMode(EditMode.CREATE);
    }

    public void reset() {
        this.summary = new Summary();
        bindFromModel();
        setEditMode(EditMode.CREATE);
    }

    private void bindFromModel() {
        this.titleTextField.setText(this.summary.getTitle());
        this.authorsTextField.setText(this.summary.getAuthors());
        this.colorComboBox.setSelectedItem(ColorGenerator.Color.valueOf(this.summary.getColor().toUpperCase()));
        this.tagsTextField.setText(this.summary.getTags());
        this.summaryTextArea.setText(this.summary.getSummary());
    }

    private void bindToModel() {
        this.summary.setTitle(this.titleTextField.getText());
        this.summary.setAuthors(this.authorsTextField.getText());
        ColorGenerator.Color color = (ColorGenerator.Color) this.colorComboBox.getSelectedItem();
        this.summary.setColor(color.toString().toLowerCase());
        this.summary.setTags(this.tagsTextField.getText());
        this.summary.setSummary(this.summaryTextArea.getText());
    }

    private void onSaveSummaryButtonActionPerformed(ActionEvent e) {
        bindToModel();

        this.summaryAddedListener.onSummaryAdded(this.summary);
    }

    private void onNewSummaryButtonActionPerformed(ActionEvent actionEvent) {
        this.summary = new Summary();
        setEditMode(EditMode.CREATE);
        bindFromModel();
    }

    private void onUpdateSummaryButtonActionPerformed(ActionEvent actionEvent) {
        bindToModel();
        this.summaryUpdatedListener.onSummaryUpdated(this.summary);
    }


    public void setSummaryAddedListener(SummaryAddedListener summaryAddedListener) {
        this.summaryAddedListener = summaryAddedListener;
    }

    public void setSummaryUpdatedListener(SummaryUpdatedListener summaryUpdatedListener) {
        this.summaryUpdatedListener = summaryUpdatedListener;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
        setEditMode(EditMode.EDIT);
        bindFromModel();
    }

    private void setEditMode(EditMode editMode) {
        this.editMode = editMode;
        if(this.editMode == EditMode.CREATE) {
            this.saveSummaryButton.setEnabled(true);
            this.updateSummaryButton.setEnabled(false);
        } else {
            this.saveSummaryButton.setEnabled(false);
            this.updateSummaryButton.setEnabled(true);
        }
    }

    public interface SummaryAddedListener {
        void onSummaryAdded(Summary summary);
    }

    public interface SummaryUpdatedListener {
        void onSummaryUpdated(Summary summary);
    }

    public enum EditMode {
        CREATE, EDIT
    }
}
