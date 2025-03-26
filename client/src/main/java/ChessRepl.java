
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
public class ChessRepl {
    private final ChessClient client;
    private State state;

    public ChessRepl(String serverHost, int serverPort) {
        this.client = new ChessClient(serverHost, serverPort);
        state = State.LOGOUT;
    }

    public void run(){
        System.out.println("Welcome to Chess");

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("> ");
            String line = scanner.nextLine();
            if(line.equals("exit")) {
                System.out.println("Hope to see you next time!");
                break;
            };

            evalCommand(line);

        }
    }

    public void evalCOmmand(String command){
        var tokens = command.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);


        if (state == State.LOGOUT) {
            handleLOGOUT(cmd, params);
        }
        else{
            handleLOGGIN(cmd, params);
        }

    }

    private void handleLOGGIN(String command, String[] params) {
        switch (command) {
            case "help":
                printPostloginHelp();
                break;
            case "create":
                doLogout();
                break;
            case "list":
                doCreateGame();
                break;
            case "join":
                doListGames();
                break;
            case "observe":
                doPlayGame();
                break;
            case "logout":
                doObserveGame();
                break;
            case "quit":
                doQuit():
                break:
            case "help":
                doHelp():
                break:
            default:
                System.out.println("Unknown command. Type 'help' for options.");
        }
    }


    private void handleLOGOUT(String command, String[] params){
        switch (command) {
            case "help":
                printPreloginHelp();
                break;
            case "login":
                doLogin();
                break;
            case "register":
                doRegister();
                break;
            case "help":
                help();
                break;
            default:
                System.out.println("Unknown command. Type 'help' for options.");
        }
    }


}