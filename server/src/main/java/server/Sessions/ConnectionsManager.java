package server.sessions;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;
import websocket.messages.*;


public class ConnectionsManager {
    private final ConcurrentHashMap< Session, Integer> connections = new ConcurrentHashMap<>();

    public void add( Session session, int gameID) {
        connections.put(session, gameID);
    }

    public void remove(Session session) {
        connections.remove(session);
    }



    public void replace( Session session, int gameID) {
        connections.put( session, gameID);

    }

    public void send(Session session, String message) throws IOException {
        session.getRemote().sendString(message);
    }

    public void broadcastInGame(Session currentSession, ServerMessage message, boolean toItSelf) throws IOException {
        int currentGameID = connections.get(currentSession);
        for (var session : connections.entrySet()) {
            int loopingGameID = session.getValue();
            Session loopSession = session.getKey();
            boolean inGame = (loopingGameID != 0);
            boolean inSameGame = (currentGameID == loopingGameID);
            if(session.getKey().equals(currentSession)) {
                if(toItSelf) {
                    send(loopSession, new Gson().toJson(message));
                }
            }else{

                if (inGame && inSameGame) {
                    send(loopSession, new Gson().toJson(message));
                }
            }

        }
    }


}




