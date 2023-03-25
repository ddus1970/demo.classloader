/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo.classloader.providers.caesar;

import demo.classloader.providers.interfaces.Cipher;
import java.nio.ByteBuffer;

/**
 *
 * @author ddus
 */
public class CaesarCipher implements Cipher {

    private int _encryptKey;
    
    @Override
    public void setEncryptKey(int encryptKey) {
        _encryptKey = encryptKey;
    }

    @Override
    public int getEncryptKey() {
        return _encryptKey;
    }

    @Override
    public void decrypt(ByteBuffer input, ByteBuffer output) {
        int inputPos = input.position();
        while (input.hasRemaining()) {
            output.put((byte)(input.get() - _encryptKey));
        }
    }

    @Override
    public void encrypt(ByteBuffer input, ByteBuffer output) {
        int inputPos = input.position();
        while (input.hasRemaining()) {
            output.put((byte)(input.get() + _encryptKey));
        }
    }
}
