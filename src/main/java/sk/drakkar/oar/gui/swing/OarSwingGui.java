package sk.drakkar.oar.gui.swing;

import com.bulenkov.darcula.DarculaLaf;
import net.miginfocom.swing.MigLayout;
import sk.drakkar.oar.ColorGenerator;
import sk.drakkar.oar.conversion.Summary;
import sk.drakkar.oar.conversion.SummaryExporter;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

public class OarSwingGui extends JFrame implements ArticleSummaryPanel.SummaryAddedListener, ArticleSummaryPanel.SummaryUpdatedListener {
    private JTable summaryTable = new JTable();

    private JScrollPane summaryTableScrollPane = new JScrollPane(this.summaryTable);

    private SummaryTableModel summaryTableModel = new SummaryTableModel();

    private ArticleSummaryPanel articleSummaryPanel = new ArticleSummaryPanel();

    private SummaryExporter summaryExporter = new SummaryExporter();
    private HeaderPanel headerPanel;

    public OarSwingGui() throws HeadlessException {
        super("Drakkar");
        setLayout(new MigLayout("wrap 1", "[grow, fill]", "[grow, growprio 5, fill][grow, growprio 15, fill]"));

        configureHeaderPanel();

        this.summaryTable.setModel(this.summaryTableModel);
        this.summaryTable.getSelectionModel().addListSelectionListener(this::summaryTableListSelectionChanged);
        this.summaryTable.setDefaultRenderer(ColorGenerator.Color.class, new ColorTableCellRenderer());
        this.summaryTable.getColumnModel().getColumn(SummaryTableModel.Column.COLOR.ordinal()).setMaxWidth(32);
        configureDeleteKey(this.summaryTable);

        add(this.summaryTableScrollPane, "hmax 400, height 200");

        this.articleSummaryPanel.setSummaryAddedListener(this);
        this.articleSummaryPanel.setSummaryUpdatedListener(this);
        add(this.articleSummaryPanel);

        setBounds(0, 0, 800, 600);
        setLocationRelativeTo(null);

        Summary summary = new Summary();
        summary.setTitle("Hello");
        summary.setAuthors("John");

        this.summaryTableModel.add(summary);

        pack();
    }

    private void configureHeaderPanel() {
        this.headerPanel = new HeaderPanel();
        add(this.headerPanel);
        this.headerPanel.setOnExportButtonActionListener(this::onExportButtonClicked);
    }

    private void onExportButtonClicked(ActionEvent event) {
        File issueFile = this.headerPanel.getIssueFile();
        String pdfTargetFolder = this.headerPanel.getTargetFolder();
        String issueNumber = this.headerPanel.getIssueNumber();

        if(issueFile == null) {
            JOptionPane.showMessageDialog(this, "PDF soubor Drakkaru nebyl nastaven!");
            return;
        }

        File targetFolder = new File(pdfTargetFolder + File.separator + issueNumber);
        if(! targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        List<Summary> summaries = this.summaryTableModel.getSummaries();
        summaryExporter.writeSummaries(summaries, targetFolder);
        summaryExporter.writeIssueMetadata(summaries, issueFile, targetFolder);
    }

    private void configureDeleteKey(JTable table) {
        InputMap inputMap = table.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = table.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        actionMap.put("delete", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                int selectedRow = table.getSelectedRow();
                OarSwingGui.this.summaryTableModel.remove(selectedRow);
            }
        });
    }

    @Override
    public void onSummaryAdded(Summary summary) {
        this.summaryTableModel.add(summary);
    }

    @Override
    public void onSummaryUpdated(Summary summary) {
        this.summaryTableModel.refresh();
    }

    private void summaryTableListSelectionChanged(ListSelectionEvent event) {
        if(event.getValueIsAdjusting()) {
            return;
        }
        try {
            int index = this.summaryTable.getSelectedRow();
            Summary summary = this.summaryTableModel.getSummary(index);
            this.articleSummaryPanel.setSummary(summary);
        } catch (IndexOutOfBoundsException e) {
            this.articleSummaryPanel.reset();
        }
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new DarculaLaf());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    OarSwingGui oarSwingGui = new OarSwingGui();
                    oarSwingGui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    oarSwingGui.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
