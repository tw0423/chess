package dataaccess;

import java.util.ArrayList;

import model.*;


public class MemoryUserDAO implements UserDAO {
    ArrayList<UserData> users;
    public MemoryUserDAO() {
        users = new ArrayList<>();
    }

    @Override
    public UserData getUser(String username) throws AccessDeniedException , EmptyDataException{
        if (users.isEmpty()){
            throw new EmptyDataException("Empty data");
        }

        for (UserData user : users) {
            if(user.username().equals(username)) {
                return user;
            }
        }
        throw new AccessDeniedException("User not found");
    }

    @Override
    public void createUser(UserData user) {
        users.add(user);
    }

    @Override
    public void clearUsers() {
        users.clear();
    }
}


