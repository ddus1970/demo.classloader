/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package demo.classloader.app;

import java.util.List;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.util.ServiceLoader;

import demo.classloader.providers.interfaces.Cipher;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

/**
 *
 * @author ddus
 */
public class ClassLoaderApp {
    public final static String LOGGER_NAME = "demo.classloader";
    public final static System.Logger _logger;
    
    static {
        //loadLoggerJdk4();
        _logger = loadLoggerJdk9();
    }

    /**
     * Loads loggers in JDK 4 style.
     */
    private static void loadLoggerJdk4() 
    {
        try {
            InputStream stream = ClassLoaderApp.class.getClassLoader()
                    .getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(stream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Loads logger in JDK 9 style.
     */
    private static System.Logger loadLoggerJdk9() {
        List<System.Logger> loggers = ServiceLoader.load(System.LoggerFinder.class)
                .stream()
                //.map(ServiceLoader.Provider<LoggerFinder>::get)
                .map(ServiceLoader.Provider::get)
                .map((System.LoggerFinder lf) -> lf.getLogger(LOGGER_NAME, 
                        ClassLoaderApp.class.getModule()))
                .collect(Collectors.toList());

        Optional<System.Logger> oLogger = loggers.stream().findFirst();
        return oLogger.orElseThrow(() -> new RuntimeException(
                "Logger " + LOGGER_NAME + " not found"));
    }

    /* 
     * @return 
     */
    private static List<Cipher> loadCiphers() {
        return ServiceLoader.load(Cipher.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(cipher -> cipher != null)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Cipher> ciphers = loadCiphers();

        EventQueue.invokeLater(() -> {
            var model = new ClassLoaderModel();
            var view = new ClassLoaderView();
            var controller = new ClassLoaderController(view, model, ciphers.get(0));
            view.setTitle("Class loader demo");
            view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            view.setVisible(true);
        });
    }
}
