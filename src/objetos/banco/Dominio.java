/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetos.banco;

/**
 *
 * @author cisa
 */
public class Dominio {
    public String nome;
    public String tipo;
    public String V;
    public String F;
    
    public Dominio() {
        
    }
    
    public Dominio(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }
    
    public Dominio(String nome, String tipo, String V, String F) {
        this.nome = nome;
        this.tipo = tipo;
        if (tipo.equals("boolean")) {
            this.V = V;
            this.F = F;
        }
    }
    
}
