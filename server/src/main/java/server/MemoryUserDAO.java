package server;

import java.util.ArrayList;

import model.*;


public class MemoryUserDAO implements UserDAO {
    ArrayList<UserData> users;
    public MemoryUserDAO() {
        users = new ArrayList<>();
    }

    @Override
    public UserData getUser(String username) {
        if (users.isEmpty()){
            return null;
        }

        for (UserData user : users) {
            if(user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void createUser(UserData user) {
        users.add(user);
    }

    @Override
    public void clear() {
        users.clear();
    }
}


//
//public interface UserDAO {
//    UserData getUser(String username);
//    void createUser(UserData user) ;
//    //    boolean authenticateUser(String username, String password);
//    // we wil check the return in the  service, so probably not gonna do it here
//    void clear();
//}
