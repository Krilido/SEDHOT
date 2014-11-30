/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progjarclient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Krilido
 */
public class ProgjarClient {
   private static Socket soket; 
   private static BufferedReader buf;
   private static String namaFile;
   /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
         
        try {
            soket = new Socket("localhost", 9999);
            buf = new BufferedReader(new InputStreamReader(System.in));        
        } 
        catch (Exception e) {
            System.err.println("Koneksi gagal. Silahkan coba lagi");
            System.exit(1);
        }
        // TODO code application logic here
    }

    public static void send(){
        try{
            namaFile = buf.readLine();
            File fileBaru = new File(namaFile);
            byte[] bytearray = new byte[(int) fileBaru.length()];

            FileInputStream fis;
            fis = new FileInputStream(fileBaru);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(bytearray, 0, bytearray.length);

            OutputStream out;
            out = soket.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeUTF(fileBaru.getName());
            dos.writeLong(bytearray.length);
            dos.write(bytearray, 0, bytearray.length);
            dos.flush();
            
        } catch (Exception e) {
            System.err.println("File tidak ada");
        }
    }
 
    public static void receiveFile(String namaFile) throws IOException{
        int bytesRead;
            try (InputStream in = soket.getInputStream()) {
                DataInputStream clientData;
                clientData = new DataInputStream(in);            
                namaFile = clientData.readUTF();
                OutputStream output;
                output = new FileOutputStream(( namaFile));
                long size = clientData.readLong();
                byte[] buffer = new byte[1024];
                while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                    output.write(buffer, 0, bytesRead);
                    size -= bytesRead;
                }
                
                output.close();
            }
    }

    
}
