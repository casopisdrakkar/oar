package sk.drakkar.oar.gui.swing;

import sk.drakkar.oar.ColorGenerator;
import sk.drakkar.oar.conversion.Summary;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SummaryTableModel extends AbstractTableModel {
    private List<Summary> summaries = new ArrayList<>();

    public final List<Column> columns = Arrays.asList(Column.values());

    public void remove(int index) {
        this.summaries.remove(index);
        fireTableDataChanged();
    }

    public enum Column {
        COLOR("color", "RGB"),
        TITLE("title", "Název"),
        AUTHORS("authors", "Autoři"),
        TAG("tags", "Tagy"),
        SUMMARY("summary", "Perex");

        private String code;

        private String description;

        Column(String code, String description) {
            this.code = code;
            this.description = description;
        }

        @Override
        public String toString() {
            return this.description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    };

    public SummaryTableModel() {
        // empty constructor
    }

    public SummaryTableModel(List<Summary> summaries) {
        this.summaries = new ArrayList<>(summaries);
    }

    @Override
    public int getRowCount() {
        return this.summaries.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    public void add(Summary summary) {
        this.summaries.add(summary);
        fireTableDataChanged();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Summary summary = this.summaries.get(rowIndex);
        Column column = this.columns.get(columnIndex);
        switch (column) {
            case TAG:
                return summary.getTags();
            case TITLE:
                return summary.getTitle();
            case COLOR:
                return summary.getColor();
            case AUTHORS:
                return summary.getAuthors();
            case SUMMARY:
                return summary.getSummary();
            default:
                throw new IllegalStateException("Neznámy typ stĺpca");
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        Column column = this.columns.get(columnIndex);
        return column.getDescription();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Column column = this.columns.get(columnIndex);
        switch (column) {
            case TAG:
            case TITLE:
                return String.class;
            case COLOR:
                return ColorGenerator.Color.class;
            case AUTHORS:
            case SUMMARY:
                return String.class;
            default:
                throw new IllegalStateException("Neznámy typ stĺpca");
        }
    }

    public Summary getSummary(int index) {
        return this.summaries.get(index);
    }

    public void refresh() {
        fireTableDataChanged();
    }

    public List<Summary> getSummaries() {
        return new ArrayList<>(this.summaries);
    }
}
