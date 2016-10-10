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
public class Atributo {
    public String nome;
    public String tipo;
    public boolean flag;
    public boolean pojo;
    public Dominio dominio;
    
    public Atributo() {
        
    }
    
    public Atributo(String nome, String tipo, boolean flag) {
        this.nome = nome;
        this.tipo = tipo;
        this.flag = flag;
        this.pojo = false;
        this.dominio = null;
    }
    
    public Atributo(String nome, String tipo, boolean flag, boolean pojo) {
        this.nome = nome;
        this.tipo = tipo;
        this.flag = flag;
        this.pojo = pojo;
        this.dominio = null;
    }
    
    public Atributo(String nome, String tipo, boolean flag, Dominio dominio) {
        this.nome = nome;
        this.tipo = tipo;
        this.flag = flag;
        this.pojo = false;
        this.dominio = dominio;
    }
}
