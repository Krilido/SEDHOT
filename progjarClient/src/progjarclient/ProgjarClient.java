/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progjarclient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Krilido
 */
public class ProgjarClient {
   private static Socket soket; 
   private static Socket soket2; 
   private static BufferedReader buf;
   private static String namaFile;
   private static String user;
   private static final String konek = "200 HI! Welcome to SEDHOT Server :D\r\n\r\n";
   private static final String konfirm = "200 OK\r\n\r\n";
   /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
         
        try {
            
            
            soket2 = new Socket("localhost", 9191);
            buf = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out;
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soket2.getOutputStream())), true);
            switch (buf.readLine()) {
                case konek:
                    String[] input;
                    input = new String[2];
                    input[0] = "USR";
                    Scanner scanner = new Scanner(System.in);
                    user = scanner.nextLine();
                    input[1] = user;
                    out.println(Arrays.toString(input));
                    if(buf.readLine().equals(konfirm) ){
                        while(true){
                            try {
                                buf.readLine();
                                if("1".equals(buf.readLine())){
                                    buf.readLine();
                                    out.println("SEN");
                                    if("200 READY TO RECIVE FILE\r\n\r\n".equals(buf.readLine())){
                                        namaFile = buf.readLine();
                                        send(namaFile);
                                    }
                                    if("200 FILE TRANSFER SUCCESS\r\n\r\n".equals(buf.readLine())){
                                        break;
                                    }
                                }
                                if("2".equals(buf.readLine())){
                                    out.println("CEK");
                                    buf.readLine();
                                    if("200 FILE TO YOU:\r\n".equals(buf.readLine())){
                                        //send();
                                    }
                                    }
                                if("3".equals(buf.readLine())){
                                    }
                                if("4".equals(buf.readLine())){
                                    }
                            }
                            catch (IOException | NumberFormatException e) {
                                System.err.println("not valid input");
                            }
                        }
                        //   soket.close();
                    }
                    break;
                case "500 NO USER\r\n\r\n":
                    break;
            }
            }
         
        catch (Exception e) {
            System.err.println("Koneksi gagal. Silahkan coba lagi");
            System.exit(1);
              
       // TODO code application logic here
    }
}

    /**
     *
     * @param namaFile
     */
    public static void send(String namaFile){
        try{
//            namaFile = buf.readLine();
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

            DataOutputStream dos;
            dos = new DataOutputStream(out);
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
