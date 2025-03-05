package dataaccess;
import model.*;
public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData user) throws DataAccessException ;
//    boolean authenticateUser(String username, String password);
    // we wil check the return in the  service, so probably not gonna do it here
    void clearUsers() throws DataAccessException;
}
