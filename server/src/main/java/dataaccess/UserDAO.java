package dataaccess;
import model.*;
public interface UserDAO {
    UserData getUser(String username) throws AccessDeniedException, EmptyDataException;
    void createUser(UserData user) ;
//    boolean authenticateUser(String username, String password);
    // we wil check the return in the  service, so probably not gonna do it here
    void clearUsers();
}
