/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabela;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cisa
 */
public class NomesPojoTableModel extends DefaultTableModel {
    private List<String> tooltipErro = new ArrayList();
    private List<Color> coresLinhas = new ArrayList();
    
    private final Color corPadrao = Color.WHITE;
    public final Color corValido = new Color(175, 245, 162);
    public final Color corAviso =  new Color(245, 244, 162);
    public final Color corInvalido = new Color(245, 162, 168);
    
    public static final int INVALIDO = 0;
    public static final int AVISO = 1;
    public static final int VALIDO = 2;
    
    public int statusDados = INVALIDO;    
    public String tipoDados;

    public int[] statusLinha;
    
    public String getTooltipAt(int row) {
        if (row >= getRowCount() || row < 0) {
            return null;
        }
        
        return tooltipErro.get(row);
    }

    public void setTooltipAt(int row, String s) {
        tooltipErro.set(row, s);
    }
    
    public Color getCorLinha(int linha) {
        if (linha >= getRowCount() || linha < 0) {
            return corPadrao;
        }
        
        return coresLinhas.get(linha);
    }
    
    public void setCorLinha(int linha, Color cor) {
        coresLinhas.set(linha, cor);
        fireTableRowsUpdated(linha, linha);
    }

    @Override
    public void addRow(Vector rowData) {
        super.addRow(rowData);
        tooltipErro.add(null);
        coresLinhas.add(corPadrao);
    }
}
