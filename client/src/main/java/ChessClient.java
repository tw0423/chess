import chess.ChessGame;
import jdk.jshell.spi.ExecutionControlProvider;
import model.GameData;
import model.UserData;
import service.CreateGameRequest;
import service.CreateGameResponse;
import service.ListGameReponse;
import service.LoginResponse;
import ui.ChessBoardPainter;

import java.util.ArrayList;
import java.util.HashMap;

public class ChessClient{
    private final ServerFacade facade;
    private HashMap<Integer, Integer> matchingIDMap;
    private ArrayList<GameData> gameDataList;
    private boolean loggedIn = false;
    private String currentUser = null;  // store username
    private String authToken = null;
    private ChessRepl chessRepl = null;



    public ChessClient(String serverURL, ChessRepl chessRepl) {
        this.facade = new ServerFacade(serverURL);
        this.chessRepl = chessRepl;
//        gameDataList = this.listGames();
//        createMathcingMap();
    }


    public boolean doRegister(String ... para) {
        try {
            String username = para[0];
            String password = para[1];
            String email = para[2];
            UserData response = facade.registerUser(username, password, email);
            System.out.println("Successfully registered user " + response.username());

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean doLogin(String ... para) {
        try {
            String username = para[0];
            String password = para[1];
            LoginResponse res = facade.loginUser(username, password);
            System.out.println("Successfully login as " + res.username());
            this.authToken = res.authToken();
        }catch (Exception e){

            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public int creatGame(String ... para) {
        String gameName = para[0];
        CreateGameResponse res = null;
        try {
            res = facade.createGame(gameName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(res ==null){
            return -1;
        }
        int displayNumber = matchingIDMap.size();
        matchingIDMap.put(displayNumber, res.gameID());

        return res.gameID();
    }

    public ArrayList<GameData> listGames() {
        ListGameReponse res = null;
        try {
            res = facade.getGames();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }






        return res.games();
    }

    public boolean joinGame(String ... para){
        String playerColor = para[1];
        int gameID;
        if(isInteger(para[0])) {
            gameID = Integer.parseInt(para[0]);
        }
        else{
            System.out.println("Please enter a valid game ID");
            return false;
        }
        ArrayList<GameData> list = this.listGames();
        GameData gameData = list.get(gameID);
        try {
            facade.joinGame(playerColor, gameData.gameID()
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;

        }
        return true;
    }

    public boolean logout(){
        try {
            facade.logoutUser();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.authToken = null;
        return true;

    }

    public boolean observeGame(String ... para){
        String playerColor = para[1];
        if(!(playerColor.equals("white")||playerColor.equals("black"))){
            System.out.println("only accept color for white or black");
            return false;
        }



        int gameNum = Integer.parseInt(para[0]);

        ArrayList<GameData> list = this.listGames();
        GameData gameData = list.get(gameNum+1);
        int gameID = gameData.gameID();
        ListGameReponse res = null;
        try {
            res = facade.getGames();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        ArrayList<GameData> games = res.games();
        for(GameData game: games){
            if(game.gameID() == gameID){
                ChessGame chessGame = game.game();
                ChessBoardPainter painter= new ui.ChessBoardPainter(chessGame, playerColor);
                painter.main(null);
            }
        }
        return true;
    }

    private void createMathcingMap() {
        ArrayList<GameData> list = this.listGames();

        for (int i = 0; i < list.size(); i++) {
            int displayNumber = i + 1;  // 1-based index
            int gameID = list.get(i+1).gameID();
            matchingIDMap.put(displayNumber, gameID);  // Maps 1 -> gameID, 2 -> gameID, etc.
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}