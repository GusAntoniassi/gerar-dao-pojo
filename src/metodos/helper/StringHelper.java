/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodos.helper;

/**
 *
 * @author cisa
 */
public class StringHelper {
    public static String pascalCase(String str) {
        if (str == null) {
            return null;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }
    
    public static String camelCase(String str) {
        if (str == null) {
            return null;
        } else {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        }
    }
    
    public static String repetirStr(String str, int vezes) {
        String strFinal = "";
        for (int i = 0; i < vezes; i++) {
            strFinal += str;
        }
        
        return strFinal;
    }
    
    public static String linhaDepoisDe(String str, String trecho) {
        if (!str.contains(trecho)) {
            return str;
        } else {
            return str.substring(str.indexOf(trecho) + trecho.length());
        }
    }
    
    public static String linhaAntesDe(String str, String trecho) {
        if (!str.contains(trecho)) {
            return str;
        } else {
            return str.substring(0, str.indexOf(trecho));
        }
    }
    
    public static int indexOfPrimeiraMaiuscula(String str) {
        for (int i = str.length() - 1; i >= 0; i--) {
            if (Character.isUpperCase(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOfUltimaMaiuscula(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
               return i;
            }
        }
        return -1;
    }
    
    public static int contarOcorrencias(String s, CharSequence cs) {
        if (cs.length() == 0) {
            return s.length();
        } else {
            return (s.length() - s.replace(cs, "").length()) / cs.length(); 
        }
    }
    
    public static char charAntecessor(String s, CharSequence cs, int ocorrencia) {
        if (cs.length() == 0) {
            return Character.MIN_VALUE;
        } else {
            if (ocorrencia <= 0) {
                ocorrencia = 1;
            } else if (ocorrencia > contarOcorrencias(s, cs)) {
                ocorrencia = contarOcorrencias(s, cs);
            }

            int indice = -(cs.length());
            for (int i = 0; i < ocorrencia; i++) {
                indice = s.indexOf((String) cs, indice+cs.length());
            }

            int posicaoChar = s.indexOf((String) cs, indice)-1;
            if (posicaoChar == -1) {
                return Character.MIN_VALUE;
            } else {
                return s.charAt(posicaoChar);
            }
        }
    }
    
    public static char charAntecessor(String s, CharSequence cs) {
        return charAntecessor(s, cs, 1);
    }
    
    public static char charSucessor(String s, CharSequence cs, int ocorrencia) {
        if (cs.length() == 0) {
            return Character.MIN_VALUE;
        } else {
            if (ocorrencia <= 0) {
                ocorrencia = 1;
            } else if (ocorrencia > contarOcorrencias(s, cs)) {
                ocorrencia = contarOcorrencias(s, cs);
            }

            int indice = -(cs.length());
            for (int i = 0; i < ocorrencia; i++) {
                indice = s.indexOf((String) cs, indice+cs.length());
            }
                
            int posicaoChar = s.indexOf((String) cs, indice)+cs.length();
            if (posicaoChar == -1 || posicaoChar >= s.length()) {
                return Character.MIN_VALUE;
            } else {
                return s.charAt(posicaoChar);
            }
        }
    }
    
    public static Character charSucessor(String s, CharSequence cs) {
        return charSucessor(s, cs, 1);
    }
}
