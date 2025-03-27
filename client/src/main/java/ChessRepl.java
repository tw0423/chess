
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import model.GameData;
import ui.EscapeSequences;
public class ChessRepl {
    private final ChessClient client;
    private State state;

    public ChessRepl(String serverURL) {
        this.client = new ChessClient(serverURL, this);
        this.state = State.LOGOUT;
    }

    public void run(){
        System.out.println("Welcome to Chess !!!");
        System.out.println("Below is the command line arguments you can type in");
        Scanner scanner = new Scanner(System.in);

        while(true){
            printPreloginHelp();
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
                client.creatGame(params);
                break;
            case "list":
                ArrayList<GameData> gameDataList =  client.listGames();
                for (GameData gameData : gameDataList) {
                    System.out.println(gameData);
                }
                break;
            case "join":
                client.joinGame(params);
                break;
//            case "observe":
//                client.doPlayGame();
//                break;
//            case "logout":
//                client.doObserveGame();
//                break;
//            case "quit":
//                client.doQuit();
//                break;
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
                if(client.doLogin(params)) {
                    state = State.LOGIN;
                }
                break;
            case "register":
                if(!client.doRegister(params)){
                    System.out.println("Registration failed");
                };
                break;

            default:
                System.out.println("Unknown command. Type 'help' for options.");
        }
    }

    private void printPreloginHelp(){
        printBlue("register <USERNAME> <PASSWORD> <EMAIL> ");
        printGray("- to create an account ");
        System.out.println();
        printBlue("login <USERNAME> <PASSWORD> <EMAIL> ");
        printGray("- to play chess game ");
        System.out.println();

        printBlue("quit ");
        printGray("- quit chess");
        System.out.println();

        printBlue("help ");
        printGray("- see all possible commands");
        System.out.println();


    }

    private void printPostloginHelp(){
        printBlue("create <NAME> ");
        printGray("- to create an game ");
        printBlue("list ");
        printGray("- to see all the games can join ");
        printBlue("join <ID> [WHITE|BLACK]");
        printGray("- join game and slect sides to join");
        printBlue("observe <ID> ");
        printGray("- a game");
        printBlue("logout ");
        printGray("- when you are done");
        printBlue("quit ");
        printGray("- quit chess");
        printBlue("help ");
        printGray("- see all possible commands");

    }
    private void printBlue(String text){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + text + EscapeSequences.RESET_TEXT_COLOR);
    }
    private void printGray(String text){
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + text + EscapeSequences.RESET_TEXT_COLOR);

    }


}