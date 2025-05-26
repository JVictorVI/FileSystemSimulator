package com.filesystem;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        setTitle("Gerenciador de Arquivos");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centraliza

        initComponents();
    }

    private void initComponents() {

    FileSystemSimulator fileSystem = new FileSystemSimulator();
    fileSystem.replayJournal();

    // Painel principal do JFrame com BorderLayout
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Menu de navegação
    JMenuBar navigationBar = new JMenuBar();

    JMenuItem createFolderItem = new JMenuItem("Criar Pasta");
    createFolderItem.addActionListener(e -> {
        String folderName = JOptionPane.showInputDialog(this, "Digite o nome da nova pasta:");
        if (folderName != null && !folderName.trim().isEmpty()) {
            fileSystem.createDirectory("root", folderName);
            JOptionPane.showMessageDialog(this, "Pasta criada: " + folderName);
        } else {
            JOptionPane.showMessageDialog(this, "Nome inválido para a pasta.");
        }
    });

    JMenuItem createFileItem = new JMenuItem("Criar Arquivo");
    createFileItem.addActionListener(e -> {
        String fileName = JOptionPane.showInputDialog(this, "Digite o nome do novo arquivo:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            fileSystem.createFile("root", fileName);
            JOptionPane.showMessageDialog(this, "Arquivo criado: " + fileName);
        } else {
            JOptionPane.showMessageDialog(this, "Nome inválido para o arquivo.");
        }
    });

    JMenuItem listDirectoryItem = new JMenuItem("Listar Diretório");

    navigationBar.add(createFileItem);
    navigationBar.add(createFolderItem);
    navigationBar.add(listDirectoryItem);

    mainPanel.add(navigationBar, BorderLayout.NORTH);

    // Painel que vai conter os cards dos arquivos e pastas
    JPanel cardsPanel = new JPanel();
    cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
    cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JScrollPane scrollPane = new JScrollPane(cardsPanel);
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    // Listener para listar diretório
    listDirectoryItem.addActionListener(e -> {
        cardsPanel.removeAll();

        String directoryContent = fileSystem.listDirectoryString("root");

        for (String line : directoryContent.split("\n")) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            card.setBackground(new Color(45, 45, 48)); // cor escura similar Windows 11

            JLabel content = new JLabel(line);
            content.setForeground(Color.WHITE);
            content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            card.add(content);

            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // para o card ter altura fixa e largura máxima
            cardsPanel.add(card);
            cardsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // espaço entre cards
            
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();

        System.out.println("Conteúdo do diretório:\n" + directoryContent);
    });

    setContentPane(mainPanel);
}

    
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());

            } catch (UnsupportedLookAndFeelException ex) {
                System.err.println("Falha ao aplicar FlatLaf.");
            }

            SwingUtilities.invokeLater(() -> {
                new Main().setVisible(true);
            });
    }

    //Agora o estado está reconstruído e você pode usar o sistema normalmente
    //sim.listDirectory("root");
    //sim.createDirectory("root", "novos");
    //sim.createFile("root/novos", "arquivo1.txt");
    //sim.createFile("root/novos", "alzir_aula.pdf");
    //sim.listDirectory("root/novos");

    //sim.deleteDirectory("root", "novos");
    //sim.listDirectory("root");
}



