/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progjarserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Krilido
 */
class Konek implements Runnable {
    private final Socket Sclient;
    private BufferedReader masuk = null;

    public Konek(Socket client) {
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
                switch (pil) {
                    case "1":
                        terimaFile();
                        break;
                    case "2":
                        while(masuk.readLine() != null){
                            namaKirim=masuk.readLine();
                            kirimFile(namaKirim);
                        }
                        break;
                    case "3":
                        showdir();
                        break;
                    default:
                        System.out.println("Command tidak dikenali");
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Konek.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void terimaFile() {
        int bytesRead;
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void kirimFile(String namaKirim) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void showdir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
