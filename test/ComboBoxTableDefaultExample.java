import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class ComboBoxTableDefaultExample {
  public static void main(String[] argv) throws Exception {
    JFrame jf = new JFrame();
    JTable table = new JTable();
    JScrollPane jsp = new JScrollPane(table);
    
    jf.getContentPane().add(jsp);
    
    DefaultTableModel model = (DefaultTableModel) table.getModel();

    model.addColumn("A", new Object[] { "item1" });
    model.addColumn("B", new Object[] { "item2" });

    JComboBox comboBox = new JComboBox(new String[] {"String", "Bool", "teste"});

    TableColumn col = table.getColumnModel().getColumn(0);
    col.setCellEditor(new MyComboBoxEditor(comboBox));
    
    String[] items = new String[comboBox.getItemCount()];
    for (int i = 0; i < comboBox.getItemCount(); i++) {
        items[i] = (String) comboBox.getItemAt(i);
    }
    
    col.setCellRenderer(new MyComboBoxRenderer(items));
    
    jf.pack();
    jf.setVisible(true);
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
  public MyComboBoxRenderer(String[] items) {
    super(items);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    setSelectedItem(value);
    return this;
  }
}

class MyComboBoxEditor extends DefaultCellEditor {
  public MyComboBoxEditor(JComboBox cb) {
    super(cb);
  }
}
