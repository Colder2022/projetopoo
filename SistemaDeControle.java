import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SistemaDeControle {

    private JFrame frame;
    private JPasswordField senhaField;
    private JButton entradaButton;
    private JButton saidaButton;
    private DatabaseHandler dbHandler;

    public SistemaDeControle() {
        try {
            dbHandler = new DatabaseHandler("MeuBancoSistema.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private DefaultTableModel tableModel = new DefaultTableModel(
            new Object[][] { { "Luiz", "123.456.789-00", "1234" },
                    { "Gustavo", "987.654.321-00", "567" } },
            new Object[] { "Nome", "CPF", "Senha" });

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SistemaDeControle().criarGUI();
            }
        });
    }

    private void criarGUI() {
        frame = new JFrame("Sistema de Controle de Ponto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel senhaLabel = new JLabel("Inserir Senha:");
        panel.add(senhaLabel);

        senhaField = new JPasswordField(10);
        senhaField.setPreferredSize(new Dimension(senhaField.getPreferredSize().width, 50));
        panel.add(senhaField);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        entradaButton = new JButton("Registrar Entrada");
        entradaButton.setBackground(Color.GREEN);
        entradaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarPonto("Entrada");
            }
        });
        saidaButton = new JButton("Registrar Saída");
        saidaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarPonto("Saída");
            }
        });
        botoesPanel.add(entradaButton);
        botoesPanel.add(saidaButton);
        panel.add(botoesPanel);

        JButton configuracoesButton = new JButton("Configurações");
        configuracoesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirLogin();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(configuracoesButton);
        panel.add(buttonPanel);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private void registrarPonto(String tipoPonto) {
        String senha = new String(senhaField.getPassword());
        if (senha.equals("senha123")) {
            JOptionPane.showMessageDialog(frame, "Ponto " + tipoPonto + " registrado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(frame, "Senha incorreta.");
        }
    }

    private void abrirLogin() {
        JFrame loginFrame = new JFrame("Login de Administrador");
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usuarioLabel = new JLabel("Usuário:");
        panel.add(usuarioLabel);

        JTextField usuarioField = new JTextField(10);
        panel.add(usuarioField);

        JLabel senhaLabel = new JLabel("Senha:");
        panel.add(senhaLabel);

        JPasswordField senhaField = new JPasswordField(10);
        panel.add(senhaField);

        JButton logarButton = new JButton("Logar");
        logarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                String senha = new String(senhaField.getPassword());
                if (usuario.equals("admin") && senha.equals("admin")) {
                    abrirConfiguracoes();
                    loginFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Usuário ou senha incorretos.");
                }
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(logarButton);
        panel.add(buttonPanel);

        loginFrame.getContentPane().add(panel);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(frame);
        loginFrame.setVisible(true);
    }

    private void abrirConfiguracoes() {
        JFrame configFrame = new JFrame("Configurações");
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton adicionarButton = new JButton("Adicionar");
        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarUsuario(tableModel, table);
            }
        });
        JButton editarButton = new JButton("Editar");
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarUsuario(table);
            }
        });
        JButton excluirButton = new JButton("Excluir");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirUsuario(tableModel, table);
            }
        });
        buttonPanel.add(adicionarButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(excluirButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        configFrame.getContentPane().add(panel);
        configFrame.pack();
        configFrame.setLocationRelativeTo(frame);
        configFrame.setVisible(true);
    }

    private void adicionarUsuario(DefaultTableModel model, JTable table) {
        // Adicionar um novo usuário com valores padrão na tabela
        Object[] novoUsuario = { "", "", "" };
        model.addRow(novoUsuario);

        // Obter a linha recém-adicionada
        int novaLinha = model.getRowCount() - 1;

        // Obter os valores do novo usuário
        String nome = (String) table.getValueAt(novaLinha, 0);
        String cpf = (String) table.getValueAt(novaLinha, 1);
        String senha = (String) table.getValueAt(novaLinha, 2);

        // Adicionar o novo usuário no banco de dados
        try {
            dbHandler.addUser(nome, cpf, senha);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editarUsuario(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um usuário para editar.");
            return;
        }
        String nome = (String) table.getValueAt(selectedRow, 0);
        String cpf = (String) table.getValueAt(selectedRow, 1);
        String senha = (String) table.getValueAt(selectedRow, 2);

        try {
            dbHandler.updateUser(nome, cpf, senha);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void excluirUsuario(DefaultTableModel model, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um usuário para excluir.");
            return;
        }
        String nome = (String) table.getValueAt(selectedRow, 0);

        try {
            dbHandler.deleteUser(nome);
            model.removeRow(selectedRow);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}