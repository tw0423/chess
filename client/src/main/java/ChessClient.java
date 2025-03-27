import chess.ChessGame;
import model.GameData;
import model.UserData;
import service.CreateGameRequest;
import service.CreateGameResponse;
import service.ListGameReponse;
import service.LoginResponse;
import ui.ChessBoardPainter;

import java.util.ArrayList;

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
        UserData response = facade.registerUser(username, password, email);
        System.out.println("Successfully registered user " + response.username());
        return true;
    }

    public boolean doLogin(String ... para) {
        String username = para[0];
        String password = para[1];
        LoginResponse res = facade.loginUser(username, password);
        System.out.println("Successfully registered user " + res.username());
        this.authToken = res.authToken();
        return true;
    }

    public int creatGame(String ... para) {
        String gameName = para[0];
        CreateGameResponse res = facade.createGame(gameName);
        if(res ==null){
            return -1;
        }
        return res.gameID();
    }

    public ArrayList<GameData> listGames() {
        ListGameReponse res = facade.getGames();
        return res.games();
    }

    public boolean joinGame(String ... para){
        String playerColor = para[0];
        int gameID = Integer.parseInt(para[1]);
        facade.joinGame(playerColor, gameID);
        return true;

    }

    public boolean logout(){
        facade.logoutUser();
        this.authToken = null;
        return true;

    }

    public boolean observeGame(String ... para){
        String playerColor = para[1];
        int gameID = Integer.parseInt(para[0]);
        ListGameReponse res = facade.getGames();
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


}