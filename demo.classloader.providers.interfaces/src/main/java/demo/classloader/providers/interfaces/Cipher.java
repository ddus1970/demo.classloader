/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo.classloader.providers.interfaces;

import java.nio.ByteBuffer;

/**
 * Interface 
 * @author ddus
 */
public interface Cipher {
    
    /**
     * Sets encrypt key
     * @param encryptKey 
     */
    public void setEncryptKey(int encryptKey);

    /**
     * Returns encrypt key
     * @return 
     */
    public int getEncryptKey();
    
    /**
     * Decrypts bytes from input buffer and put them into output buffer
     * @param input input buffer
     * @param output output buffer
     * @return count of read bytes
     */
    public void decrypt(ByteBuffer input, ByteBuffer output);
    
    /**
     * Encrypts bytes from input buffer and puts them into output buffer
     * @param input input buffer
     * @param output output buffer
     * @return count of read bytes
     */
    public void encrypt(ByteBuffer input, ByteBuffer output);

}
