/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpfileserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import java.io.*;
import java.net.*;

class TCPFileServer {

    public static void main(String argv[]) throws Exception {
        String ClientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6789);
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader infromClient =
                    new BufferedReader(new InputStreamReader(
                    connectionSocket.getInputStream()));
            DataOutputStream outToClient =
                    new DataOutputStream(
                    connectionSocket.getOutputStream());
            ClientSentence = infromClient.readLine();
            try {
                RandomAccessFile in = new RandomAccessFile(ClientSentence, "r");
                String s;
                String total = "";
                while ((s = in.readLine()) != null)
                    total = total + s;
                outToClient.writeBytes(total + "\n");
                in.close();
            } catch (Exception e) {
                outToClient.writeBytes("File not exist!!!");
            }
        }
    }
}