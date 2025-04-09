package websocket.messages;

public class Notification extends ServerMessage{

    String message;
    public Notification(String message) {
        //  In Java, super refers to the parent class (also called the superclass).
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
