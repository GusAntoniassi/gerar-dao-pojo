/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.*;

/**
 *
 * @author cisa
 */
public class TelaSistema extends JFrame {
    private JPanel jpNorte = new JPanel();
    private JTextField jtfFilePath = new JTextField(40);
    private JButton jbFileChooser = new JButton("...");
    private JFileChooser jfc = new JFileChooser();
    
    private JPanel jpSul = new JPanel();
    private JTextArea jtaStatus = new JTextArea();
    
    
    public TelaSistema() {
        setTitle("Gerador de DAO e POJO");
        
        getContentPane().add("North", jpNorte);
        getContentPane().add("South", jpSul);
        
        jpNorte.setLayout(new FlowLayout(FlowLayout.LEFT));
        jpNorte.add(jtfFilePath);
        jpNorte.add(jbFileChooser);
        
        setSize(600, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new TelaSistema();
        
    }
}