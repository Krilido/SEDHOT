/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progjarserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Krilido
 */
public class ProgjarServer {

    /**
     * @param args the command line arguments
     */
    
    private static ServerSocket serverSocket;
    private static Socket client = null;
    
    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ArrayList<Konek> daftar;
        try{
            daftar = new ArrayList<>();
            serverSocket = new ServerSocket(9999);
            System.err.println("Memulai Server......");
        } 
        catch (Exception e){
            System.err.println("Port Telah Penuh");
            System.exit(1);
        }
        while(true){
            try{
                client = serverSocket.accept();
                System.out.println("Koneksi berhasil dari :" + client);
                Thread baru = new Thread(new Konek (client));
                baru.start();
            }
            catch(Exception e){
                System.err.println("Koneksi eror");
            }
        // TODO code application logic here
        }
    }
}
