package server;

import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;

public interface AuthDAO {
    void addAuth(AuthData authData);
    void removeAuth(String authToken);

    AuthData getAuth(String authToken);

}
