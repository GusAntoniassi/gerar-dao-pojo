/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetos.banco;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cisa
 */
public class Tabela {
    public String nome;
    public List<Atributo> atributos = new ArrayList<Atributo>();
    public boolean temAtributosNaoIdentificados;
    
    public Tabela() {
        temAtributosNaoIdentificados = false;
    }
    
    public Tabela(String nome, List<Atributo> atributos, boolean flag) {
        this.nome = nome;
        this.atributos = atributos;
        this.temAtributosNaoIdentificados = flag;
    }
}
