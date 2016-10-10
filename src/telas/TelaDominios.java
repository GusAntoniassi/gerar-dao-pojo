/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import sistema.Dominios;
import tabela.DominiosTableModel;

/**
 *
 * @author cisa
 */
public class TelaDominios extends JDialog implements ActionListener {
    public static JFrame jfPai = new JFrame();
    private DominiosTableModel dtm;
    private JTable tabela;
    
    private JPanel jpBotoes = new JPanel();
    private JButton jbConcluir = new JButton("Concluir");
    private JButton jbCancelar = new JButton("Cancelar");
    
    public TelaDominios(DominiosTableModel dtm) {
        super(jfPai, "Domínios identificados", true);
        
        this.dtm = dtm;
        
        tabela = new JTable(dtm) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
        
        jfPai.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                jfPai.dispose();
            }
        });
        
        JScrollPane jspTabela = new JScrollPane(tabela);
        tabela.setFillsViewportHeight(true);
        
        getContentPane().add("Center", jspTabela);
        getContentPane().add("South", jpBotoes);
        
        jpBotoes.setLayout(new FlowLayout(FlowLayout.TRAILING));
        
        jpBotoes.add(jbCancelar);
        jpBotoes.add(jbConcluir);

        jbConcluir.addActionListener(this);
        jbCancelar.addActionListener(this);
        
        tabela.changeSelection(0, 1, false, false);
        setSize(515, 450);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbConcluir) {
            this.dispose();
        } else if (e.getSource() == jbCancelar) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        Dominios.main(args);
    }
}
