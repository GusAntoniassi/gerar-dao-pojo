/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabela;

import java.awt.Event;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author cisa
 */
public class NomesPojoTable extends JTable {
    class ActionLinhaAbaixo extends AbstractAction {
        int colunaPreferida;

        public ActionLinhaAbaixo() {
            this(0);
        }

        public ActionLinhaAbaixo(int colunaPreferida) {
            super();
            this.colunaPreferida = colunaPreferida;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getSelectedColumn() == colunaPreferida) {
                if (isEditing()) {
                    getCellEditor().stopCellEditing();
                }

                if (getSelectedRow() == getRowCount() - 1) {
                    changeSelection(0, 1, false, false);
                } else {
                    changeSelection(getSelectedRow() + 1, colunaPreferida, false, false);
                }
            } else {
                changeSelection(getSelectedRow(), colunaPreferida, false, false);
            }
        }
    }

    class ActionLinhaAcima extends AbstractAction {
        int colunaPreferida;

        public ActionLinhaAcima() {
            this(0);
        }

        public ActionLinhaAcima(int colunaPreferida) {
            super();
            this.colunaPreferida = colunaPreferida;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getSelectedColumn() == colunaPreferida) {
                if (isEditing()) {
                    getCellEditor().stopCellEditing();
                }

                if (getSelectedRow() == 0) {
                    changeSelection(getRowCount() - 1, colunaPreferida, false, false);
                } else {
                    changeSelection(getSelectedRow() - 1, colunaPreferida, false, false);
                }
            } else {
                changeSelection(getSelectedRow(), colunaPreferida, false, false);
            }
        }
    }

    public NomesPojoTable(NomesPojoTableModel dtm) {
        super(dtm);

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "tab");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, Event.SHIFT_MASK), "shift tab");

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Event.SHIFT_MASK), "shift enter");

        this.getActionMap().put("tab", new ActionLinhaAbaixo(1));
        this.getActionMap().put("shift tab", new ActionLinhaAcima(1));

        this.getActionMap().put("enter", new ActionLinhaAbaixo(1) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                editCellAt(getSelectedRow(), getSelectedColumn());
            }
        });
        this.getActionMap().put("shift enter", new ActionLinhaAcima(1) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                editCellAt(getSelectedRow(), getSelectedColumn());
            }
        });

        this.setDefaultRenderer(Object.class, new LinhaValidadaRenderer());
        this.setDefaultRenderer(ImageIcon.class, new IconRenderer());

        this.getColumnModel().getColumn(2).setMaxWidth(64);
        this.getColumnModel().getColumn(2).setMinWidth(20);
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
        this.getColumnModel().getColumn(2).setMaxWidth(64);
        this.getColumnModel().getColumn(2).setMinWidth(20);
    }

    static class IconRenderer extends LinhaValidadaRenderer.UIResource {

        public IconRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }

        public void setValue(Object value) {
            setIcon((value instanceof Icon) ? (Icon) value : null);
        }
    }

    @Override
    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
        if (columnClass.equals(Object.class)) {
            return (LinhaValidadaRenderer) super.getDefaultRenderer(columnClass);
        } else if (columnClass.equals(IconRenderer.class)) {
            return (LinhaValidadaRenderer.UIResource) super.getDefaultRenderer(columnClass);
        } else {
            return super.getDefaultRenderer(columnClass);
        }
    }

    public boolean isCellEditable(int linha, int coluna) {
        return (coluna == 1);
    }

    //  Returning the Class of each column will allow different
    //  renderers to be used based on Class
    @Override
    public Class<?> getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }

    public String getToolTipText(MouseEvent e) {
        String tip = null;
        Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = convertColumnIndexToModel(columnAtPoint(p));

        if (colIndex == 2 && rowIndex >= 0) {
            tip = getModel().getTooltipAt(rowIndex);
        }

        return tip;
    }

    @Override
    public NomesPojoTableModel getModel() {
        return (NomesPojoTableModel) dataModel;
    }

}
