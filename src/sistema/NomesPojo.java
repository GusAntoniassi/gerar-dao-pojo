/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import java.util.ArrayList;
import java.util.List;
import tabela.NomesPojoTableModel;
import telas.TelaNomesPojo;

/**
 *
 * @author cisa
 */
public class NomesPojo {

    public static final String TABELAS = "as tabelas";
    public static final String ATRIBUTOS = "os atributos";

    public static List<String> identificarNomes(List<String> nomesNaoIdentificados, String tipoDados) {
        NomesPojoTableModel dtm = new NomesPojoTableModel();

        dtm.tipoDados = tipoDados;

        dtm.addColumn("Nome SQL");
        dtm.addColumn("Nome POJO");
        dtm.addColumn("Status");

        for (String s : nomesNaoIdentificados) {
            dtm.addRow(new String[]{s, "", ""});
        }

        TelaNomesPojo telaNomes = new TelaNomesPojo(dtm);

        List<String> nomesIdentificados = new ArrayList();
        
        for (int i = 0; i < dtm.getRowCount(); i++) {
            String nome = ((String) dtm.getValueAt(i, 1)).trim();
            nomesIdentificados.add(nome);
        }

        return nomesIdentificados;
    }
    
    public static void main(String[] args) {
        List<String> dados = new ArrayList();
        dados.add("abcd");
        dados.add("abcd");
        dados.add("abcd");
        dados.add("abcd");

        NomesPojo.identificarNomes(dados, NomesPojo.ATRIBUTOS);
    }
}