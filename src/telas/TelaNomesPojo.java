/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import sistema.NomesPojo;
import componentes.GusOptionPane;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.lang.model.SourceVersion;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.EmptyBorder;
import metodos.helper.StringHelper;
import tabela.NomesPojoTable;
import tabela.NomesPojoTableModel;

/**
 *
 * @author cisa
 */
public class TelaNomesPojo extends JDialog implements ActionListener {

    public static JFrame jfPai = new JFrame();

    private JLabel labelNorte;
    private JPanel jpSul = new JPanel();
    private JPanel jpBotoes = new JPanel();

    private JButton jbConcluir = new JButton("Concluir");
    private JButton jbCorrigir = new JButton("Corrigir");
    private JButton jbCancelar = new JButton("Cancelar");

    //private List<NomesPojoTableModel> dtms;
    private NomesPojoTableModel dtm;
    private NomesPojoTable tabela;

    private String pathErro = "img/erro.png";
    private String pathAviso = "img/aviso.png";
    private String pathCerto = "img/certo.png";
    //private ImageIcon certo = new ImageIcon("img/certo.png");

    public TelaNomesPojo(NomesPojoTableModel dtm) {
        super(jfPai, "Nomes não detectados", true);

        this.dtm = dtm;

        tabela = new NomesPojoTable(dtm);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        jfPai.setDefaultCloseOperation(EXIT_ON_CLOSE);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                jfPai.dispose();
            }
        });

        labelNorte = new JLabel("<html>Não foi possível detectar o nome do POJO para " + dtm.tipoDados + "  abaixo:"
        //+ ". "
        //+ "Informe um nome manualmente (no padrão PascalCase do Java)</html>");
        );

        JScrollPane jspTabela = new JScrollPane(tabela);
        tabela.setFillsViewportHeight(true);

        getContentPane().add("North", labelNorte);
        getContentPane().add("Center", jspTabela);
        getContentPane().add("South", jpSul);

        labelNorte.setBorder(new EmptyBorder(5, 5, 5, 0));
        labelNorte.setFont(new Font("Dialog", 0, 12));

        jpSul.setLayout(new BoxLayout(jpSul, BoxLayout.X_AXIS));
        jpSul.add(jpBotoes);

        jpBotoes.setLayout(new FlowLayout(FlowLayout.TRAILING));
        jpBotoes.add(jbCancelar);
        jpBotoes.add(jbCorrigir);
        jpBotoes.add(jbConcluir);

        jbCorrigir.setEnabled(false);
        jbCorrigir.setToolTipText("Corrige automaticamente os campos marcados com um aviso (amarelo)");

        jbConcluir.addActionListener(this);
        jbCorrigir.addActionListener(this);
        jbCancelar.addActionListener(this);
        
        tabela.changeSelection(0, 1, false, false);
        setSize(515, 450);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbConcluir) {
            if (validar()) {
                this.dispose();
            }
        } else if (e.getSource() == jbCorrigir) {
            corrigir();
        } else if (e.getSource() == jbCancelar) {
            System.exit(0);
        }
    }

    private boolean validar() {
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }

        dtm.statusLinha = new int[dtm.getRowCount()];
        dtm.statusDados = NomesPojoTableModel.VALIDO;

        for (int i = 0; i < dtm.getRowCount(); i++) {
            String valorLinha = ((String) dtm.getValueAt(i, 1)).trim();
            // Tirar os espaços do começo e do fim
            dtm.setValueAt(valorLinha, i, 1);
            valorLinha = (valorLinha == null) ? "" : valorLinha;

            String motivoInvalido = null;
            dtm.statusLinha[i] = NomesPojoTableModel.VALIDO;

            if (valorLinha.equals("")) {
                dtm.statusLinha[i] = NomesPojoTableModel.INVALIDO;
                motivoInvalido = "Não pode ficar vazio!";
            } else {
                if (valorLinha.toLowerCase().contains("id")) {
                    if ( // linha possui "id" seguido de outra palavra (ex idPais)
                            (valorLinha.substring(0, 2).equals("id")
                            && Character.isUpperCase(valorLinha.charAt(2)))
                            || // linha possui "Id" antecedido por outra palavra (ex paisId)
                            (valorLinha.substring(valorLinha.length() - 2, valorLinha.length()).equals("Id")
                            && Character.isLowerCase(valorLinha.charAt(valorLinha.length() - 3))
                            || (dtm.tipoDados.equals(NomesPojo.TABELAS) && Character.isUpperCase(valorLinha.charAt(valorLinha.length() - 3))))) {
                        dtm.statusLinha[i] = NomesPojoTableModel.AVISO;
                        motivoInvalido = "Não é recomendado utilizar \"id\" no nome d" + (dtm.tipoDados.replaceAll("s", "")) + ".";
                    }
                }

                if (dtm.tipoDados.equals(NomesPojo.TABELAS) && Character.isLowerCase(valorLinha.charAt(0))) {
                    dtm.statusLinha[i] = NomesPojoTableModel.AVISO;
                    motivoInvalido = "O nome corresponde a uma tabela, mas a primeira letra está minúscula";
                    //JOptionPane.showMessageDialog(null, "Aviso! " + valorLinha + " corresponde a um nome de tabela, mas está com a primeira letra minúscula");
                } else if (dtm.tipoDados.equals(NomesPojo.ATRIBUTOS) && Character.isUpperCase(valorLinha.charAt(0))) {
                    dtm.statusLinha[i] = NomesPojoTableModel.AVISO;
                    motivoInvalido = "O nome corresponde a um atributo, mas a primeira letra está maiúscula";
                    //JOptionPane.showMessageDialog(null, "Aviso! " + valorLinha + " corresponde a um nome de atributo, mas está com a primeira letra maiúscula");
                }

                if (valorLinha.contains(" ")) {
                    dtm.statusLinha[i] = NomesPojoTableModel.AVISO;
                    motivoInvalido = "A linha contém um espaço";
                    //JOptionPane.showMessageDialog(null, "Aviso! " + valorLinha + " possui um espaço!");
                }

                // Se não for um nome de variável válido
                if (!SourceVersion.isName(StringHelper.camelCase(valorLinha.replaceAll("\\s", "")))) {

                    //JOptionPane.showMessageDialog(null, "Inválido! Valor Inválido" + valorLinha);
                    dtm.statusLinha[i] = NomesPojoTableModel.INVALIDO;
                    motivoInvalido = "Não é um nome de variável válido!";
                    //dtmAtual.statusDados = dtmAtual.statusLinha[i];
                }

                if (dtm.statusLinha[i] != NomesPojoTableModel.INVALIDO) {
                    // Percorrer os valores acima para ver se tem algum repetido
                    for (int j = i - 1; j >= 0; j--) {
                        if (((String) dtm.getValueAt(i, 1)).toLowerCase().equals(((String) dtm.getValueAt(j, 1)).toLowerCase())) {

                            //JOptionPane.showMessageDialog(null, "Inválido! Valores iguais" + valorLinha + " e " + (String) dtmAtual.getValueAt(j, 1));
                            dtm.statusLinha[i] = NomesPojoTableModel.INVALIDO;
                            motivoInvalido = "Esse nome já foi utilizado na linha " + (j + 1);
                            break;
                        }
                    }
                }

                if (dtm.statusLinha[i] == NomesPojoTableModel.AVISO) {
                    jbCorrigir.setEnabled(true);
                }
            }

            switch (dtm.statusLinha[i]) {
                case NomesPojoTableModel.VALIDO:
                    System.out.println("valido");
                    dtm.setValueAt(new ImageIcon(pathCerto), i, 2);
                    dtm.setCorLinha(i, dtm.corValido);
                    break;
                case NomesPojoTableModel.AVISO:
                    System.out.println("aviso");
                    dtm.setValueAt(new ImageIcon(pathAviso, motivoInvalido), i, 2);
                    dtm.setCorLinha(i, dtm.corAviso);
                    break;
                case NomesPojoTableModel.INVALIDO:
                    dtm.setValueAt(new ImageIcon(pathErro, motivoInvalido), i, 2);
                    dtm.setCorLinha(i, dtm.corInvalido);
                    break;
                default:
                    break;
            }

            dtm.setTooltipAt(i, motivoInvalido);

            if (dtm.statusDados == NomesPojoTableModel.VALIDO) {
                dtm.statusDados = dtm.statusLinha[i];
            }
        }

        if (dtm.statusDados == NomesPojoTableModel.INVALIDO) {
            GusOptionPane.showInlineIconDialog(null, "Nome(s) inválido(s) detectado(s)! Passe o mouse sobre o %icon para mais informações.", new ImageIcon(pathErro));
        } else if (dtm.statusDados == NomesPojoTableModel.AVISO) {
            GusOptionPane.showInlineIconDialog(null, "Alguns nomes não estão totalmente corretos.\nClique em corrigir ou altere-os manualmente\n(passe o mouse sobre o %icon para mais informações)", new ImageIcon(pathAviso));
        }

        this.requestFocus();
        return dtm.statusDados == NomesPojoTableModel.VALIDO;
    }

    public void corrigir() {
        for (int i = 0; i < dtm.statusLinha.length; i++) {
            if (dtm.statusLinha[i] == NomesPojoTableModel.AVISO) {
                String valor = (String) dtm.getValueAt(i, 1);
                valor = valor.trim().replaceAll("\\s+", "");

                if (valor.toLowerCase().contains("id")) {
                    // linha possui "id" seguido de outra palavra (ex idPais)
                    if (valor.substring(0, 2).equals("id")
                            && Character.isUpperCase(valor.charAt(2))) {
                        valor = StringHelper.camelCase(valor.substring(2));
                    }

                    // linha possui "Id" antecedido por outra palavra (ex paisId)
                    if (valor.substring(valor.length() - 2, valor.length()).equals("Id")
                            && Character.isLowerCase(valor.charAt(valor.length() - 3))
                            || (dtm.tipoDados.equals(NomesPojo.TABELAS) && Character.isUpperCase(valor.charAt(valor.length() - 3)))) {
                        valor = valor.substring(0, valor.length() - 2);
                    }
                }

                if (dtm.tipoDados.equals(NomesPojo.TABELAS)) {
                    valor = StringHelper.pascalCase(valor);
                } else if (dtm.tipoDados.equals(NomesPojo.ATRIBUTOS)) {
                    valor = StringHelper.camelCase(valor);
                }

                dtm.setValueAt(valor, i, 1);
                dtm.setValueAt(new ImageIcon(pathCerto), i, 2);
                dtm.setCorLinha(i, dtm.corValido);
            }
        }

        dtm.statusDados = NomesPojoTableModel.VALIDO;
        jbCorrigir.setEnabled(false);
    }
}