/**
 * @todo
 *
 * A IMPLEMENTAR:
 * Interface Gráfica
 * Seletor de prefixo/sufixo pra código (ex "codPais", "idPais", "fkPais")
 * Seletor de padrão para separação de palavras (ex "id_pais", "id-pais")
 */
package metodos.sql;

import com.stibocatalog.hunspell.Hunspell;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import metodos.helper.StringHelper;
import objetos.banco.Atributo;
import objetos.banco.Tabela;
import sistema.NomesPojo;

/**
 *
 * @author cisa
 */
public class SQLInterpreter {

    private static Hunspell.Dictionary d = null;
    private static File arquivo;
    private static BufferedReader br;

    public static final int UNDERLINE = 1;
    public static final int COLADO = 2;

    private static String separadoParaCamel(String str, CharSequence separador, CharSequence strId) {
        String[] palavras = str.split((String) separador);

        // Colocar a primeira palavra em letra minúscula
        String palavra = palavras[0].toLowerCase();

        // Se a primeira palavra for "id", e tiver mais palavras depois dela, removemos o "id"
        if (palavra.equals("id") && palavras.length > 1) {
            palavra = "";
        }

        // Colocar cada palavra em maiúscula
        for (int i = 1; i < palavras.length; i++) {
            // Como o atributo da FK vai ser um objeto da tabela, e não um int com o Id, não precisamos colocar Id no nome
            if (!palavras[i].equals(strId)) {
                palavra += StringHelper.pascalCase(palavras[i]);
            }
        }

        // Transformar em camelCase novamente para garantir
        return StringHelper.camelCase(palavra);
    }

    private static String separadoParaPascal(String str, CharSequence separador, CharSequence strId) {
        return StringHelper.pascalCase(separadoParaCamel(str, separador, strId));
    }

    private static String coladoParaCamel(String str, CharSequence strId) {
        // Tirar todos os espaços em branco
        str = str.trim().replaceAll("\\s+", "");

        if (d != null && d.misspelled(str)) {
            for (String sugestao : d.suggest(str)) {
                if (sugestao.replaceAll("\\s", "").equals(str)) {
                    // Combinação perfeita
                    String[] palavras = sugestao.split("\\s+");

                    // Remover o "id" do começo e do fim do atributo
                    if (palavras[0].equals(strId)) {
                        palavras[0] = "";
                    } else if (palavras[palavras.length - 1].equals(strId)) {
                        palavras[palavras.length - 1] = "";
                    }

                    String palavra = palavras[0];

                    for (int i = 1; i < palavras.length; i++) {
                        palavra += StringHelper.pascalCase(palavras[i]);
                    }

                    // Transformar em camelCase novamente para garantir
                    return StringHelper.camelCase(palavra);
                }
            }
            // Se chegou aqui e não retornou, então não encontrou a sugestão perfeita
            return null;
        } else {
            return str;
        }
    }

    private static String coladoParaPascal(String str, CharSequence strId) {
        return StringHelper.pascalCase(coladoParaCamel(str, strId));
    }

