
import metodos.helper.StringHelper;
import metodos.sql.SQLInterpreter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cisa
 */
public class Teste {
    public static void main(String[] args) {
        SQLInterpreter.pegarDominioDaSQL("CREATE DOMAIN ABERTO_FECHADO AS CHAR(1) NOT NULL CHECK (VALUE IN ('A', 'F')");
    }
}
