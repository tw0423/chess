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
import java.util.Objects;
import chess.*;
import websocket.messages.Notification;

@WebSocket
public class WebsocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        Server.connections.add( session, 0);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Server.connections.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        if (message.contains("\"commandType\":\"CONNECT\"")) {
            ConnectCommand command = new Gson().fromJson(message, ConnectCommand.class);
            Server.connections.replace(  session, command.getGameID());
            manageConnect(session, command);
        }
        else if (message.contains("\"commandType\":\"MAKE_MOVE\"")) {
            MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
            manageMakeMove(session, command);
        }
        else if (message.contains("\"commandType\":\"LEAVE\"")) {
            LeaveCommand command = new Gson().fromJson(message, LeaveCommand.class);
            manageLeave(session, command);
        }
        else if (message.contains("\"commandType\":\"RESIGN\"")) {
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

            AuthData authData = Server.userService.getAuth(authToken);
            String userName = authData.authToken();

            ChessGame.TeamColor joinColor = getTeamColor(authData, gameData);

            boolean isPlayer;
            Notification notification;
            if(joinColor == null){
//                isPlayer = false;
                notification = new Notification("%s joined the game as observer".formatted(userName));
            }else{
//                isPlayer = true;
                 notification = new Notification("%s:%s joined the game as player".formatted(joinColor.toString(), userName));
            }

            Server.connections.replace( session, gameID);
            Server.connections.broadcastInGame(session, notification);
        }
        catch (DataAccessException e) {
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

            ChessGame game = gameData.game();
            ChessGame.TeamColor joinColor = getTeamColor(authData, gameData);
            //error if the connector is in an observer
            if(joinColor == null){
                Error error = new Error("You are not allowed to make a move in this game since you are just observing the game");
                sendErrorMessage(session, error);
            }
            ChessGame.TeamColor opponentColor = getOpponentColor(joinColor);
            ChessMove move = command.getMove();

            String opponentName = (opponentColor == ChessGame.TeamColor.BLACK ? gameData.blackUsername() : gameData.whiteUsername());



            if(joinColor == game.getTeamTurn()) {
                game.makeMove(move);
                if(game.isInCheck(opponentColor)){
                    Notification notify = new Notification("%s:%s is checkmated".formatted(opponentColor.toString(), opponentName));
                    Server.connections.broadcastInGame(session, notify);
                }
                else if(game.isInStalemate(opponentColor)){
                    Notification notify = new Notification("Both of you are in stalemate");
                    Server.connections.broadcastInGame(session, notify);

                }
                else if(game.isInCheck(opponentColor)){
                    Notification notify = new Notification("%s:%s is in check".formatted(opponentColor.toString(), opponentName));
                    Server.connections.broadcastInGame(session, notify);
                }

                //update the game
                GameData newData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
                Server.gameService.updateGame(authToken, newData);
                LoadGame load = new LoadGame(game);
                Server.connections.broadcastInGame(session, load);


            }
            else{
                Error error = new Error("It's not your term yet");
                sendErrorMessage(session, error);
                //send the error messages showing that it is not your term right now
            }


        }
        catch (DataAccessException e) {
            Error error = new Error(e.getMessage());
//            sendErrorMessage(session, error);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        }

    }



    private ChessGame.TeamColor getTeamColor(AuthData authdata, GameData gameData) {
        String userName = authdata.username();
        String whiteName = gameData.whiteUsername();
        String blackName = gameData.blackUsername();
        if(Objects.equals(userName, whiteName)){
            return ChessGame.TeamColor.WHITE;
        }
        else if(Objects.equals(userName, blackName)){
            return ChessGame.TeamColor.BLACK;
        }
        return null;

    }

    public void manageLeave(Session session, ResignCommand command) {
        try {
            String authToken = command.getAuthToken();

            AuthData authData = Server.userService.getAuth(authToken);
            Server.connections.remove(session);

            Notification notification = new Notification("%s has left the game".formatted(authData.username()));
            Server.connections.broadcastInGame(session, notification);
            session.close();
        }catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void manageResign(Session session, ResignCommand command) {
        try{
            String authToken = command.getAuthToken();
            int gameID = command.getGameID();
            AuthData authData = Server.userService.getAuth(authToken);
            GameData gameData = Server.gameService.getGame(gameID);
            ChessGame.TeamColor userColor = getTeamColor(authData, gameData);
            
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private ChessGame.TeamColor getOpponentColor(ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }

    private void sendErrorMessage(Session session, Error message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }



    //留言板
    //I have't impelemting the situation that the game is over, I am kinda lazy rn




}
