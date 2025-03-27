
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
        printPreloginHelp();
        while(true){
            System.out.print("[" +state + "}" +">>> " );
            String line = scanner.nextLine();
            if(line.equals("exit")) {
                System.out.println("Hope to see you next time!");
                break;
            };

            evalCommand(line, scanner);

        }
    }

    public void evalCommand(String command, Scanner scanner) {
        var tokens = command.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
//        var params = Arrays.copyOfRange(tokens, 1, tokens.length);


        if (state == State.LOGOUT) {
            handleLOGOUT(cmd, scanner);
        }
        else{
            handleLOGGIN(cmd, scanner);
        }

    }

    private void handleLOGGIN(String command, Scanner scanner) {
        switch (command) {
            case "help":
                printPostloginHelp();
                break;
            case "create":
                System.out.print("<GAME NAME>: ");
                String gameName = scanner.nextLine();
                String[] createParams = { gameName };
                client.creatGame(createParams);
                break;
            case "list":
                ArrayList<GameData> gameDataList =  client.listGames();
                for (GameData gameData : gameDataList) {
                    System.out.println(gameData);
                }
                break;
            case "join":
                System.out.print("<GAME ID>: ");
                String joinId = scanner.nextLine();
                System.out.print("<COLOR> [WHITE or BLACK]: ");
                String joinColor = scanner.nextLine();
                String[] joinParams = { joinId, joinColor };

                client.joinGame(joinParams);
                break;

            case "observe":
                System.out.println("<GAME ID>: ");
                String obsId = scanner.nextLine();

                String[] obsParams = { obsId };

                client.observeGame(obsParams);
                break;
            case "logout":
                client.logout();
                break;
            default:
                System.out.println("Unknown command. Type 'help' for options.");
        }
    }


    private void handleLOGOUT(String command, Scanner scanner) {
        ArrayList<String> params = null;
        switch (command) {
            case "help":
                printPreloginHelp();
                break;
            case "login":
                System.out.print("<USERNAME>: ");
                String loginUser = scanner.nextLine();
                System.out.print("<PASSWORD>: ");
                String loginPass = scanner.nextLine();

                String[] loginParams = { loginUser, loginPass };

                if (client.doLogin(loginParams)) {
                    state = State.LOGIN;
                } else {
                    System.out.println("Login failed");
                }
                break;
            case "register":
                System.out.print("<USERNAME>: ");
                String username = scanner.nextLine();
                System.out.print("<PASSWORD>: ");
                String password = scanner.nextLine();

                System.out.print("<EMAIL>: ");
                String email = scanner.nextLine();

                String[] registerParams = { username, password, email };

                if (!client.doRegister(registerParams)) {
                    System.out.println("Registration failed");
                }
                break;

            default:
                System.out.println("Unknown command. Type 'help' for options.");
        }
    }

    private void printPreloginHelp(){
        printBlue("register  ");
        printGray("- to create an account ");

        newLine();        printBlue("login  ");
        printGray("- to play chess game ");

        newLine();
        printBlue("quit ");
        printGray("- quit chess");

        newLine();
        printBlue("help ");
        printGray("- see all possible commands");
        newLine();

    }

    private void printPostloginHelp(){
        printBlue("create ");
        printGray("- to create an game ");

        newLine();
        printBlue("list ");
        printGray("- to see all the games can join ");

        newLine();
        printBlue("join ");
        printGray("- join game and slect sides to join");

        newLine();
        printBlue("observe");
        printGray("- a game");

        newLine();
        printBlue("logout ");
        printGray("- when you are done");

        newLine();
        printBlue("exit ");
        printGray("- quit chess");
        newLine();
        printBlue("help ");
        printGray("- see all possible commands");
    }

    private void newLine(){
        System.out.println();
    }

    private void printBlue(String text){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + text + EscapeSequences.RESET_TEXT_COLOR);
    }
    private void printGray(String text){
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + text + EscapeSequences.RESET_TEXT_COLOR);

    }


}