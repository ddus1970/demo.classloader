/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo.classloader.plugins;

import javax.swing.JOptionPane;

/**
 *
 * @author ddus
 */
public class Plugin1 {
    
    public static void main(String[] args) {
        ClassLoader cl = Plugin1.class.getClassLoader();
        StringBuilder sb = new StringBuilder();
        sb.append("Plugin One has been loaded \n by ");
        sb.append(cl.toString());
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
