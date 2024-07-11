package chatutils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

    private static final Pattern MESSAGE_PATTERN = Pattern.compile(
        "Message \\[fromUserName=(\\w+), toUsername=(\\w+), messageType=(\\w+), message=(.+)\\]");
    
    public static Message messageStringToObject(String message) throws IllegalArgumentException{
       
        Matcher matcher = MESSAGE_PATTERN.matcher(message);

       if (!matcher.matches()) {
            // System.out.println("Error matcher does not match!!!");
            // System.out.println("Arguement Given: \n"+ message);

            throw new IllegalArgumentException("Invalid Message Format");
       }


       
        // Extract values from groups
        String fromUserName = matcher.group(1);
        String toUsername = matcher.group(2);

        String messageTypeStr = matcher.group(3); // Capture messageType as String

        // Convert messageTypeStr to MessageType enum
        MessageType messageType = MessageType.valueOf(messageTypeStr.toUpperCase()); // Assuming messageType is always uppercase
        //System.out.println("No error converting message type");


        String  messageExtracted = matcher.group(4);

        return new Message(fromUserName, toUsername, messageType, messageExtracted);

       

  
       }
}