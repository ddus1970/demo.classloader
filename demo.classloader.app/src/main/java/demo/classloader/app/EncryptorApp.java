package demo.classloader.app;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;
import org.apache.commons.cli.*;

/**
 *
 * @author ddus
 */
public class EncryptorApp {

    enum ProcessType {
        ReadAll,
        Channel
    };
    
    static final String _encryptExt = ".caeser";
    static final String _defaultClassName = "demo.classloader.plugin.Plugin1";

    static final Option _optionClassName
            = new Option("c", "className", true, "name of class for encryption");
    static final Option _optionEncryptKey
            = new Option("e", "encryptKey", true, "key for encryption of class bytes");

    private static Options getOptions() {
        Options options = new Options();
        //_optionClassName.setRequired(true);
        options.addOption(_optionClassName);
        options.addOption(_optionEncryptKey);
        return options;
    }

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("EncryptorApp <className>", getOptions());
    }

    // /mnt/data/ddu/projects/java/demos/demo.classloader/src/main/java/demo/classloader/plugin/Plugin1.java

    private static void encryptClass(String className, String encryptStr)
            throws IOException, URISyntaxException
    {
        encryptClass(className, encryptStr, ProcessType.ReadAll);    
    }

    private static void encryptClass(String className, String encryptStr, 
            ProcessType processType)
            throws IOException, URISyntaxException 
    {
        final String cname = className.replace('.', '/').concat(".class");

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final URI classURI = loader.getResource(cname).toURI();
        final String classPath = classURI.toString();
        final URI encryptURI = new URI(classPath
                .substring(0, classPath.lastIndexOf(".class"))
                .concat(_encryptExt)
                .concat(".")
                .concat(processType.name())
                );
        if (processType == ProcessType.ReadAll) {
            encryptFile(Path.of(classURI), Path.of(encryptURI), encryptStr);
        }
        else {
            encryptFile2(Path.of(classURI), Path.of(encryptURI), encryptStr);
        }
    }

    private static void encryptFile(Path input, Path output, String encryptStr) 
            throws IOException
    {
        final int encryptKey = Integer.parseInt(encryptStr);
        byte[] classBytes = Files.readAllBytes(input);
        for (int i = 0; i < classBytes.length; i++) {
            classBytes[i] = (byte) (classBytes[i] + encryptKey);
        }
        Files.write(output, classBytes);
    }
    
    private static void encryptFile2(Path input, Path output, String encryptStr) 
            throws IOException
    {
        final int encryptKey = Integer.parseInt(encryptStr);
        try (
                SeekableByteChannel inputChannel = Files.newByteChannel(input,
                    StandardOpenOption.READ);  
                SeekableByteChannel outputChannel = Files.newByteChannel(output, 
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            ) 
        {
            ByteBuffer inBuffer = ByteBuffer.allocate(256);
            ByteBuffer outBuffer = ByteBuffer.allocate(172);
            while (inputChannel.read(inBuffer) > 0) {
                inBuffer.flip();
                while (inBuffer.hasRemaining()) {
                    outBuffer.put((byte) (inBuffer.get() + encryptKey));
                    if (!outBuffer.hasRemaining()) {
                        outBuffer.flip();
                        outputChannel.write(outBuffer);
                        outBuffer.clear();
                    }
                }
                inBuffer.clear();
            }
            if (outBuffer.position() > 0) {
                outBuffer.flip();
                outputChannel.write(outBuffer);
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cl = parser.parse(getOptions(), args);
            String className = cl.getOptionValue(_optionClassName, _defaultClassName);
            String encryptKey = cl.getOptionValue(_optionEncryptKey, "3");
            encryptClass(className, encryptKey, ProcessType.Channel);
        } catch (ParseException ex) {
            printHelp();
            return;
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
            return;
        }
    }
}
