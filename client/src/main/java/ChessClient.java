

public class ChessClient{
    private final ServerFacade facade;
    private boolean loggedIn = false;
    private String currentUser = null;  // store username
    private String authToken = null;
    private ChessRepl chessRepl = null;

    public ChessClient(String serverURL, ChessRepl chessRepl) {
        this.facade = new ServerFacade(serverURL);
        this.chessRepl = chessRepl;
    }

    //how it works is we will send the request to server??

    public boolean doRegister(String ... para) {
        String username = para[0];
        String password = para[1];
        String email = para[2];
        facade.registerUser(username, password, email);
        return true;
    }
    public void login(String username, String password) {
        currentUser = username;
    }

}