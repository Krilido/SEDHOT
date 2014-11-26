/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedhotserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MALEPANDA
 */
public class SEDHOTserver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ArrayList <ThreadData> daftar;
            daftar = new ArrayList<>();
            ServerSocket cmdss = new ServerSocket(9191);
            ServerSocket filess = new ServerSocket(9090);
            Socket client = cmdss.accept();
            System.out.println("Koneksi berhasil dari :" + client.getInetAddress());
            Thread baru = new Thread(new ThreadData (client));
            baru.start();
            
        } catch (IOException ex) {
            Logger.getLogger(SEDHOTserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
