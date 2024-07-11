package client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import chatutils.ConsoleColors;
import chatutils.Message;
import chatutils.MessageType;
import chatutils.MessageUtils;


/**
 * @author Christopher 
 * @version 0.0.1
 * 
 * Client class that initiates and maintains client session. 
 */
public class Client {
    private Socket socket;
    private BufferedReader serverIn;
    private PrintWriter clientOut;
    private BufferedReader userInput;
    private String clientName; 


    static final int PORT = 5000;
    static final String ADDRESS = "localhost";
    static final String SERVER_NAME = "ORIGINSERVER";
    static final String SERVER_CONNECT_STRING = "Server Ready";


    /**
     * 
     * @param socket
     * @throws IOException
     * 
     */
    public Client(Socket socket) throws IOException {
        this.socket = socket;
        serverIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        clientOut = new PrintWriter(this.socket.getOutputStream(), true);
        userInput = new BufferedReader(new InputStreamReader(System.in));

        connectToServer();
    }

    /**
     * 
     * @throws IOException Sends exception if unable to connect to server
     * 
     * Creates connection to server, waits for server SERVER_CONNECT_STRING, asks for user details. 
     */
    private void connectToServer() throws IOException{
 
        Message message = new Message();
      
        String serverMessageString = "";
        Message serverMessage;
            
        while(true){
            serverMessageString = serverIn.readLine();
            
            //System.out.println(serverMessageString);
            serverMessage = MessageUtils.messageStringToObject(serverMessageString);
            
            if(serverMessage.getMessage().equals(SERVER_CONNECT_STRING)){
                System.out.println("Server Ready to Connect");
                break;
            } 

        }

        //Send user credentials
        message.setMessageType(MessageType.CREDENTIALS);
        message.setMessage("this is some text get username from the potential client");

         
        System.out.println(ConsoleColors.BLUE + "---" + ConsoleColors.PURPLE + " LOGIN " + ConsoleColors.BLUE + "---");
        
        System.out.println(ConsoleColors.GREEN + "Enter your username: " + ConsoleColors.RESET);
        //System.out.flush(); // Ensure prompt is printed immediately
        clientName = userInput.readLine();
        message.setFromUserName(clientName);
        
        System.out.println(ConsoleColors.RED + "[Type quit to exit]" + ConsoleColors.RESET);
        clientOut.println(message.toString());

           
    }


    /**
     * Manages user prompt and displaying received messages from the server
     */
    public void start() {
        Thread receiveThread = new Thread(() -> {
            try {
                String serverMessageString;
                Message serverMessage;
                while ((serverMessageString = serverIn.readLine()) != null) {
                    serverMessage = MessageUtils.messageStringToObject(serverMessageString);
                    System.out.println("\n" +  serverMessage.getFromUserName() + ": " + serverMessage.getMessage());
                    printUserPrompt();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start();

        Thread sendThread = new Thread(() -> {
            Message message = new Message();
            message.setMessageType(MessageType.INFO);
            message.setFromUserName(clientName);


            try {
                while (true) {
                    System.out.print(ConsoleColors.YELLOW + "Enter recipient and message" + ConsoleColors.CYAN + "(recipient:message)" + ConsoleColors.RESET + ": ");
                    System.out.flush(); // Ensure prompt is printed immediately
                    String userInputLine = userInput.readLine();
                    if (userInputLine != null && !userInputLine.isEmpty()) {


                        if(userInputLine.equalsIgnoreCase("quit")){
                            message.setMessageType(MessageType.CLOSE_CONNECTION);
                            message.setToUsername(SERVER_NAME);
                            message.setMessage("close");


                            clientOut.println(message.toString());
                            System.out.println("Goodbye...");


                            System.exit(0);
                        }
                            

                        // Splitting user input into recipient and message
                        int colonIndex = userInputLine.indexOf(':');
                        if (colonIndex != -1) {
                            String recipient = userInputLine.substring(0, colonIndex).trim();
                            String messageString = userInputLine.substring(colonIndex + 1).trim();
                            if (!recipient.isEmpty() && !messageString.isEmpty()) {

                               
                                message.setToUsername(recipient);
                                message.setMessage(messageString);
                                clientOut.println(message.toString());

                                
                            } else {
                                System.out.println("Invalid input. Format should be recipient:message.");
                            }
                        } else {
                            System.out.println("Invalid input. Format should be recipient:message.");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sendThread.start();

        try {
            receiveThread.join();
            sendThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompt immediate display and out.flush()
     */
    private void printUserPrompt() {
        System.out.print(ConsoleColors.YELLOW + "Enter recipient and message" + ConsoleColors.CYAN + "(recipient:message)" + ConsoleColors.RESET + ": ");
        System.out.flush(); // Ensure prompt is printed immediately
    }
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(ADDRESS, PORT); 
            Client client = new Client(socket);
            client.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
