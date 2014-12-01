/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedhotserver;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MALEPANDA
 */
public class ThreadData implements Runnable {
    
    private final Socket cmdclient;
    private final Socket fileclient;
    private BufferedReader cmdin = null;
    private final String filepath="file/";
    private String username =null;

    public ThreadData(Socket cmdclient, Socket fileclient) {
        this.cmdclient = cmdclient; 
        this.fileclient = fileclient;
    }
    
    @Override
    public void run() {
        try {
            cmdin = new BufferedReader(new InputStreamReader(cmdclient.getInputStream()));
            PrintWriter cmdout= new PrintWriter(new BufferedWriter(new OutputStreamWriter(cmdclient.getOutputStream())), true);
            
            BufferedReader filein = new BufferedReader(new InputStreamReader(fileclient.getInputStream()));
            PrintWriter fileout= new PrintWriter(new BufferedWriter(new OutputStreamWriter(fileclient.getOutputStream())), true);
            
            File folder = new File(filepath);
            File[] listOfFiles = folder.listFiles();
            ArrayList<String> fileforyou = new ArrayList<>();
            
            String req;
            String[] cmd;
            int stat=0;
            cmd = new String[3];
            cmdout.println("200 HI! Welcome to SEDHOT Server :D\r\n\r\n");
            while((req = cmdin.readLine()) != null){
                int i =0;
                for (String retval: req.split(" ")){
                cmd[i]=retval;
                i++;
            }
            if ("USR".equals(cmd[0])){
                if( ("fakhri".equals(cmd[1])) || ("azis".equals(cmd[1])) || ("raga".equals(cmd[1])) ){
                    stat=1;
                    cmdout.println("200 OK\r\n\r\n");
                    username=cmd[1];
                }
                else{
                    cmdout.println("500 NO USER\r\n\r\n");
                }
       //     }
            
            
            }
            else if ("SEN".equals(cmd[0]) && stat==1){
                int bytesRead;
                String filename = null;
                for (String retval: cmd[1].split("/")){
                    filename=retval;
                }
                
                InputStream in = fileclient.getInputStream();
                DataInputStream clientData;
                clientData = new DataInputStream(in);            
                //namaFile = clientData.readUTF();
                String namaFile = filepath+username+"_"+cmd[2]+"_"+filename;
                OutputStream output;
                output = new FileOutputStream(namaFile);
                cmdout.println("200 READY TO RECIVE FILE\r\n\r\n");
                long size = clientData.readLong();
                byte[] buffer = new byte[1024];
                while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                    output.write(buffer, 0, bytesRead);
                    size -= bytesRead;
                }
                cmdout.println("200 FILE TRANSFER SUCCESS\r\n\r\n");
                output.close();
                
//                PrintWriter filewriter = new PrintWriter(filepath+cmd[2]+"_"+username+"_"+filename, "UTF-8");
//                cmdout.println("200 READY TO RECIVE FILE\r\n\r\n");
//                while((filereq = filein.readLine()) != null){
//                    filewriter.println(filereq);
//                }
//                filewriter.close();
//                cmdout.println("200 FILE TRANSFER SUCCESS\r\n\r\n");
            }
            
            else if ("CEK".equals(cmd[0]) && stat==1){
                cmdout.println("200 FILE TO YOU:\r\n");
                for (int j = 0; j < listOfFiles.length; j++) {
                    String[] name = new String[4];
                    int z=0;
                    for (String retval: listOfFiles[j].getName().split("_")){
                        name[z]=retval;
                        z++;
                    }
//                    System.out.println(name[0]);
                    if (name[0].equals(username) || name[0].equals("ALL")) {
                        fileforyou.add(listOfFiles[j].getName());
                        cmdout.println((j+1)+" "+name[2]+" "+name[1]+"\r\n");
                    }
                }
            }
            
            else if ("GET".equals(cmd[0]) && stat==1){
                int idfile = Integer.parseInt(cmd[1]);
                File fileBaru = new File(filepath+fileforyou.get(idfile));
                byte[] bytearray = new byte[(int) fileBaru.length()];

                FileInputStream fis;
                fis = new FileInputStream(fileBaru);
                BufferedInputStream bis = new BufferedInputStream(fis);
                //bis.read(mybytearray, 0, mybytearray.length);

                DataInputStream dis = new DataInputStream(bis);
                dis.readFully(bytearray, 0, bytearray.length);

                OutputStream out;
                out = fileclient.getOutputStream();

                //Sending file name and file size to the server
                DataOutputStream dos = new DataOutputStream(out);
                dos.writeUTF(fileBaru.getName());
                dos.writeLong(bytearray.length);
                dos.write(bytearray, 0, bytearray.length);
                dos.flush();
            }
            
            else{
                cmdout.println("400 BAD REQUEST\r\n\r\n");
            }
                
            }  
            cmdin.close();
            cmdout.close();
        } 
        catch (IOException ex) {
            Logger.getLogger(ThreadData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }           

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}