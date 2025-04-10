package client;

import chess.*;
import model.GameData;
import model.UserData;
import reqres.*;
import ui.ChessBoardPainter;
import websocket.commands.MakeMoveCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.Error;


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
    private String gameColor = null;
    private ChessGame currentGame = null;
    private int gameID = 0;



    public ChessClient(String serverURL, ChessRepl chessRepl) {
        this.facade = new ServerFacade(serverURL);
        this.chessRepl = chessRepl;
//        gameDataList = this.listGames();
//        createMathcingMap();
    }


    public boolean doRegister(String ... para) {
        try {
            if(para.length != 3){
                System.out.println("missing required information");
                return false;
            }
            String username = para[0];
            String password = para[1];
            String email = para[2];

            if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
                System.out.println("missing required information");
                return false;
            }
            RegisterResponse response = facade.registerUser(username, password, email);
            System.out.println("Successfully registered user " + response.username());
            this.doLogin(username, password);

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
            if(username.isEmpty() || password.isEmpty() ){
                System.out.println("missing required information");
                return false;
            }
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
        if(gameName.isEmpty() ){
            System.out.println("missing required information");
            return 0;
        }
        CreateGameResponse res = null;
        try {
            res = facade.createGame(gameName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(res ==null){
            return -1;
        }

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
        if(playerColor.isEmpty()){
            System.out.println("missing required information");
            return false;
        }
        gameColor = playerColor;

        ArrayList<GameData> list = this.listGames();
        if(gameID >= list.size()+1 || gameID < 0 ){
            System.out.println("invalid game Number");
            return false;
        }
        GameData gameData = list.get(gameID-1);
        this.gameID = gameData.gameID();
        try {
            facade.joinGame(playerColor, gameData.gameID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;

        }

        this.observeGame(para[0], playerColor);
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
        gameColor = playerColor;


        int gameNum = Integer.parseInt(para[0]);

        ArrayList<GameData> list = this.listGames();
        if(gameNum > list.size()||gameNum < 0){
            return false;
        }
        GameData gameData = list.get(gameNum-1);
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
                this.gameID = game.gameID();
                currentGame = chessGame;
                ChessBoardPainter painter= new ui.ChessBoardPainter(chessGame, playerColor);
                painter.main(null);
            }
        }
        return true;
    }


    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //for client command

    public void reDrawBoard(){
        drawBoard(currentGame, gameColor);
    }

    public boolean makeMove(String ... para){
        ChessPiece.PieceType promotiontype;
        if (para.length >= 3 && para[0].matches("[a-h][1-8]") && para[1].matches("[a-h][1-8]")) {
            ChessPosition startingPosition = new ChessPosition(para[0].charAt(1) - '0', para[0].charAt(0) - ('a' - 1));
            ChessPosition endingPosition = new ChessPosition(para[1].charAt(1) - '0', para[1].charAt(0) - ('a' - 1));

            //update the promotion
            if(para.length == 3){
                String promotion = para[2];
                promotiontype = getPieceType(promotion);

            }else{
                 promotiontype = null;
            }

            if(!checkColor(startingPosition, currentGame)){
                System.out.println("You can not move the opponent's chess piece");
            }

            ChessMove move = new ChessMove(startingPosition, endingPosition, promotiontype);
            if(currentGame.validMoves(startingPosition).contains(move)){
                MakeMoveCommand makeMove = new MakeMoveCommand(authToken, gameID, move);
                ws.moveCommand(makeMove);
            }
            else {
                System.out.println("invalid move");
                return false;
            }
            return true;
        }else{
            System.out.println("please provide a valid position");
            return false;
        }
    }



    public void notify(Notification notification){
        System.out.println(notification.getMessage());
    }

    public void notifyError(Error error){
        System.out.println(error.getMessage());
    }

    public void handleLoadGame(LoadGame loadGame){
        System.out.println("game updated: ");
        this.drawBoard(loadGame.getGame(), gameColor);
        currentGame = loadGame.getGame();
    }

    private void drawBoard(ChessGame game, String color){
        ChessBoardPainter painter= new ui.ChessBoardPainter(game, color);
        painter.main(null);

    }
    private ChessPiece.PieceType getPieceType(String piece){
        if(piece.equals("king")){
            return ChessPiece.PieceType.KING;
        }else if(piece.equals("queen")){
            return ChessPiece.PieceType.QUEEN;
        }else if(piece.equals("rook")){
            return ChessPiece.PieceType.ROOK;
        }else if(piece.equals("knight")){
            return ChessPiece.PieceType.KNIGHT;
        }
        return null;

    }

    private boolean checkColor(ChessPosition position, ChessGame game){
        ChessBoard board = game.getBoard();
        ChessPiece piece = board.getPiece(position);
        if(piece.getTeamColor().equals(gameColor)){
            return true;
        }else {
            return false;
        }
    }




}