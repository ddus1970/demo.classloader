/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo.classloader.app;

import demo.classloader.providers.interfaces.Cipher;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.*;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import static java.lang.System.Logger;
        
/**
 *
 * @author ddus
 */
public class ClassLoaderController {

    //private static Logger _logger = Logger.getLogger(ClassLoaderController.class.getName());
    private static Logger _logger = ClassLoaderApp._logger;

    ClassLoaderView _view;
    ClassLoaderModel _model;
    Cipher _cipher;
    private Action _encryptAction;
    private Action _loadEncryptedAction;
    

    public ClassLoaderController(ClassLoaderView view, ClassLoaderModel model,
            Cipher cipher) 
    {
        _view = view;
        _model = model;
        _cipher = cipher;
        initActions();
        initView();
    }

    private ClassLoaderView getView() {
        return _view;
    }

    private ClassLoaderModel getModel() {
        return _model;
    }

    private Cipher getCipher() {
        return _cipher;
    }

    private void initActions() {
        _encryptAction = new AbstractAction("Encrypt") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    encryptClass();
                    ClassLoaderController.this.updateViewStatus();
                }
                catch (IOException ex) {
                    JOptionPane.showMessageDialog(_view, ex);
                }
                //JOptionPane.showMessageDialog(_view, ae);
            }
        };
        
        _loadEncryptedAction = new AbstractAction("Load encrypted") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                loadClass(true);
            }
        };
    }

    private void initView() {
        final ClassLoaderModel m = getModel();
        final ClassLoaderView v = getView();

        fromModelToView();
        updateViewStatus();

        v.getPluginDirField().getDocument().addDocumentListener(
                new MyDocumentListener(ev -> {
            if ( ev.getType() == DocumentEvent.EventType.INSERT || 
                ev.getType() == DocumentEvent.EventType.REMOVE )
            {
                m.setPluginDir(v.getPluginDirField().getText());
                updateViewStatus();
            }
        }));

        v.getPluginClassField().getDocument().addDocumentListener(
                new MyDocumentListener(ev -> {
            if ( ev.getType() == DocumentEvent.EventType.INSERT || 
                ev.getType() == DocumentEvent.EventType.REMOVE )
            {
                m.setPluginClassName(v.getPluginClassField().getText());
                updateViewStatus();
            }
        }));
        
        v.getEncryptKeyField().getDocument().addDocumentListener(                
                new MyDocumentListener(ev -> {
            if ( ev.getType() == DocumentEvent.EventType.INSERT 
//                || ev.getType() == DocumentEvent.EventType.REMOVE 
               )
            {
                m.setEncryptNumber(v.getEncryptKeyField().getText());
                updateViewStatus();
            }
        }));


        v.getEncryptButton().setAction(getEncryptAction());

        v.getLoadEncryptedButton().setAction(getLoadEncryptedAction());

        v.getLoadPlainButton().addActionListener((event) -> {
            loadClass(false);
        });
    }

    private void fromModelToView() {
        var v = getView();
        var m = getModel();
        v.getPluginDirField().setText(m.getPluginDir());
        v.getPluginClassField().setText(m.getPluginClassName());
        v.getEncryptKeyField().setText(Integer.toString(m.getEncryptNumber()));
    }

    private void fromViewToModel() {
        var v = getView();
        var m = getModel();
        m.setPluginDir(v.getPluginDirField().getText());
        m.setPluginClassName(v.getPluginClassField().getText());
        m.setEncryptNumber(Integer.parseInt(v.getEncryptKeyField().getText()));
    }

    /**
     * Update view status from model
     */
    private void updateViewStatus() {
        final var v = getView();
        final var m = getModel();

        final var pluginDir = m.getPluginDir();
        final var pluginClassName = m.getPluginClassName();

        final var hasPlainClass =  ClassLoaderModel.
                checkPlainClassPath(pluginDir, pluginClassName);
        final var hasEncryptedClass = ClassLoaderModel.
                checkEncryptedClassPath(pluginDir, pluginClassName);
        
        _logger.log(Logger.Level.TRACE, new StringBuilder("updateViewStatus")
                .append("; hasPlainClass=").append(hasPlainClass)
                .append("; hasEncrypterClass=").append(hasEncryptedClass)
                .append("; pluginDir=").append(pluginDir)
                .append("; pluginClassName=").append(pluginClassName)
                .toString());
        
        getEncryptAction().setEnabled(hasPlainClass);
        getLoadEncryptedAction().setEnabled(hasEncryptedClass);
        v.getLoadPlainButton().setEnabled(hasPlainClass);
    }

    /**
     * Loads plain plugin class
     */
    private void loadPlugin() {
        loadClass(false);
    }

    /**
     * Loads and invokes plugin class
     *
     * @param isEncrypted
     */
    private void loadClass(boolean isEncrypted) {
        try {
            final var m = getModel();
            Cipher cipher = null;
            if (isEncrypted) {
                cipher = getCipher();
                cipher.setEncryptKey(m.getEncryptNumber());
            }
            var loader = new CryptoClassLoader(m, cipher);
            Class clz = loader.loadClass(m.getPluginClassName());

            Method meth = clz.getDeclaredMethod("main", String[].class);
            meth.invoke(null, (Object) new String[]{});
        } 
        catch (Throwable ex) {
            JOptionPane.showMessageDialog(_view, ex);
        }
    }

    /**
     * Encrypts class file with encryptor.
     *
     */
    private void encryptClass()
            throws IOException {
        final var m = getModel();
        final var cipher = getCipher();
        cipher.setEncryptKey(m.getEncryptNumber());
        ByteBuffer inputBuff = ByteBuffer.allocate(256);
        ByteBuffer outputBuff = ByteBuffer.allocate(256);
        Path plainClassPath = m.getPluginPath().resolve(m.getPlainClassPath());
        Path encryptedClassPath = m.getPluginPath().resolve(m.getEncryptedClassPath());
        try (FileChannel input = FileChannel.open(plainClassPath, READ); 
            FileChannel output = FileChannel.open(encryptedClassPath,
                    WRITE, TRUNCATE_EXISTING, CREATE)) {
            while (input.read(inputBuff) >= 0) {
                inputBuff.flip();
                while (inputBuff.hasRemaining()) {
                    outputBuff.clear();
                    cipher.encrypt(inputBuff, outputBuff);
                    outputBuff.flip();
                    int written = output.write(outputBuff);
                }
                inputBuff.clear();
            }
        }
    }

    /**
     * @return the _encryptAction
     */
    public Action getEncryptAction() {
        return _encryptAction;
    }

    /**
     * @return the _loadEncryptedAction
     */
    public Action getLoadEncryptedAction() {
        return _loadEncryptedAction;
    }
    
    static class MyDocumentListener implements DocumentListener {
        private Consumer<DocumentEvent> _eventHandler;
        
        public MyDocumentListener(Consumer<DocumentEvent> eventHandler ){
            _eventHandler = eventHandler;
        }
        
        @Override
        public void insertUpdate(DocumentEvent de) {
            _eventHandler.accept(de);
        }

        @Override
        public void removeUpdate(DocumentEvent de) {
            _eventHandler.accept(de);
        }

        @Override
        public void changedUpdate(DocumentEvent de) {
            _eventHandler.accept(de);
        }
    }
}
