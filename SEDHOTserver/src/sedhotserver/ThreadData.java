/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedhotserver;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

                    
            String req, filereq;
            String[] cmd;
            int stat=0;
            cmd = new String[3];
            cmdout.println("200 HI! Welcome to SEDHOT Server :D\r\n\r\n");
            while((req = cmdin.readLine()) != null){
                System.out.println(req);
                int i =0;
                for (String retval: req.split(" ")){
                cmd[i]=retval;
                i++;
            }
            System.out.println(cmd[1]);
            if ("USR".equals(cmd[0])){
                if( ("fakhri".equals(cmd[1])) || ("azis".equals(cmd[1])) || ("raga".equals(cmd[1])) ){
                    stat=1;
                    cmdout.println("200 OK\r\n\r\n");
                    username=cmd[1];
                }
                else{
                    cmdout.println("500 NO USER\r\n\r\n");
                }
            }
            
            
            else if ("SEN".equals(cmd[0]) && stat==1){
                String filename = null;
                for (String retval: cmd[1].split("/")){
                    filename=retval;
                }
                PrintWriter filewriter = new PrintWriter(filepath+cmd[2]+"_"+username+"_"+filename, "UTF-8");
                cmdout.println("200 READY TO RECIVE FILE\r\n\r\n");
                while((filereq = filein.readLine()) != null){
                    filewriter.println(filereq);
                }
                filewriter.close();
                cmdout.println("200 FILE TRANSFER SUCCESS\r\n\r\n");
            }
            
            else if ("CEK".equals(cmd[0]) && stat==1){
                cmdout.println("200 FILE TO YOU:\r\n");
                for (i = 0; i < listOfFiles.length; i++) {
                    String[] name = new String[4];
                    i=0;
                    for (String retval: listOfFiles[i].getName().split("_")){
                        name[i]=retval;
                    }
                    if (name[0].equals(username)) {
                        fileforyou.add(listOfFiles[i].getName());
                        cmdout.println((i+1)+" "+cmd[2]+" "+cmd[1]+"\r\n");
                    }
                }
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