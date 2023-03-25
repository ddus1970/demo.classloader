/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
 */

module demo.classloader.app {
    requires java.base;
    requires java.desktop;
    requires java.logging;
    requires commons.cli;
    requires demo.classloader.providers.interfaces;
    requires demo.classloader.logging;
    
    uses System.LoggerFinder;
    uses demo.classloader.providers.interfaces.Cipher;
}
