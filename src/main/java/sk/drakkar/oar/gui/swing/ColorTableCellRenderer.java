package sk.drakkar.oar.gui.swing;

import sk.drakkar.oar.ColorGenerator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class ColorTableCellRenderer implements TableCellRenderer {
    private DefaultTableCellRenderer delegateTableCellRenderer = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        ColorGenerator.Color color = parseColor(value);
        if (color != null) {
            JLabel cellLabel = (JLabel) delegateTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            cellLabel.setIcon(new ArticleColorIcon(color, 32, ArticleColorIcon.DEFAULT_DIMENSION));
            cellLabel.setText("");
            return cellLabel;
        } else {
            return delegateTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private ColorGenerator.Color parseColor(Object value) {
        try {
            if (value == null) {
                return null;
            }
            return ColorGenerator.Color.valueOf(((String) value).toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
