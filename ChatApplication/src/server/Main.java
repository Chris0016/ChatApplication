package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import chatutils.Message;
import chatutils.MessageType;
import chatutils.MessageUtils;


/**
 * @author Christopher
 * 
 * Main server class, waits and initiates connection with the client, delagates session management to ServerThread 
 */
public class Main {

    static final int PORT = 5000;


    public static void main(String[] args) {
       
        
        HashMap<String, ServerThread> threadClientMap = new HashMap<>();

        BufferedReader input;
        PrintWriter output;


        Message startSessionMessage = new Message();
        startSessionMessage.setMessageType(MessageType.INFO);
        
        startSessionMessage.setMessage("Server Ready");
        startSessionMessage.setFromUserName("ORIGINSERVER");
        startSessionMessage.setToUsername("POTENTIALCLIENT");

    
        try (ServerSocket serversocket = new ServerSocket(PORT)){
            while(true) {
                Socket socket = serversocket.accept();
                
                    
                input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(),true);

                output.println(startSessionMessage.toString());


                String clientMessageString = input.readLine();
                System.out.println("Received: " + clientMessageString);


                Message clientMessage = MessageUtils.messageStringToObject(clientMessageString);
                String userName = clientMessage.getFromUserName();

                ServerThread serverThread = new ServerThread(socket, threadClientMap, userName);
                
                threadClientMap.put(userName, serverThread);
                serverThread.start();

            }
        } catch (Exception e) {
            System.out.println("Error occured in main: " + e.getStackTrace());
        }
    }


}