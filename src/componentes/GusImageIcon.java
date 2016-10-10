/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import java.awt.event.MouseAdapter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author cisa
 */
public class GusImageIcon extends JLabel {    
    public GusImageIcon(String path) {
        super(new ImageIcon(path));
    }
    
    public GusImageIcon(String path, String hint) {
        this(path);
        this.setToolTipText(hint);
    }
    
    /*public static void main(String[] args) {
        JFrame jf = new javax.swing.JFrame("Teste");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        jf.add(new GusImageIcon("img/erro.png", "Se fudeu"));
        jf.setSize(100, 50);
        jf.setVisible(true);
    }*/
}
