package chatutils;


public class Message {
  
    
    private String fromUserName;
    private String toUsername;
    private MessageType messageType; 
    private String message;
   // private String Timestamp;


    public Message(String fromUserName, String toUsername, MessageType messageType, String message) {
        this.fromUserName = fromUserName;
        this.toUsername = toUsername;
        this.messageType = messageType;
        this.message = message;
    }

    public Message(){
        
    }

    public String getFromUserName() {
        return fromUserName;
    }


    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }


    public String getToUsername() {
        return toUsername;
    }


    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }


    public MessageType getMessageType() {
        return messageType;
    }


    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


   

    @Override
    public String toString() {
        return "Message [fromUserName=" + fromUserName + ", toUsername=" + toUsername + ", messageType=" + messageType
                + ", message=" + message + "]";
    }


    
    
    

}
