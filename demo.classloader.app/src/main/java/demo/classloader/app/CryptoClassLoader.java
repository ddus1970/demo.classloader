/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo.classloader.app;

import demo.classloader.providers.interfaces.Cipher;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author ddus
 */
public class CryptoClassLoader extends ClassLoader {
    
    ClassLoaderModel _model;
    private Cipher _cipher;
    
/*    
    public CryptoClassLoader(URL[] urls) {
        super(urls);
    }
*/    
    public CryptoClassLoader(ClassLoaderModel model) {
        this(model, null);
    }
    
    public CryptoClassLoader(ClassLoaderModel model, Cipher cipher) {
        super();
        _model = model;
        _cipher = cipher;
    }
    
    public ClassLoaderModel getModel() {
        return _model;
    }
    
    @Override
    protected Class<?> findClass(String className) 
            throws ClassNotFoundException 
    {
        try {
            byte[] classBytes = loadClassBytes(className);
            Class<?> clz = super.defineClass(className, classBytes, 
                    0, classBytes.length);
            if (clz != null)
                return clz;
        }
        catch (IOException | URISyntaxException ex) {
            throw new ClassNotFoundException(className, ex);
        }
        throw new ClassNotFoundException(className);
    }
    
    private byte[] loadClassBytes(String className) 
            throws IOException, URISyntaxException
    {
        ClassLoader cl = this.getClass().getClassLoader();
        final var m = getModel();
        Path classForLoadPath = null;
        if (_cipher != null) {
            classForLoadPath = m.getPluginPath().resolve(m.getEncryptedClassPath());
        } else {
            classForLoadPath = m.getPluginPath().resolve(m.getPlainClassPath());
        }
            
        byte[] classBytes = null;
        if (Files.exists(classForLoadPath)) {
            classBytes = Files.readAllBytes(classForLoadPath);
            if (_cipher != null) {
                ByteBuffer input = ByteBuffer.wrap(classBytes);
                ByteBuffer output = input.duplicate();
                _cipher.decrypt(input, output);
                classBytes = output.array();
            }
        };
        
        return classBytes;
    } 
}
