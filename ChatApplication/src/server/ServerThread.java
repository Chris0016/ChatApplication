package server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import chatutils.Message;
import chatutils.MessageType;
import chatutils.MessageUtils;

/**
 * 
 * @author Christopher
 * 
 * Client to Server to Client message mechanism: 
 * User to user messages are done through a hashmap that keeps track of connection thread and corresponding user to whom the thread
 * belongs to. When message from a user is received, ServerThread checks existing hashmap for receipientID and forwards data to that
 * thread. 
 * 
 * 
 */
public class ServerThread extends Thread {

    private Socket socket;
    private HashMap<String, ServerThread> threadMap;
    private PrintWriter output;
    private String clientName;
    private Message broadcastMessage;

    private final String SERVER_NAME = "ORIGINSERVER"; 


    public ServerThread(Socket socket, HashMap<String, ServerThread> threadMap, String clientName) {
        this.socket = socket;
        this.threadMap = threadMap;
        this.clientName = clientName;

        broadcastMessage = new Message();
        broadcastMessage.setFromUserName(SERVER_NAME);
        broadcastMessage.setToUsername("BROADCAST");
        broadcastMessage.setMessageType(MessageType.INFO);
        

        printAllConnectedThreads();
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            
            //returning the output to the client : true statement is to flush the buffer otherwise
            //we have to do it manuallyy
             output = new PrintWriter(socket.getOutputStream(),true);


            //DEBUG
            // output.println(new Message(
            //     userName, 
            //     "ORIGINSERVERthread",
            //     MessageType.INFO,
            //     "message from thread helllo!!!" ).toString()
            //     );
            //DEBUG


            //inifite loop for server
            while(true) {
                String clientMessageString = input.readLine();
                //if user types exit command
              
    
                System.out.println(clientMessageString);
                Message clientMessage = MessageUtils.messageStringToObject(clientMessageString);
                

                System.out.println("HELLO THIS IS COOL");
                


                if (clientMessage.getMessageType().equals(MessageType.CLOSE_CONNECTION)){
                    try {
                        threadMap.remove(clientMessage.getFromUserName());
                        Thread.currentThread().interrupt();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return;
                } else {
                    System.out.println("not a quit message");
                }


                String recipient = clientMessage.getToUsername();

                if(threadMap.containsKey(recipient))
                    threadMap.get(recipient).printToClient(clientMessage);
                else {
                    output.println(new Message(
                        SERVER_NAME, 
                        clientName,
                        MessageType.ERROR,
                        "Unable to send message, user is not connected or does not exists"
                    ).toString());

                }
                   
            }

        } catch (Exception e) {
            System.out.println("Error occured " +e.getStackTrace());
        }
    }


    /**
     * 
     * @param message Message to send
     * 
     * 
     */
    private void printToClient(Message message){
        output.println(message.toString());
    }


    /**
     * 
     * @param outputMessage String message to broadcast
     * 
     * Broadcast message to all existing clients
     */
    private void printToALlClients(String outputMessage) {
   

        for( String clientId : threadMap.keySet()) {
           threadMap.get(clientId).output.println(outputMessage);
        }

    }

    /**
     * Private helper function to debug, allows programmer to see the list of 
     * current <Key,Value> pairs in the hashmap
    */
    private void printAllConnectedThreads(){
        for( String clientId : threadMap.keySet()) {
            System.out.println(clientId);
         }
    }

}
