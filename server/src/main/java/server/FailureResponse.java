package server;

public class FailureResponse extends Exception {
    public FailureResponse(String message) {
        super(message);
    }
}
