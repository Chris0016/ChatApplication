## Getting Started
    - Run the Main.java in the server folder
    - Run the Client.java in the client
    - Run another Client.java instance
    * Continue creating client instances

Type in username in the client.java to log in

To send messages, type recipient followed by colon followed by message(recipient:message)


### About

A minimal multiclient server project using java sockets and threads. Client to server to client communication (not broadcast although it can be implemented). 


### Rules of Communication
Usernames cannot have spaces

#### Message Format
    FromUsername
    ToUsername
    MessageType
    messageString


An example message sent would look like the following: 

    "Message [fromUserName=" + fromUserName + ", toUsername=" + toUsername + ", messageType=" + messageType + ", message=" + message + "]";
    
### How it works

The server acts like the middle man of communication, validating and fowarding messages between clients. Each client connected to the server maintains a socket of communication with the server. The server keeps track of each connection through individual threads each maintaining a connection. To send data from a client to server to a client (not broadcast),  the server maintains a hashmap of each thread along with its corresponding clientId( the username its connected to). 



