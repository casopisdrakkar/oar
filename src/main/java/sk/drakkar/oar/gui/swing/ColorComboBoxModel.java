package sk.drakkar.oar.gui.swing;

import sk.drakkar.oar.ColorGenerator;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;

public class ColorComboBoxModel extends AbstractListModel<ColorGenerator.Color> implements ComboBoxModel<ColorGenerator.Color>, ListCellRenderer<ColorGenerator.Color> {
    private ColorGenerator.Color selectedColor;

    private DefaultListCellRenderer delegateCellRenderer = new DefaultListCellRenderer();

    @Override
    public void setSelectedItem(Object color) {
        this.selectedColor = (ColorGenerator.Color) color;
    }

    @Override
    public Object getSelectedItem() {
        return this.selectedColor;
    }

    @Override
    public int getSize() {
        return ColorGenerator.Color.values().length;
    }

    @Override
    public ColorGenerator.Color getElementAt(int index) {
        return ColorGenerator.Color.values()[index];
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ColorGenerator.Color> list, ColorGenerator.Color color, int index, boolean isSelected, boolean cellHasFocus) {
        String value;
        if(color == null) {
            value = "- vyberte barvu -";
        } else {
            value = color.toString().toLowerCase();
        }
        Component cell = delegateCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(color != null && cell instanceof JLabel) {
            JLabel cellLabel = (JLabel) cell;
            cellLabel.setIcon(new ArticleColorIcon(color));
        }
        return cell;
    }
}
