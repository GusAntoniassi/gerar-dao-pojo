/*
 * TODO: mexer no alterar() pra ele pegar 1, 2, 3, 4 em vez de 2, 3, 4, 5 nos getters do PS
 */
package objetos.banco;

import java.util.List;
import metodos.helper.StringHelper;

/**
 *
 * @author cisa
 */
public class Banco {
    //public List<Tabela> tabelas = new ArrayList<Tabela>();
    public static String classeConexao = "Conexao";
    public static String pacoteConexao = "banco";
    public static String getConexao = "getConexao";
        
    public static String booleanSim = "S";
    public static String booleanNao = "N";    
    
    
    public static void criarPOJO(List<Tabela> tabelas) {
        for (Tabela tabela : tabelas) {
            // Criar o pacote
            System.out.println("package pojo;\n");
            // Criar a classe do objeto
            System.out.println("public class " + StringHelper.pascalCase(tabela.nome) + " {");
            
            // Criar todos os atributos passados
            for (Atributo atributo : tabela.atributos) {
                System.out.print("    " + atributo.tipo + " " + atributo.nome);
                if (atributo.pojo) {
                    System.out.println(" = new " + atributo.tipo + "();");
                } else {
                    System.out.println(";");
                }
            }
            
            // Pular uma linha entre os atributos e os métodos
            System.out.println("");
            
            // Criar getters e setters para cada atributo
            for (Atributo atributo : tabela.atributos) {
                // Se o atributo for boolean, o getter vai se chamar "isAtributo" em vez de "getAtributo"                
                montarMetodoPOJO(atributo, 
                                ((atributo.tipo == "boolean") ? "is" : "get"),
                                true);
                
                montarMetodoPOJO(atributo, "set", false);
                                
            }
            // Fechar a classe
            System.out.println("}");
        }
    }
    
    private static void montarMetodoPOJO(Atributo atributo, String prefixo, boolean getter) {   
        // Criar o cabeçalho do método
        System.out.print("    public " + ((!getter) ? "void" : atributo.tipo) + " " + prefixo
            // fazer camelCase depois do prefixo
            + StringHelper.pascalCase(atributo.nome));
        
        if (getter) {
            System.out.println("() {");            
            System.out.println("        return " + atributo.nome + ";");
        } else {
            System.out.println("(" + atributo.tipo + " " + atributo.nome + ") {");
            System.out.println("        this." + atributo.nome + " = " + atributo.nome + ";");
        }
        
        System.out.println("    }\n");
    }
    
    private static void criarSettersPS(int inicio, Tabela tabela) {
        int cont = 0;
        for (int i = inicio; i < tabela.atributos.size(); i++) {
                cont++;
                Atributo atributo = tabela.atributos.get(i);
                if (atributo.tipo == "boolean") {
                    System.out.print("            ps.setString(" + cont + ", (" + tabela.nome);
                    if (atributo.dominio != null) {
                        System.out.println(".is" + StringHelper.pascalCase(atributo.nome) + "() ? \"" + atributo.dominio.V + "\" : \"" + atributo.dominio.F + "\"));");
                    } else {
                        System.out.println(".is" + StringHelper.pascalCase(atributo.nome) + "() ? \"" + booleanSim + "\" : \"" + booleanNao + "\"));");
                    }
                } else if (atributo.pojo) {
                    System.out.print("            ps.setInt(" + cont + ", " + tabela.nome);
                    System.out.println(".get" + StringHelper.pascalCase(atributo.nome) + "().get" + StringHelper.pascalCase(tabela.atributos.get(0).nome) + "());");
                } else if (atributo.tipo == "date") {
                    System.out.print("            ps.set" + StringHelper.pascalCase(atributo.tipo) + "(" + cont + ", " + tabela.nome);
                    System.out.println("new java.sql.Date(Funcionarios.getData_nascimento().getTime()) .get" + StringHelper.pascalCase(atributo.nome) + "());");           
                } else {
                    System.out.print("            ps.set" + StringHelper.pascalCase(atributo.tipo) + "(" + cont + ", " + tabela.nome);
                    System.out.println(".get" + StringHelper.pascalCase(atributo.nome) + "());");
                }
        }
    }
    
