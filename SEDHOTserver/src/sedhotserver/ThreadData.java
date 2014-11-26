/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedhotserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MALEPANDA
 */
public class ThreadData implements Runnable {
    
    private final Socket Sclient;
    private BufferedReader masuk = null;

    public ThreadData(Socket client) {
        this.Sclient = client; 
    }

    @Override
    public void run() {
        try {
            masuk = new BufferedReader(new InputStreamReader(Sclient.getInputStream()));
            String pil;
            String namaKirim;
            while(masuk.readLine() != null){
                pil = masuk.readLine();
                if ("USER".equals(pil)){
                    
                }
                
                
                
            }  
        } 
        catch (IOException ex) {
            Logger.getLogger(ThreadData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }           
}