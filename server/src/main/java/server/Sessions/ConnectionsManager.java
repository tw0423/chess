package server.Sessions;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;
import websocket.messages.*;
//public class ConnectionsManager {
//    public final ConcurrentHashMap<Integer, Connection> connections = new ConcurrentHashMap<>();
//
//    public void add(int gameID, Session session) {
//        var connection = new Connection(gameID, session);
//        connections.put(gameID, connection);
//    }
//
//    public void remove(Session session) {
//        Integer targetKey = null;
//        for (var entry : connections.entrySet()) {
//            if (entry.getValue().session.equals(session)) {
//                targetKey = entry.getKey();
//                break;
//            }
//        }
//        if (targetKey != null) {
//            connections.remove(targetKey);
//        }
//    }
//
//    public void broadcast(int excludeGameID, Notification notification) throws IOException {
//        var removeList = new ArrayList<Connection>();
//        for (var c : connections.values()) {
//            if (c.session.isOpen()) {
//                if (c.gameID != (excludeGameID)) {
//                    c.send(notification.toString());
//                }
//            } else {
//                removeList.add(c);
//            }
//        }
//
//        // Clean up any connections that were left open.
//        for (var c : removeList) {
//            connections.remove(c.gameID);
//        }
//    }
//
//    public void replace(int gameID, Session session) {
//        Connection connection = new Connection(gameID, session);
//        connections.put(gameID, connection);
//
//    }
//}

public class ConnectionsManager {
    private final ConcurrentHashMap< Session, Integer> connections = new ConcurrentHashMap<>();

    public void add( Session session, int gameID) {
        connections.put(session, gameID);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(int excludeGameID, Notification notification) throws IOException {
        int gameID;
        var removeList = new ArrayList<Session>();
        for (var c : connections.entrySet()) {
            gameID = c.getValue();
            Session session = c.getKey();
            if (session.isOpen()) {
                if (gameID != (excludeGameID)) {
                    send(session, notification.toString());
                }
            } else {
                removeList.add(session);
            }
        }

        // Clean up any connections that were left open.
        for (var session : removeList) {
            connections.remove(session);
        }
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