    public static void criarDAO(List<Tabela> tabelas) {
        for (Tabela tabela : tabelas) {
            String nomeId = tabela.atributos.get(0).nome;
            
            String nomeTabelaCamel = tabela.nome;
            String nomeTabelaCaps = nomeTabelaCamel.toUpperCase();
            String nomeTabelaPascal = StringHelper.pascalCase(nomeTabelaCamel);
            
            // Criar o pacote
            System.out.println("package dao;\n");
            
            // Imports
            System.out.println("import " + pacoteConexao + "." + classeConexao + ";");
            System.out.println("import java.sql.PreparedStatement;");
            System.out.println("import java.sql.ResultSet;");
            System.out.println("import java.sql.Statement;");
            System.out.println("import java.sql.SQLException;");
            System.out.println("import javax.swing.JOptionPane;");
            System.out.println("import pojo." + nomeTabelaPascal + ";\n");
            
            // Criar a classe
            System.out.println("public class Dao" + nomeTabelaPascal + " {");
            // Criar o atributo do POJO
            System.out.println("    private " + nomeTabelaPascal + " " + nomeTabelaCamel + ";");
            // Criar a SQLINCLUIR, repetindo "?," conforme o número de atributos 
            System.out.println("    private final String SQLINCLUIR = \"INSERT INTO " + nomeTabelaCaps
                               + " VALUES (" + StringHelper.repetirStr("?,", tabela.atributos.size()-1) + "?)\";");
            // Criar a SQLALTERAR, com cada atributo seguido de um " = ?"
            System.out.print("    private final String SQLALTERAR = \"UPDATE " + nomeTabelaCaps
                               + " SET ");
            
            for (int i = 1; i < tabela.atributos.size(); i++) {
                Atributo atributo = tabela.atributos.get(i);
                
                if (atributo.pojo) {
                    // TODO: Alterar pra perguntar pro usuário qual o padrão de FK no banco
                    System.out.print("ID");
                }
                
                System.out.print(atributo.nome.toUpperCase() + " = ?");
                
                if (i != tabela.atributos.size()-1) {
                    System.out.print(", ");
                }
            }
            
            System.out.println(" WHERE " + nomeId.toUpperCase() + " = ?\";");
            
            // Criar o SQLDELETE    
            System.out.println("    private final String SQLEXCLUIR = \"DELETE FROM " + nomeTabelaCaps
                               + " WHERE " + nomeId.toUpperCase() + " = ?\";");
            // Criar o SQLCONSULTAR
            System.out.println("    private final String SQLCONSULTAR = \"SELECT * FROM " + nomeTabelaCaps
                               + " WHERE " + nomeId.toUpperCase() + " = ?\";\n");
            // Criar o construtor
            System.out.println("    public Dao" + nomeTabelaPascal + " (" + nomeTabelaPascal + " "
                               + nomeTabelaCamel + ") {");
            System.out.println("        this." + nomeTabelaCamel + " = " + nomeTabelaCamel + ";");
            System.out.println("    }\n");
            // Criar o método de pegar generator
            System.out.println("    private Integer pegarGenerator() {");
            System.out.println("        try {");
            System.out.println("            Statement st = " + classeConexao + "." + getConexao + "().createStatement();");
            System.out.println("            ResultSet rs = st.executeQuery(\"SELECT GEN_ID(GEN_" + nomeTabelaCaps + ", 1) "
                                            + "FROM RDB$DATABASE\");");
            System.out.println("            rs.next();");
            System.out.println("            return rs.getInt(1);");
            System.out.println("        } catch (Exception e) {");
            System.out.println("            e.printStackTrace();");
            System.out.println("            JOptionPane.showMessageDialog(null, \"Não foi possível obter o generator de " 
                                            + nomeTabelaPascal + ".\");");
            System.out.println("            return null;");
            System.out.println("        }");
            System.out.println("    }\n");
            
            // Criar o método incluir
            System.out.println("    public boolean incluir() {");
            System.out.println("        try {");
            System.out.println("            PreparedStatement ps = " + classeConexao + "." + getConexao 
                                            + "().prepareStatement(SQLINCLUIR);");
            System.out.println("            " + nomeTabelaCamel + ".set" + StringHelper.pascalCase(nomeId) + "(pegarGenerator());");
            
            criarSettersPS(0, tabela);
            
            System.out.println("            ps.executeUpdate();");
            System.out.println("            return true;");
            System.out.println("        } catch (SQLException e) {");
            System.out.println("            e.printStackTrace();");
            System.out.println("            JOptionPane.showMessageDialog(null, \"Não foi possível fazer a inclusão de " + 
                                            StringHelper.pascalCase(tabela.nome) + ".\");");
            System.out.println("            return false;");
            System.out.println("        }");
            System.out.println("    }\n");
            
            // Criar o método alterar
            System.out.println("    public boolean alterar() {");
            System.out.println("        try {");
            System.out.println("            PreparedStatement ps = " + classeConexao + "." + getConexao + "().prepareStatement(SQLALTERAR);");
            
            criarSettersPS(1, tabela);
            
            System.out.println("            ps.setInt(" + tabela.atributos.size() + ", " + nomeTabelaCamel + ".get" + StringHelper.pascalCase(nomeId) + "());");
            System.out.println("            ps.executeUpdate();");
            System.out.println("            return true;");
            System.out.println("        } catch (SQLException e) {");
            System.out.println("            e.printStackTrace();");
            System.out.println("            JOptionPane.showMessageDialog(null, \"Não foi possível fazer a alteração de " + 
                                            StringHelper.pascalCase(tabela.nome) + ".\");");
            System.out.println("            return false;");
            System.out.println("        }");
            System.out.println("    }\n");
            
            // Criar o método de excluir
            System.out.println("    public boolean excluir() {");
            System.out.println("        try {");
            System.out.println("            PreparedStatement ps = " + classeConexao + "." + getConexao + "().prepareStatement(SQLEXCLUIR);");
            System.out.println("            ps.setInt(1, " + nomeTabelaCamel + ".get" + StringHelper.pascalCase(nomeId) + "());");
            System.out.println("            ps.executeUpdate();");
            System.out.println("            return true;");
            System.out.println("        } catch (SQLException e) {");
            System.out.println("            e.printStackTrace();");
            System.out.println("            JOptionPane.showMessageDialog(null, \"Não foi possível fazer a alteração de " + 
                                            StringHelper.pascalCase(tabela.nome) + ".\");");
            System.out.println("            return false;");
            System.out.println("        }");
            
            // Criar o método de consultar
            System.out.println("    public boolean consultar() {");
            System.out.println("        try {");
            System.out.println("            PreparedStatement ps = " + classeConexao + "." + getConexao + "().prepareStatement(SQLCONSULTAR);");
            System.out.println("            ps.setInt(1, " + nomeTabelaCamel + ".get" + StringHelper.pascalCase(nomeId) + "());");
            System.out.println("            ResultSet rs = ps.executeQuery();");
            System.out.println("            if (rs.next()) {");
            
            for (int i = 1; i < tabela.atributos.size(); i++) {
                    Atributo atributo = tabela.atributos.get(i);
                    
                    if (atributo.pojo) {
                        System.out.println("                " + nomeTabelaCamel + ".get" + StringHelper.pascalCase(atributo.tipo) + "().setId(rs.getInt(" + (i+1) + "));");
                        System.out.println("                Dao" + StringHelper.pascalCase(atributo.tipo) + " dao" + atributo.tipo + " = new Dao" 
                                + StringHelper.pascalCase(atributo.tipo) + "(" + nomeTabelaCamel + ".get" + StringHelper.pascalCase(atributo.tipo) + "());");
                        System.out.println("                dao" + atributo.tipo + ".consultar();");
                        System.out.println("            }");
                        System.out.println("            return true;");
                    } else {
                        System.out.print("                " + tabela.nome + ".set" + StringHelper.pascalCase(atributo.nome));

                        if (atributo.tipo == "boolean") {
                            if (atributo.dominio != null) {
                                System.out.println("(rs.getString(" + (i+1) + ").equals(\"" + atributo.dominio.V + "\"));");
                            } else {
                                System.out.println("(rs.getString(" + (i+1) + ").equals(\"" + booleanSim + "\"));");
                            }
                        } else {
                            System.out.println("(rs.get" + StringHelper.pascalCase(atributo.tipo) + "(" + (i+1) + "));");
                        }
                    }
            }
            
            System.out.println("        } catch (SQLException e) {");
            System.out.println("            e.printStackTrace();");
            System.out.println("            JOptionPane.showMessageDialog(null, \"Não foi possível fazer a consulta de " + 
                                            StringHelper.pascalCase(tabela.nome) + ".\");");
            System.out.println("            return false;");
            System.out.println("        }");
            System.out.println("    }");
            System.out.println("}");
        }
        
    }
}
