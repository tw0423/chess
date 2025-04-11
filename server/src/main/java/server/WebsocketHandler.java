package server;


import chess.ChessGame;
import dataaccess.DataAccessException;
import excpetion.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import websocket.commands.*;
import com.google.gson.Gson;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import chess.*;

@WebSocket
public class WebsocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        Server.connections.add(session, 0);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Server.connections.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        if (message.contains("\"commandType\":\"CONNECT\"")) {
            ConnectCommand command = new Gson().fromJson(message, ConnectCommand.class);
            Server.connections.replace(session, command.getGameID());
            manageConnect(session, command);
        } else if (message.contains("\"commandType\":\"MAKE_MOVE\"")) {
            MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
            manageMakeMove(session, command);
        } else if (message.contains("\"commandType\":\"LEAVE\"")) {
            LeaveCommand command = new Gson().fromJson(message, LeaveCommand.class);
            manageLeave(session, command);
        } else if (message.contains("\"commandType\":\"RESIGN\"")) {
            ResignCommand command = new Gson().fromJson(message, ResignCommand.class);
            manageResign(session, command);
        }
    }

    private void manageConnect(Session session, ConnectCommand command) {
        //add the session into the connectionsmanager
        //check is it the observer or player
        //if it is observer, boradcast ..observer
        //if it is gameplaer, broadcast .. joiner
        try {
            int gameID = command.getGameID();
            String authToken = command.getAuthToken();

            GameData gameData = Server.gameService.getGame(gameID);
            if(gameData == null){
                Error error = new Error("wrong gameID");
                sendErrorMessage(session, error);
            }
            AuthData authData = Server.userService.getAuth(authToken);
            if (authData == null) {
                // Send an error notification or handle accordingly
                try {
                    sendErrorMessage(session, new Error("Invalid auth token"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            String userName = authData.authToken();

            ChessGame.TeamColor joinColor = getTeamColor(authData, gameData);

            boolean isPlayer;
            Notification notification;
            if (joinColor == null) {
//                isPlayer = false;
                notification = new Notification("%s joined the game as observer".formatted(userName));
            } else {
//                isPlayer = true;
                notification = new Notification("%s:%s joined the game as player".formatted(joinColor.toString(), userName));
            }

            Server.connections.replace(session, gameID);
            sendLoadGame(session, new LoadGame(gameData.game()));
            Server.connections.broadcastInGame(session, notification, false);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void manageMakeMove(Session session, MakeMoveCommand command) {
        try {
            int gameID = command.getGameID();
            String authToken = command.getAuthToken();
            GameData gameData = Server.gameService.getGame(gameID);

            AuthData authData = Server.userService.getAuth(authToken);

            if(authData == null){
                Error error = new Error("not authorized");
                sendErrorMessage(session, error);
                return;
            }

            ChessGame game = gameData.game();

            if(game.gameOver){
                Error error = new Error("game is alrealdy over.");
                sendErrorMessage(session, error);
                return;
            }
            ChessGame.TeamColor joinColor = getTeamColor(authData, gameData);
            //error if the connector is in an observer
            if (joinColor == null) {
                Error error = new Error("You are not allowed to make a move in this game since you are just observing the game");
                sendErrorMessage(session, error);
                return;
            }
            ChessGame.TeamColor opponentColor = getOpponentColor(joinColor);
            ChessMove move = command.getMove();

            Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());

            if(!validMoves.contains(move)){
                Error error = new Error("invalid move");
                sendErrorMessage(session, error);
                return;
            }



            String opponentName = (opponentColor == ChessGame.TeamColor.BLACK ? gameData.blackUsername() : gameData.whiteUsername());


            if (joinColor == game.getTeamTurn()) {
                game.makeMove(move);
                if (game.isInCheck(opponentColor)) {
                    Notification notify = new Notification("%s:%s is in check".formatted(opponentColor.toString(), opponentName));
                    Server.connections.broadcastInGame(session, notify, true);
                } else if (game.isInStalemate(opponentColor)) {
                    Notification notify = new Notification("Both of you are in stalemate");
                    Server.connections.broadcastInGame(session, notify, true);

                } else if (game.isInCheckmate(opponentColor)) {
                    Notification notify = new Notification("%s:%s is in checkmated".formatted(opponentColor.toString(), opponentName));
                    Server.connections.broadcastInGame(session, notify,true);
                }

                //update the game
                GameData newData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
                Server.gameService.updateGame(authToken, newData);
                LoadGame load = new LoadGame(game);
                Server.connections.broadcastInGame(session, load, true);
                Notification notification = new Notification("%s:%s just made a new move.".formatted(joinColor.toString(),authData.username()));
                Server.connections.broadcastInGame(session, notification, false);

            } else {
                Error error = new Error("It's not your term yet");
                sendErrorMessage(session, error);
                //send the error messages showing that it is not your term right now
            }


        } catch (DataAccessException | IOException | InvalidMoveException | UnauthorizedException e) {
            Error error = new Error(e.getMessage());
            try {
                sendErrorMessage(session, error);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public void manageLeave(Session session, LeaveCommand command) {
        try {

            String authToken = command.getAuthToken();
            int gameID = command.getGameID();
            GameData gameData = Server.gameService.getGame(gameID);
            AuthData authData = Server.userService.getAuth(authToken);
            String userName = authData.username();

            GameData updateGame;

            if(userName.equals(gameData.blackUsername())){
                updateGame = new GameData(gameID, gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            }else{
                updateGame = new GameData(gameID, null,gameData.blackUsername() , gameData.gameName(), gameData.game());
            }
            Notification notification = new Notification("%s has left the game".formatted(authData.username()));
            Server.gameService.updateGame(authToken,updateGame);
            Server.connections.broadcastInGame(session, notification, false);
            Server.connections.remove(session);
            session.close();



        } catch (DataAccessException | IOException |UnauthorizedException e) {
            Error error = new Error(e.getMessage());
            try {
                sendErrorMessage(session, error);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void manageResign(Session session, ResignCommand command) {
        try {
            String authToken = command.getAuthToken();
            int gameID = command.getGameID();
            AuthData authData = Server.userService.getAuth(authToken);
            GameData gameData = Server.gameService.getGame(gameID);
            ChessGame game = gameData.game();
            if(game.gameOver){
                Error error = new Error("game is alrealdy over.");
                sendErrorMessage(session, error);

                return;
            }
            ChessGame.TeamColor userColor = getTeamColor(authData, gameData);

            if (userColor == null) {
                Error error = new Error("You are not allowed to make a resign in a game as a observer");
                sendErrorMessage(session, error);
                return;
            }
            game.setGameOver();
            Server.gameService.updateGame(authToken, new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game));
            Notification notification = new Notification("%s:%s has resigned. %s now is over".formatted(userColor.toString(), authData.username(), gameData.gameName()));
            Server.connections.broadcastInGame(session, notification,true);
        } catch (DataAccessException | IOException | UnauthorizedException e) {
            Error error = new Error(e.getMessage());
            try {
                sendErrorMessage(session, error);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    private ChessGame.TeamColor getTeamColor(AuthData authdata, GameData gameData) {
        String userName = authdata.username();
        String whiteName = gameData.whiteUsername();
        String blackName = gameData.blackUsername();
        if (Objects.equals(userName, whiteName)) {
            return ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(userName, blackName)) {
            return ChessGame.TeamColor.BLACK;
        }
        return null;

    }


    private ChessGame.TeamColor getOpponentColor(ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }

    private void sendErrorMessage(Session session, Error message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

    private void sendLoadGame(Session session, LoadGame message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

    private void sendNotification(Session session, Notification message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));

    }




    //留言板
    //I have't impelemting the situation that the game is over, I am kinda lazy rn


}
