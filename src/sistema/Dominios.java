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
    public static class ComboBoxTiposJava extends JComboBox {
        private String[] tiposJava = new String[] {"boolean", 
            "char", "String"};
    
        public ComboBoxTiposJava(String tipoSelecionado) {
            super();
            
            for (String tipo : tiposJava) {
                addItem(tipo);
            }
            
            setSelectedItem(tipoSelecionado);
        }
    }
    
    
    
    public static void main(String[] args) {
        List<Dominio> dominios = new ArrayList();
        dominios.add(new Dominio("Teste1", "boolean", "V", "F"));
        dominios.add(new Dominio("Teste2", "boolean", "S", "N"));
        dominios.add(new Dominio("Teste3", "boolean", "F", "J"));
        dominios.add(new Dominio("Teste4", "String"));
        dominios.add(new Dominio("Teste5", "int"));
        TelaDominios telaDominios = new TelaDominios(dominios);
    }
}
