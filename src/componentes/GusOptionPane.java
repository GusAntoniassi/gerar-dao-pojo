/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import metodos.helper.StringHelper;

/**
 *
 * @author cisa
 */
public class GusOptionPane extends JOptionPane {
    public static void showInlineIconDialog(Component parentComponent, Object message, float alignment, ImageIcon... icones) {
        String mensagem = (String) message;
        
        if (!mensagem.contains("%icon") || icones == null) {
            throw new IllegalArgumentException("Não há nenhum %icon na mensagem");
        } else if (mensagem.contains("%icon") && (icones.length < StringHelper.contarOcorrencias(mensagem, "%icon"))) {
            throw new IllegalArgumentException("Quantidade de %icon na mensagem não bate com o tanto de ícones passados como parâmetro");
        }
        
        String[] linhas = mensagem.split("\\n");
        JPanel jpMensagem = new JPanel();
        jpMensagem.setLayout(new BoxLayout(jpMensagem, BoxLayout.Y_AXIS));
        
        JPanel[] jpLinhas = new JPanel[linhas.length];
        int indiceIcone = 0;
        
        for (int i = 0; i < linhas.length; i++) {
            jpLinhas[i] = new JPanel();
            jpLinhas[i].setLayout(new BoxLayout(jpLinhas[i], BoxLayout.LINE_AXIS));
                        
            if (StringHelper.contarOcorrencias(linhas[i], "%icon") > 0) {
                for (int j = 0; j < StringHelper.contarOcorrencias(linhas[i], "%icon"); j++) {
                    jpLinhas[i].add(new JLabel(linhas[i].substring(0, linhas[i].indexOf("%icon"))));
                    jpLinhas[i].add(new JLabel(icones[indiceIcone]));
                    linhas[i] = linhas[i].substring(linhas[i].indexOf("%icon") + "%icon".length());
                    indiceIcone++;
                }
                
                if (!linhas[i].isEmpty()) {
                    jpLinhas[i].add(new JLabel(linhas[i]));
                }
            } else {
                jpLinhas[i].add(new JLabel(linhas[i]));
            }
            
            jpMensagem.add(jpLinhas[i]);
        }
        
        JOptionPane.showMessageDialog(parentComponent, jpMensagem);
    }
    
    
    public static void showInlineIconDialog(Component parentComponent, Object message, ImageIcon... icones) {
        showInlineIconDialog(parentComponent, message, JComponent.LEFT_ALIGNMENT, icones);
    }
    
    public static void main(String[] args) {
        String pathErro = "img/erro.png";
        String pathAviso = "img/aviso.png";
        String pathCerto = "img/certo.png";
        
        GusOptionPane.showInlineIconDialog(null, "Nome(s) inválido(s) detectado(s)! Passe o mouse sobre o %icon para mais informações.", new ImageIcon(pathErro));
    }
}
