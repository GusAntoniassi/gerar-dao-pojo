/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import objetos.banco.Dominio;
import sistema.Dominios;

/**
 *
 * @author cisa
 */
public class TelaDominios extends JDialog implements ActionListener {
    List<Dominio> dominiosBooleanos = new ArrayList();

    public static JFrame jfPai = new JFrame();
    private JPanel jpTabelaDominios = new JPanel();
    private JPanel jpBotoes = new JPanel();
    
    private JComponent[][] vetorTabela;
    
    private JButton jbConfirmar = new JButton("Confirmar");
    private JButton jbCancelar = new JButton("Cancelar");
    
    public TelaDominios(List<Dominio> dominios) {
        super(jfPai, "Resolver domínios booleanos", true);
        
        // Pegar apenas os domínios booleanos
        for (Dominio dominio : dominios) {
            if (dominio.tipo.equals("boolean")) {
                dominiosBooleanos.add(dominio);
            }
        }
        
        jfPai.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                jfPai.dispose();
            }
        });
        
        System.out.println("1");
        montarTabelaDominios();
        System.out.println("2");
        getContentPane().add("Center", jpTabelaDominios);
        getContentPane().add("South", jpBotoes);
        System.out.println("3");
        
        jpBotoes.setLayout(new FlowLayout(FlowLayout.TRAILING));
        
        jpBotoes.add(jbCancelar);
        jpBotoes.add(jbConfirmar);

        jbConfirmar.addActionListener(this);
        jbCancelar.addActionListener(this);
        
        System.out.println("4");
        pack();
        setVisible(true);
        System.out.println("5");
    }

    private void montarTabelaDominios() {
        // n linhas e 5 colunas, sendo elas:
        // nome | tipo java | valor p/ true | valor p/ false | inverter
        vetorTabela = new JComponent[dominiosBooleanos.size()+1][4];
        
        vetorTabela[0][0] = new JLabel("Nome");
        vetorTabela[0][1] = new JLabel("Tipo Java");
        vetorTabela[0][2] = new JLabel("Valor p/ true");
        vetorTabela[0][3] = new JLabel("Valor p/ false");
        
        for (int i = 1; i < dominiosBooleanos.size(); i++) {
            Dominio dominio = dominiosBooleanos.get(i);
            vetorTabela[i][0] = new JLabel(dominio.nome);
            vetorTabela[i][1] = new Dominios.ComboBoxTiposJava(dominio.tipo);
            vetorTabela[i][2] = new JTextField(dominio.V);
            vetorTabela[i][3] = new JTextField(dominio.F);
        }
        
        jpTabelaDominios.setLayout(new GridLayout(vetorTabela.length-1, 4));
        // Colocar os componentes no painel
        for (int i = 0; i < vetorTabela.length-1; i++) {
            for (int j = 0; j < 4; j++) {
                jpTabelaDominios.add(vetorTabela[i][j]);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbConfirmar) {
            this.dispose();
        } else if (e.getSource() == jbCancelar) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        Dominios.main(new String[1]);
    }
    
    private class TextFieldDominio extends JTextField {
        public TextFieldDominio(String valorPadrao, String tipoDominio) {
            super(valorPadrao, 6);
            // definir tipo de dados válido e valor máximo de caracteres dependendo do tipo 
        }
    }
}
