package client;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import websocket.commands.*;
import com.google.gson.Gson;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

public class WebsoecketCommunicator extends Endpoint{
    Session session;
    ChessClient chessClient;
    public WebsoecketCommunicator(String url, ChessClient client) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            chessClient = client;
            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    handleMessage(message);

                    Notification notification = new Gson().fromJson(message, Notification.class);
                    chessClient.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }



    private void handleMessage(String message) {
        if (message.contains("\"serverMessageType\":\"NOTIFICATION\"")) {
            Notification notif = new Gson().fromJson(message, Notification.class);
            chessClient.notify(notif);
        }
        else if (message.contains("\"serverMessageType\":\"ERROR\"")) {
            Error error = new Gson().fromJson(message, Error.class);
            chessClient.notifyError(error);
        }
        else if (message.contains("\"serverMessageType\":\"LOAD_GAME\"")) {
            LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
            chessClient.handleLoadGame(loadGame);
        }
    }

    public void handleMakeMove(MakeMoveCommand makeMove) {
        sendCommand(makeMove);

    }

    public void handleResign(ResignCommand resignCommand){
        sendCommand(resignCommand);
    }

    public void handleLeaveGame(LeaveCommand leaveCommand) {
        sendCommand(leaveCommand);

    }

    private void sendCommand(UserGameCommand command) {
        String message = new Gson().toJson(command);
        this.session.getAsyncRemote().sendText(message);
    }











    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
