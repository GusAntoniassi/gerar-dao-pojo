
import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gus
 */
public class ComboBoxTableExample {    
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JTable table;
        table = new JTable(new Object[][]{
            new JComboBox[]{new JComboBox(new String[]{"1", "2", "3"}),
                new JComboBox(new String[]{"1", "2", "3"}),
                new JComboBox(new String[]{"1", "2", "3"})
            }, new String[] {"Teste1", "Teste2", "Teste3"}}, new String[]{"ComboBox", "String"}) 
        {
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                Object value = super.getValueAt(row, column);
                
                if(value != null && value instanceof JComboBox) {
                    GusCellEditor cellEditor = new GusCellEditor(new JComboBox()) {
                        
                    };
                    
                    cellEditor.addCellEditorListener(new CellEditorListener() {
                        @Override
                        public void editingStopped(ChangeEvent e) {
                            cellEditor.pararDeEditar();
                        }
                        
                        @Override
                        public void editingCanceled(ChangeEvent e) {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                        
                    });
                    
                    return cellEditor;
                }
                return super.getCellEditor(row, column);
            }
            
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                Object value = super.getValueAt(row, column);
            
                if (value != null && value instanceof JComboBox) {
                    return new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value,
                                boolean isSelected, boolean hasFocus, int row, int column) {
                            if (value instanceof JComboBox) {
                                super.setText(((JComboBox)value).getSelectedItem().toString());
                            }
                            return this;
                        }
                    };
                } else {
                    return super.getCellRenderer(row, column);
                }
            }
            
            /*
            @Override
            public TableColumnModel getColumnModel() {
            return null;
            }*/
        };
        
        
        
        JScrollPane jsp = new JScrollPane(table);
        
        jf.getContentPane().add(jsp);
        jf.pack();
        jf.setVisible(true);
    }
}

class GusCellEditor extends DefaultCellEditor {
        public GusCellEditor(JComboBox jcb) {
            super(jcb);
        }
        
        public GusCellEditor(JTextField jtf) {
            super(jtf);
        }
        
        public GusCellEditor(JCheckBox jcb) {
            super(jcb);
        }
        
        @Override
                        public boolean stopCellEditing() {
                            return super.stopCellEditing();
                        }
                        
                        public void pararDeEditar() {
                            this.fireEditingStopped();
                        }
                        
                        @Override
                        protected void fireEditingStopped() {
                            super.fireEditingStopped();
                        }
    }
