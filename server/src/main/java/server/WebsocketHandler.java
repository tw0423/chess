package server;


import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import websocket.commands.*;
import com.google.gson.Gson;

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
            Server.connections.replace( command.getGameID(), session);
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

    }

    private void manageMakeMove(Session session, MakeMoveCommand command) {
        GameData gameData = Server.gameService.

    }





}
