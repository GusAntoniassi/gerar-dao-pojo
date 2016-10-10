/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import java.util.ArrayList;
import java.util.List;
import objetos.banco.Atributo;
import objetos.banco.Banco;
import objetos.banco.Tabela;

/**
 *
 * @author cisa
 */
public class Main {
    public static void main(String[] args) {
        List<Tabela> tabelas = new ArrayList<Tabela>();
        List<Atributo> atributos = new ArrayList();
        
        // Estado
        Atributo id = new Atributo("id", "int", true, false);
        Atributo nome = new Atributo("nome", "String", true, false);
        Atributo sigla = new Atributo("sigla", "String", true, false);
        
        atributos.add(id);
        atributos.add(nome);
        atributos.add(sigla);
        
        Tabela tEstado = new Tabela("Estado", atributos, false);
        tabelas.add(tEstado);
        
        // Cidade
        atributos = new ArrayList();
        Atributo estado = new Atributo("estado", "Estado", true, true);
        
        atributos.add(id);
        atributos.add(nome);
        atributos.add(estado);
        
        Tabela tCidade = new Tabela("Cidade", atributos, false);
        tabelas.add(tCidade);
        
        // Endereço
        atributos = new ArrayList();
        
        Atributo cidade = new Atributo("cidade", "Cidade", true, true);
        Atributo endereco = new Atributo("endereco", "String", true, false);
        
        atributos.add(id);
        atributos.add(cidade);
        atributos.add(endereco);
        
        Tabela tEndereco = new Tabela("Endereco", atributos, false);
        tabelas.add(tEndereco);
        
        // Cargo
        atributos = new ArrayList();
        
        Atributo crmv = new Atributo("crmv", "int", true, false);
        
        atributos.add(id);
        atributos.add(nome);
        atributos.add(crmv);
        
        Tabela tCargo = new Tabela("Cargo", atributos, false);
        tabelas.add(tCargo);
        
        // Cor
        atributos = new ArrayList();
        atributos.add(id);
        atributos.add(nome);
        
        Tabela tCor = new Tabela("Cor", atributos, false);
        tabelas.add(tCor);
        
        // Espécie
        atributos = new ArrayList();
        
        Atributo especie = new Atributo("especie", "String", true, false);
        
        atributos.add(id);
        atributos.add(especie);
        
        Tabela tEspecie = new Tabela("Especie", atributos, false);
        tabelas.add(tEspecie);
        
        // Ala
        atributos = new ArrayList();
        atributos.add(id);
        atributos.add(nome);
        
        Tabela tAla = new Tabela("ala", atributos, false);
        tabelas.add(tAla);
        
        // EspecieAla
        atributos = new ArrayList();
        Atributo ala = new Atributo("ala", "Ala", true, true);
        Atributo especieFK = new Atributo("especie", "Especie", true, true);
        
        atributos.add(id);
        atributos.add(ala);
        atributos.add(especieFK);
        
        Tabela tEspecieAla = new Tabela("EspecieAla", atributos, false);
        tabelas.add(tEspecieAla);
        
        // Tipo_local
        atributos = new ArrayList();
        atributos.add(id);
        atributos.add(ala);
        atributos.add(nome);
        
        Tabela tTipoLocal = new Tabela("Tipo_local", atributos, false);
        tabelas.add(tTipoLocal);
        
        // Funcionarios
        Atributo enderecoFK = new Atributo("endereco", "Endereco", true, true);
        Atributo cargo = new Atributo("cargo", "Cargo", true, true);
        Atributo data_nascimento = new Atributo("data_nascimento", "Date", true, false);
        Atributo cpf = new Atributo("cpf", "String", true, false);
        Atributo rg = new Atributo("rg", "int", true, false);
        
        atributos.add(id);
        atributos.add(enderecoFK);
        atributos.add(cargo);
        atributos.add(nome);
        atributos.add(data_nascimento);
        atributos.add(cpf);
        atributos.add(rg);
        
        Tabela tFuncionarios = new Tabela("Funcionarios", atributos, false);
        tabelas.add(tFuncionarios);
        
        // localFuncionario
        atributos = new ArrayList();
        Atributo tipo_local = new Atributo("tipo_local", "Tipo_local", true, true);
        Atributo funcionarios = new Atributo("funcionarios", "Funcionarios", true, true);
        
        atributos.add(id);
        atributos.add(tipo_local);
        atributos.add(funcionarios);
        
        Tabela tLocalFuncionario = new Tabela("localFuncionario", atributos, false);
        tabelas.add(tLocalFuncionario);
        
        // Animal
        atributos = new ArrayList();
        
        Atributo cor = new Atributo("cor", "Cor", true, true);
        Atributo altura = new Atributo("altura", "double", true, false);
        
        atributos.add(id);
        atributos.add(tipo_local);
        atributos.add(ala);
        atributos.add(cor);
        atributos.add(especie);
        atributos.add(nome);
        atributos.add(altura);
        
        Tabela tAnimal = new Tabela("Animal", atributos, false);
        tabelas.add(tAnimal);
        
        // Consultas
        atributos = new ArrayList();
        
        Atributo animal = new Atributo("animal", "Animal", true, true);
        Atributo data_2 = new Atributo("data_2", "Date", true, false);
        Atributo diagnostico = new Atributo("diagnostico", "String", true, false);
        
        atributos.add(id);
        atributos.add(animal);
        atributos.add(funcionarios);
        atributos.add(data_2);
        atributos.add(diagnostico);
        
        Tabela tConsultas = new Tabela("Consultas", atributos, false);
        tabelas.add(tConsultas);
        
  /* 
Consultas
  id
  Animal_id
  Funcionarios_id
  data_2
  diagnostico
  */
        Banco.criarPOJO(tabelas);
        //Banco.criarDAO(tabelas);
    }
}
