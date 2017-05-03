package sk.drakkar.oar.gui.swing;

import sk.drakkar.oar.conversion.Summary;
import sk.drakkar.oar.conversion.SummaryTemplateService;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.util.List;

public class ArticleSummaryTemplateComboBoxModel extends AbstractListModel<Summary> implements ComboBoxModel<Summary>, ListCellRenderer<Summary> {
    private List<Summary> templates = SummaryTemplateService.INSTANCE.getTemplates();

    private Summary selectedItem;

    private DefaultListCellRenderer delegateListCellRenderer = new DefaultListCellRenderer();

    @Override
    public void setSelectedItem(Object anItem) {
        this.selectedItem = (Summary) anItem;
    }

    @Override
    public Object getSelectedItem() {
        if(this.selectedItem == null) {
            this.selectedItem = templates.get(0);
        }
        return this.selectedItem;
    }

    @Override
    public int getSize() {
        return templates.size();
    }

    @Override
    public Summary getElementAt(int index) {
        return templates.get(index);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Summary> list, Summary summary, int index, boolean isSelected, boolean cellHasFocus) {
        return delegateListCellRenderer.getListCellRendererComponent(list, summary.getTitle(), index, isSelected, cellHasFocus);
    }

    public Summary getDefaultSummary() {
        return this.templates.get(0);
    }
}
