package dataaccess;

import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;

public interface AuthDAO {
    void addAuth(AuthData authData)  throws DataAccessException;
    void removeAuth(String authToken)  throws DataAccessException;
    AuthData getAuth(String authToken)  throws DataAccessException;
    void clearAuth();
}
