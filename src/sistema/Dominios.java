/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import objetos.banco.Dominio;
import tabela.DominiosTableModel;
import telas.TelaDominios;

/**
 *
 * @author cisa
 */
public class Dominios {
    static class ComboBoxTiposJava extends JComboBox {
        private String[] tiposJava = new String[] {"BigDecimal", "BigInteger", "boolean", 
            "char", "int", "String"};
    
        public ComboBoxTiposJava(String tipoSelecionado) {
            super();
            
            for (String tipo : tiposJava) {
                addItem(tipo);
            }
            
            setSelectedItem(tipoSelecionado);
        }
    }
    
    public static List<Dominio> resolverDominios(List<Dominio> dominiosNaoResolvidos) {
        DominiosTableModel dtm = new DominiosTableModel();
        dtm.addColumn("Nome do domínio");
        dtm.addColumn("Tipo do domínio");
        dtm.addColumn("Valor p/ true");
        dtm.addColumn("Valor p/ false");
        dtm.addColumn("Inverter");
        
        for (Dominio d : dominiosNaoResolvidos) {
            dtm.addRow(new Object[]{d.nome, new ComboBoxTiposJava(d.tipo), d.V, d.F, new JButton("Inverter")});
        }
        
        TelaDominios telaDominios = new TelaDominios(dtm);
        
        return dominiosNaoResolvidos;
    }
    
    
    
    public static void main(String[] args) {
        List<Dominio> dominios = new ArrayList();
        dominios.add(new Dominio("Teste1", "boolean", "V", "F"));
        dominios.add(new Dominio("Teste2", "boolean", "S", "N"));
        dominios.add(new Dominio("Teste3", "boolean", "F", "J"));
        dominios.add(new Dominio("Teste4", "String"));
        dominios.add(new Dominio("Teste5", "int"));
        resolverDominios(dominios);
    }
}