    public static String tipoSQLParaJava(String linha) {
        // Jogar uma exceção se encontrar um campo do tipo array (ex. "INTEGER [4]")
        if (linha.length() > 2 && linha.matches("\\[\\d+\\]")) {
            throw new UnsupportedOperationException("Não há suporte para campos do tipo array.");
        }

        String tipoAtributo = null;

        String[] palavras = linha.split(" ");

        for (int i = 0; i < palavras.length; i++) {
            switch (palavras[i].toUpperCase()) {
                case "BIGINT":
                    tipoAtributo = "BigInteger";
                    break;
                case "BLOB":
                    // Se for um BLOB SUBTYPE TEXT, podemos usar uma string mesmo
                    if (palavras.length > i + 2 && palavras[i + 2].equals("TEXT")) {
                        tipoAtributo = "String";
                        // Para todos os outros tipos, usaremos a classe Blob do java.
                    } else {
                        tipoAtributo = "Blob";
                    }

                    break;
                case "CHAR":
                case "CHARACTER":
                    // Se for um atributo do tipo CHARACTER VARYING
                    if (palavras.length > i + 1 && palavras[i + 1].equals("VARYING")) {
                        tipoAtributo = "String";
                    } else {
                        tipoAtributo = "char";
                    }
                    break;
                case "DATE":
                    tipoAtributo = "Date";
                    break;
                case "DOUBLE":
                case "FLOAT":
                    tipoAtributo = "BigDecimal";
                    break;
                case "INTEGER":
                case "INT":
                case "SMALLINT":
                    tipoAtributo = "int";
                    break;
                case "TIME":
                    tipoAtributo = "Time";
                    break;
                case "TIMESTAMP":
                    tipoAtributo = "Timestamp";
                    break;

                default:
                    // Se for um valor com número, ex VARCHAR(80), ele não vai cair no case
                    if (palavras[i].contains("(")) {
                        if ((palavras[i]).contains("CHAR(") || (palavras[i]).contains("CHARACTER(")) {
                            if (palavras[i].charAt(palavras[i].indexOf("(") + 1) == '1') {
                                tipoAtributo = "char";
                            } else {
                                tipoAtributo = "String";
                            }
                            break;
                        } else if ((palavras[i]).contains("VARCHAR(")) {
                            tipoAtributo = "String";
                            break;
                        } else if ((palavras[1]).contains("DECIMAL(")
                                || (palavras[1]).contains("NUMERIC(")) {
                            tipoAtributo = "BigDecimal";
                            break;
                        }
                    }

                    // Se chegou no "NOT NULL" ou no "PRIMARY KEY", pular
                    if (palavras[i].equals("NOT") || palavras[i].equals("PRIMARY")) {
                        break;
                    }

                    // Ir para a próxima palavra
                    continue;

            }

            break;
        }

        return tipoAtributo;
    }

    private static void debugarTabelas(List<Tabela> tabelas) {
        for (Tabela t : tabelas) {
            System.out.println("*************");
            System.out.println(t.nome);

            for (Atributo a : t.atributos) {
                System.out.print("- " + a.nome + " - " + a.tipo);
                if (a.pojo) {
                    System.out.print(" (pojo)");
                }
                System.out.println("");
            }
        }
    }

    private static String pegarNomeTabelaDaSQL(String linha) {
        return StringHelper.linhaDepoisDe(linha, "CREATE TABLE ") // pegar a linha após o CREATE TABLE
                .split(" ")[0]; // Dividir a linha gerada com base nos espaços, e pegar o primeiro elemento
    }

    private static String pegarNomeAtributoFKDaSQL(String linha) {
        return StringHelper.linhaAntesDe(StringHelper.linhaDepoisDe(linha, "FOREIGN KEY "), " ") // Pegar a linha entre o "FOREIGN KEY" e o primeiro espaço depois do nome da FK
                .trim().replace("\\s+", "").replaceAll("\\(", "").replaceAll("\\)", ""); // Tirar todos os espaços e parenteses
    }

    private static String pegarNomeTabelaFKDaSQL(String linha) {
        String nomeTabela = StringHelper.linhaAntesDe(StringHelper.linhaDepoisDe(linha, "REFERENCES ").trim(), " "); // Pegar a linha entre o "REFERENCES" e o primeiro espaço depois do nome da tabela

        // O SQL aceita tanto "REFERENCES tabela (id)" como "REFERENCES tabela(id)", então temos que ver se tem um parêntese no que pegamos
        if (nomeTabela.contains("(")) {
            nomeTabela = StringHelper.linhaAntesDe(nomeTabela, "(").trim();
        }

        return nomeTabela;
    }

