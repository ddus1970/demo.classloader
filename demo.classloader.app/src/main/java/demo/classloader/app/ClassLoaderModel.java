/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo.classloader.app;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author ddus
 */
public class ClassLoaderModel {
    public static final String PLAIN_EXT = ".class";
    public static final String ENCRYPTED_EXT = ".encrypted";
    
    private String _pluginDir;
    private String _pluginClassName;
    private int _encryptNumber;

    public ClassLoaderModel() {
        initModel();
    }
    
    private void initModel() {
        setPluginDir(System.getProperty("user.dir"));
        setPluginClassName("demo.classloader.plugins.Plugin1");
        setEncryptNumber(3);
    }
    
    /**
     * 
     * @param className
     * @return 
     */
    public static String convertClassNameToPath(String className) {
        return className.replace(".", "/");
    }

    /**
     * 
     * @param className
     * @return 
     */
    public static Path getPlainClassPath(String className) {
        return Path.of(convertClassNameToPath(className).concat(PLAIN_EXT));
    }

    /**
     * 
     * @param className
     * @return 
     */
    public static Path getEncryptedClassPath(String className) {
        return Path.of(convertClassNameToPath(className).concat(ENCRYPTED_EXT));
    }
    
    /**
     * 
     * @param pluginPath
     * @param classPath
     * @return true if exists class with given pathes.
     */
    public static boolean checkClassPath(Path pluginPath, Path classPath) {
        return Files.exists(pluginPath.resolve(classPath));
    }
    
    public static boolean checkEncryptedClassPath(Path pluginPath, 
            String className) {
        return checkClassPath(pluginPath, getEncryptedClassPath(className));
    }

    public static boolean checkEncryptedClassPath(String pluginDir, 
            String className) {
        return checkEncryptedClassPath(Path.of(pluginDir), className);
    }
    
    public static boolean checkPlainClassPath(Path pluginPath, 
            String className) {
        return checkClassPath(pluginPath, getPlainClassPath(className));
    }

    public static boolean checkPlainClassPath(String pluginDir, 
            String className) {
        return checkPlainClassPath(Path.of(pluginDir), className);
    }
    
    /**
     * @return the _pluginDir
     */
    public Path getPluginPath() {
        return Path.of(getPluginDir());
    }

    /***
     * Relative path to encrypted class file. Full path may be obtained by resolving with getPluginPath()
     * @return 
     */
    public Path getEncryptedClassPath() {
        return getEncryptedClassPath(getPluginClassName());
    }

    /***
     * Relative path to plain class file. Full path may be obtained by resolving with getPluginPath()
     * @return 
     */
    public Path getPlainClassPath() {
        return getPlainClassPath(getPluginClassName());
    }

    /**
     * Just checks existing of file 
     * @param classPath
     * @return 
     */
    public boolean checkClassPath(Path classPath) {
        return checkClassPath(getPluginPath(), classPath);
    }
    
    /**
     * Returns existence of plain class file.
     * 
     */
    public boolean hasPlainClass() {
        return checkClassPath(getPlainClassPath());
    }

    /**
     * Returns existence of encrypted class file.
     * @return 
     */
    public boolean hasEncryptedClass() {
        return checkClassPath(getEncryptedClassPath());
    }
    
    /**
     * @return the _encryptNumber
     */
    public int getEncryptNumber() {
        return _encryptNumber;
    }

    /**
     * @param _encryptNumber the _encryptNumber to set
     */
    public void setEncryptNumber(int encryptNumber) {
        this._encryptNumber = encryptNumber;
    }

    /**
     * @param _encryptNumber the _encryptNumber to set
     */
    public void setEncryptNumber(String encryptNumber) {
        try {
            this._encryptNumber = Integer.parseInt(encryptNumber);
        }
        catch (NumberFormatException ex) {
            this._encryptNumber = 0;
        }
    }

    /**
     * @return the _pluginDir
     */
    public String getPluginDir() {
        return _pluginDir;
    }

    /**
     * @param _pluginDir the _pluginDir to set
     */
    public void setPluginDir(String _pluginDir) {
        this._pluginDir = _pluginDir;
    }

    /**
     * @return the _pluginClass
     */
    public String getPluginClassName() {
        return _pluginClassName;
    }

    /**
     * @param className the _pluginClass to set
     */
    public void setPluginClassName(String className) {
        this._pluginClassName = className;
    }
}
