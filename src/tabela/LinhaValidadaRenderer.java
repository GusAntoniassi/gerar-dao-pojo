/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabela;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author cisa
 */
public class LinhaValidadaRenderer extends DefaultTableCellRenderer {  
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        NomesPojoTableModel model = (NomesPojoTableModel) table.getModel();
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setBackground(model.getCorLinha(row));
        return c;
    }
    
    public static class UIResource extends DefaultTableCellRenderer
        implements javax.swing.plaf.UIResource
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            NomesPojoTableModel model = (NomesPojoTableModel) table.getModel();
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(model.getCorLinha(row));
            return c;
        }
    }
}
