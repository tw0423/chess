package client;

import java.util.Scanner;
import java.util.ArrayList;

import chess.ChessPosition;
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
        else if (state == State.LOGIN) {
            handleLOGGIN(cmd, scanner);
        }
        else if (state == State.INGAME) {
            handleINGAME(cmd, scanner);
        }
        else{
            handleOBSERVE(cmd, scanner);
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
                if(client.creatGame(createParams) == -1) {
                    System.out.println("Error creating game !");
                }else{
                    System.out.println("Game created !");
                }
                break;
            case "list":
                ArrayList<GameData> gameDataList = client.listGames();

                if (gameDataList == null || gameDataList.isEmpty()) {
                    System.out.println("No games available.");
                    break;
                }

                System.out.println("Available Games:");
                for (int i = 0; i < gameDataList.size(); i++) {
                    GameData game = gameDataList.get(i);
                    int displayNumber = i + 1;
                    String gameName1 = game.gameName();
                    String whiteUsername = game.whiteUsername() != null ? game.whiteUsername() : "null";
                    String blackUsername = game.blackUsername() != null ? game.blackUsername() : "null";

                    System.out.printf("%d. %s   white: %s   black: %s%n",
                            displayNumber, gameName1, whiteUsername, blackUsername);
                }
                break;
            case "join":
                System.out.print("<GAME NUMBER>: ");
                String joinId = scanner.nextLine();
                System.out.print("<COLOR> [WHITE or BLACK]: ");
                String joinColor = scanner.nextLine();
                String[] joinParams = { joinId, joinColor };

                if(client.joinGame(joinParams)){
                    System.out.println("Game joined !");
                    System.out.println("New possible commands:  ");
                    state = State.INGAME;
                    printInGameHelp();
                }else{
                    System.out.println("Error joining game !");
                }



                break;

            case "observe":
                System.out.print("<GAME NUMBER>: ");
                String obsId = scanner.nextLine();
                System.out.print("<Color> [WHITE or BLACK]: ");
                String color = scanner.nextLine();

                String[] obsParams = { obsId, color };

                if(client.observeGame(obsParams)){
                    state = State.OBSERVE;
                    System.out.print("New possible commands:  ");
                    printObserveGameHelp();
                }



                break;

            case "logout":
                client.logout();
                state = State.LOGOUT;
                break;
            default:
                System.out.println("Unknown command. Type 'help' for options.");
        }
    }


    private void handleLOGOUT(String command, Scanner scanner) {
        ArrayList<String> params = null;
        switch (command) {
            case "help":
                printInGameHelp();
                break;
            case "login":
                System.out.print("<USERNAME>: ");
                String loginUser = scanner.nextLine();
                System.out.print("<PASSWORD>: ");
                String loginPass = scanner.nextLine();

                String[] loginParams = { loginUser, loginPass };

                if (client.doLogin(loginParams)) {
                    state = State.LOGIN;
                    System.out.println("here is the new commands you can use after logging in.");
                    printPostloginHelp();

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

                if (!(client.doRegister(registerParams))) {
                    System.out.println("Registration failed");
                }else{
                    state = State.LOGIN;
                }
                break;

            default:
                System.out.println("Unknown command. Type 'help' for options.");
        }
    }

    private void handleINGAME(String command, Scanner scanner) {
        ArrayList<String> params = null;
        switch (command) {
            case "help":
                printInGameHelp();
                break;
            case "redrawboard":
                client.reDrawBoard();
                break;
            case "leave":
                manageLeave(scanner);
                break;


            case "move":
                System.out.print("<from>[a-h][1-8]: ");
                String startingPosition = scanner.nextLine();
                System.out.print("<to>[a-h][1-8]: >: ");
                String endingPosition = scanner.nextLine();
                System.out.print("<promotion>[queen|rook|bishop|knight] <skip it if not possible>: ");
                String promotion = scanner.nextLine();

                String[] moveParams = {startingPosition, endingPosition, promotion};
                if(client.makeMove(moveParams)){
                    System.out.println("Move successful");
                }else{
                    System.out.println("Move failed");
                }
                break;

            case "resign":
                System.out.print("are you sure you want to resign? (yes): ");
                String check1 = scanner.nextLine();
                if(check1.equals("yes")){
                    client.resign();

                }
                break;
            case "hightlightmove":
                managehightlight( scanner);

                break;


        }
    }

    private void handleOBSERVE(String command, Scanner scanner) {
        switch (command) {
            case "help":
                printObserveGameHelp();
                break;
            case "redrawboard":
                client.reDrawBoard();
                break;
            case "leave":
                manageLeave(scanner);
                break;
            case "hightlightmove":
                managehightlight( scanner);
                break;



        }
    }

    private void manageLeave(Scanner scanner) {
        System.out.print("are you sure you want to leave? (yes): ");
        String check = scanner.nextLine();
        if(check.equals("yes")){
            client.leaveGame();
        }
        state = State.LOGIN;
        printPostloginHelp();

    }

    private void managehightlight(Scanner scanner) {
        System.out.print("<at>[a-h][1-8]: ");
        String position = scanner.nextLine();
        String[] hightlightParams = {position};
        client.highlight(hightlightParams);

    }



    private void printPreloginHelp(){
        printBlue("register  ");
        printGray("- to create an account ");

        newLine();
        printBlue("login  ");
        printGray("- to play chess game ");

        newLine();
        printBlue("exit ");
        printGray("- exit chess");

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
        newLine();
    }

    private void printInGameHelp() {

        printBlue("redrawBoard ");
        printGray("- to redraw the chess board ");

        newLine();
        printBlue("leave ");
        printGray("- tp leave playing the game ");

        newLine();
        printBlue("move ");
        printGray("- make your move");

        newLine();
        printBlue("resign ");
        printGray("- resign game");

        newLine();
        printBlue("highlightMove ");
        printGray("- highlight legal move for specific chess piece ");
        newLine();
        printBlue("help ");
        printGray("- see all possible commands");
        newLine();

    }

    private void printObserveGameHelp() {
        printBlue("redrawBoard ");
        printGray("- to redraw the chess board ");

        newLine();
        printBlue("leave ");
        printGray("- tp leave observing the game ");


        newLine();
        printBlue("highlightMove ");
        printGray("- highlight legal move ");
        newLine();

        printBlue("help ");
        printGray("- see all possible commands");
        newLine();


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