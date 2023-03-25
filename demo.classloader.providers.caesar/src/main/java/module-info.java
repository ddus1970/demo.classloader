/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
 */

module demo.classloader.providers.caesar {
    requires demo.classloader.providers.interfaces;
    
    provides demo.classloader.providers.interfaces.Cipher 
        with demo.classloader.providers.caesar.CaesarCipher;
}
