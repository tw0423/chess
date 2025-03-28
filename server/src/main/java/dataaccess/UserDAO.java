package dataaccess;
import model.*;

import java.util.ArrayList;

public interface UserDAO {
    UserData getUser(String username, String password) throws DataAccessException;
    void createUser(UserData user) throws DataAccessException ;
//    boolean authenticateUser(String username, String password);

    void clear() throws DataAccessException;
    ArrayList<UserData> getUsers() throws DataAccessException;
}
