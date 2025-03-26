
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import ui.EscapeSequences;
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
            System.out.print("[" +state + "}" +">>> " );
            String line = scanner.nextLine();
            if(line.equals("exit")) {
                System.out.println("Hope to see you next time!");
                break;
            };

            evalCommand(line);

        }
    }

    public void evalCommand(String command){
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
                client.doLogout();
                break;
            case "list":
                client.doCreateGame();
                break;
            case "join":
                client.doListGames();
                break;
            case "observe":
                client.doPlayGame();
                break;
            case "logout":
                client.doObserveGame();
                break;
            case "quit":
                client.doQuit():
                break;
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
                client.doLogin();
                break;
            case "register":
                client.doRegister();
                break;

            default:
                System.out.println("Unknown command. Type 'help' for options.");
        }
    }

    private void printPreloginHelp(){
        pirntNewLineBlue("register <USERNAME> <PASSWORD> <EMAIL> ");
        printGray("- to create an account ");
        pirntNewLineBlue("login <USERNAME> <PASSWORD> <EMAIL> ");
        printGray("- to play chess game ");
        pirntNewLineBlue("quit ");
        printGray("- quit chess");
        pirntNewLineBlue("help ");
        printGray("- see all possible commands");

    }

    private void printPostloginHelp(){
        pirntNewLineBlue("create <NAME> <PASSWORD> <EMAIL> ");
        printGray("- to create an account ");
        pirntNewLineBlue("login <USERNAME> <PASSWORD> <EMAIL> ");
        printGray("- to play chess game ");
        pirntNewLineBlue("quit ");
        printGray("- quit chess");
        pirntNewLineBlue("help ");
        printGray("- see all possible commands");

    }
    private void pirntNewLineBlue(String text){
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + text + EscapeSequences.RESET_TEXT_COLOR);
    }
    private void printGray(String text){
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + text + EscapeSequences.RESET_TEXT_COLOR);

    }


}