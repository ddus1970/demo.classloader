/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo.classloader.app;

import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import demo.classloader.utils.GBC;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 *
 * @author ddus
 */
public class ClassLoaderView extends JFrame {

    private final JTextField _encryptKeyField = 
            new JTextField("3", 4);
    private final JTextField _pluginClassField = 
            new JTextField("demo.classloader.plugins.Plugin1", 30);
    private final JTextField _pluginDirField = 
            new JTextField(System.getProperty("user.dir"), 30);
    //private JButton _encryptButton = new JButton("Encrypt");
    
    private final JButton _loadEncryptedButton = new JButton("Load encrypted");
    
    private final JButton _loadPlainButton = new JButton("Load plain");

    private final JButton _encryptButton = new JButton();
    
    private final JFileChooser pluginChooser = new JFileChooser();
    
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 200;
    
    public ClassLoaderView() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        initComponents();
        pack();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GBC gbc;
        int row;
        
        row = 0;
        add(new JLabel("Plugins"), new GBC(0, row).setAnchor(GBC.EAST).setInsets(3));
        gbc = new GBC(1, row).setWeight(100,0).setAnchor(GBC.WEST).setInsets(3);
        gbc.fill = GBC.HORIZONTAL;
        add(_pluginDirField, gbc);
        var chooseButton = new JButton("Select");
        gbc = new GBC(2, row).setAnchor(GBC.EAST).setInsets(3);
        add(chooseButton, gbc);
        chooseButton.addActionListener((event) -> {
            pluginChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            pluginChooser.setCurrentDirectory(new File(getPluginDirField().getText()));
            int option = pluginChooser.showDialog(this, "Plugin path");
            if (option == JFileChooser.APPROVE_OPTION) {
                _pluginDirField.setText(pluginChooser.getSelectedFile().getPath());
            }
        });

        row = 1;
        add(new JLabel("Class"), new GBC(0, row).setAnchor(GBC.EAST).setInsets(3));
        gbc = new GBC(1, row).setWeight(100,0).setAnchor(GBC.WEST).setInsets(3);
        gbc.fill = GBC.HORIZONTAL;
        add(_pluginClassField, gbc);
        gbc = new GBC(2, row).setAnchor(GBC.WEST).setInsets(3);
        add(_encryptButton, gbc);
        
        row = 2;
        add(new JLabel("Key"), new GBC(0, row).setAnchor(GBC.EAST).setInsets(3));
        gbc = new GBC(1, row).setWeight(100,0).setAnchor(GBC.WEST).setInsets(3);
        gbc.fill = GBC.HORIZONTAL;
        add(_encryptKeyField, gbc);
        
        row = 3;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        gbc = new GBC(0, row, 3, 1).setAnchor(GBC.SOUTHEAST).setFill(GBC.HORIZONTAL).setInsets(3);
        add(buttonPanel, gbc);
        buttonPanel.add(_loadPlainButton, gbc);
        buttonPanel.add(_loadEncryptedButton, gbc);
    }
    
    /**
     * @return the encryptKey
     */
    public JTextField getEncryptKeyField() {
        return _encryptKeyField;
    }

    /**
     * @return the _pluginClassField
     */
    public JTextField getPluginClassField() {
        return _pluginClassField;
    }

    /**
     * @return the _pluginDir
     */
    public JTextField getPluginDirField() {
        return _pluginDirField;
    }

    /**
     * @return the _loadPlainButton
     */
    public JButton getLoadPlainButton() {
        return _loadPlainButton;
    }

    /**
     * @return the _loadEncryptedButton
     */
    public JButton getLoadEncryptedButton() {
        return _loadEncryptedButton;
    }

    /**
     * @return the _encryptButton
     */
    public JButton getEncryptButton() {
        return _encryptButton;
    }
}