    public static List<Tabela> interpretarSQLSeparado(CharSequence separador, CharSequence strId) {
        List<Tabela> tabelas = new ArrayList();

        try {
            br = new BufferedReader(new FileReader(arquivo));

            String linha;
            // Percorrer todas as linhas do script
            while ((linha = br.readLine()) != null) {
                // Se a linha for um CREATE TABLE
                if (linha.toUpperCase().contains("CREATE TABLE")) {
                    Tabela tabela = new Tabela();
                    // Pegar o nome da tabela                    
                    tabela.nome = separadoParaPascal(pegarNomeTabelaDaSQL(linha).toLowerCase(), separador, strId);

                    List<Atributo> atributos = new ArrayList();

                    // Pegar os atributos
                    while (!(linha = br.readLine()).contains(";")) { // Enquanto não chegar no ";", ainda é um statement SQL
                        // Ir pra próxima linha se for uma linha vazia
                        if (linha.trim().replaceAll("\\s+", "").equals("")) {
                            continue;
                        } else if (linha.contains("FOREIGN KEY")) {
                            String tabelaFK = separadoParaPascal(pegarNomeTabelaFKDaSQL(linha).toLowerCase(), separador, strId);
                            String atributoFK = separadoParaCamel(pegarNomeAtributoFKDaSQL(linha).toLowerCase(), separador, strId);

                            // Percorrer toda a lista de atributos até achar o atributo referenciado na FK
                            for (Atributo attr : atributos) {
                                if (attr.nome != null && attr.nome.equals(atributoFK)) {
                                    attr.pojo = true;
                                    attr.tipo = tabelaFK;
                                    break;
                                }
                            }

                            // Ir para a próxima linha
                            continue;
                        }

                        Atributo atributo = new Atributo();

                        String linhaAtributo = linha.trim().replaceAll("\\s+", " ").replaceAll(",", "");

                        // Se o padrão de palavras compostas no banco for separar por underline
                        atributo.nome = separadoParaCamel(linhaAtributo.split(" ")[0].toLowerCase(), separador, strId);

                        // Pegar o tipo do atributo
                        if (linhaAtributo.length() > 1) {
                            atributo.tipo = tipoSQLParaJava(linhaAtributo);
                        }

                        //System.out.println("Atributo: " + atributo.nome + "\nTipo: " + atributo.tipo);
                        atributos.add(atributo);
                    }
                    tabela.atributos = atributos;
                    tabelas.add(tabela);
                    //System.out.println(linha);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return tabelas;
    }

    public static List<Tabela> interpretarSQLColado(CharSequence strId) {
        String dir = System.getProperty("user.dir");
        dir = dir + File.separator + "dict" + File.separator + "pt_BR";

        try {
            d = Hunspell.getInstance().getDictionary(dir);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        List<Tabela> tabelas = new ArrayList();

        try {
            String linha;
            br = new BufferedReader(new FileReader(arquivo));
            List<String> nomesNaoIdentificados = new ArrayList();

            // Percorrer todas as linhas do script procurando por nomes de tabelas
            while ((linha = br.readLine()) != null) {
                if (linha.toUpperCase().contains("CREATE TABLE")) {
                    Tabela tabela = new Tabela();

                    // Pegar o nome da tabela
                    String nomeTabela = coladoParaPascal(pegarNomeTabelaDaSQL(linha).toLowerCase(), strId);

                    //System.out.println(nomeTabela);
                    if (nomeTabela == null) {
                        nomeTabela = pegarNomeTabelaDaSQL(linha);
                        nomesNaoIdentificados.add(nomeTabela);
                    }

                    tabela.nome = nomeTabela;
                    tabelas.add(tabela);
                }
            }

            HashMap<String, Integer> mapaTabelasNaoIdentificadas = new HashMap();
            //List<String> mapaTabelasNaoIdentificadas = new ArrayList();
            // Se teve algum nome que não conseguiu identificar automaticamente
            if (nomesNaoIdentificados.size() > 0) {
                List<String> nomesIdentificados = NomesPojo.identificarNomes(nomesNaoIdentificados, NomesPojo.TABELAS);

                if (nomesIdentificados != null) {
                    for (int i = 0; i < nomesNaoIdentificados.size(); i++) {
                        int indiceTabela = -1;

                        for (int j = 0; j < tabelas.size(); j++) {
                            if (tabelas.get(j).nome.equals(nomesNaoIdentificados.get(i))) {
                                indiceTabela = j;
                                break;
                            }
                        }

                        tabelas.get(indiceTabela).nome = nomesIdentificados.get(i);
                        mapaTabelasNaoIdentificadas.put(nomesNaoIdentificados.get(i), indiceTabela);

                        //mapaTabelasNaoIdentificadas.get(mapaTabelasNaoIdentificadas.indexOf())put(nomesNaoIdentificados.get(i), indiceTabela);
                    }
                }
            }

            // Agora que já temos os nomes das tabelas, vamos pegar os atributos de cada tabela
            br = new BufferedReader(new FileReader(arquivo));

            // Contador que indicará em qual tabela estamos (será incrementado cada vez que encontrarmos um statement CREATE TABLE
            int contador = 0;

            List<String> atributosNaoIdentificados = new ArrayList();
            // Percorrer todas as linhas do script            
            while ((linha = br.readLine()) != null) {
                if (linha.toUpperCase().contains("CREATE TABLE")) {
                    Tabela tabela = tabelas.get(contador);

                    List<Atributo> atributos = new ArrayList();
                    // Pegar todos os nomes de atributos

                    while ((!(linha = br.readLine()).contains(";"))) { // Enquanto não chegar no ";", ainda é um statement SQL
                        if (linha.trim().replaceAll("\\s+", "").equals("")) {
                            continue;
                        } else if (linha.contains("FOREIGN KEY")) {

                            // Pegar o nome da tabela
                            String tabelaFK = coladoParaPascal(pegarNomeTabelaFKDaSQL(linha).toLowerCase(), strId);
                            if (tabelaFK == null) {
                                tabelaFK = tabelas.get(mapaTabelasNaoIdentificadas.get(pegarNomeTabelaFKDaSQL(linha))).nome;
                            }

                            String atributoFK = coladoParaCamel(pegarNomeAtributoFKDaSQL(linha).toLowerCase(), strId);
                            String atributoNaoIdentificado = null;
                            if (atributoFK == null) {
                                atributoNaoIdentificado = pegarNomeAtributoFKDaSQL(linha);

                                if (!atributosNaoIdentificados.contains(atributoNaoIdentificado)) {
                                    atributosNaoIdentificados.add(atributoNaoIdentificado);
                                }
                            }

                            // Percorrer toda a lista de atributos até achar o atributo referenciado na FK
                            for (Atributo attr : atributos) {
                                if (attr.nome != null) {
                                    if (atributoFK == null && attr.nome.equals(atributoNaoIdentificado)) {
                                        attr.pojo = true;
                                        attr.tipo = tabelaFK;
                                        break;
                                    } else if (atributoFK != null && attr.nome.equals(atributoFK)) {
                                        attr.pojo = true;
                                        attr.tipo = tabelaFK;
                                        break;
                                    }
                                }
                            }

                            // Ir para a próxima linha
                            continue;
                        }

                        Atributo atributo = new Atributo();

                        String linhaAtributo = linha.trim().replaceAll("\\s+", " ").replaceAll(",", "");

                        atributo.nome = coladoParaCamel(linhaAtributo.split(" ")[0].toLowerCase(), strId);

                        if (atributo.nome == null) {
                            atributo.nome = linhaAtributo.split(" ")[0];
                            tabela.temAtributosNaoIdentificados = true;
                            
                            if (!atributosNaoIdentificados.contains(atributo.nome)) {
                                atributosNaoIdentificados.add(atributo.nome);
                            }
                        }

                        // Pegar o tipo do atributo
                        if (linhaAtributo.length() > 1) {
                            atributo.tipo = tipoSQLParaJava(linhaAtributo);
                        }

                        //System.out.println("Atributo: " + atributo.nome + "\nTipo: " + atributo.tipo);
                        atributos.add(atributo);
                    }

                    tabela.atributos = atributos;
                    contador++;
                }
            }

            if (!atributosNaoIdentificados.isEmpty()) {
                List<String> atributosIdentificados = NomesPojo.identificarNomes(atributosNaoIdentificados, NomesPojo.ATRIBUTOS);

                if (atributosIdentificados != null) {
                    for (int i = 0; i < atributosNaoIdentificados.size(); i++) {
                        for (Tabela tabela : tabelas) {
                            for (Atributo atributo : tabela.atributos) {
                                if (atributo.nome.equals(atributosNaoIdentificados.get(i))) {
                                    atributo.nome = atributosIdentificados.get(i);
                                }
                            }
                        }
                    }
                }
            }

            debugarTabelas(tabelas);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return tabelas;
    }

    public static void main(String[] args) {
        int padraoPalavras = COLADO;

        if (padraoPalavras == COLADO) {

        }
        
        try {
            //arquivo = new File(SQLInterpreter.class.getResource("/scripts/colado.sql").toURI());
            arquivo = new File(SQLInterpreter.class.getResource("/scripts/underline.sql").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //List<Tabela> tabelas = interpretarSQLColado("id");
        List<Tabela> tabelas = interpretarSQLSeparado("_", "id");

        debugarTabelas(tabelas);

    }
}
