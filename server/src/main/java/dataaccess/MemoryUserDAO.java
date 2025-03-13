package dataaccess;

import java.util.ArrayList;

import model.*;


public class MemoryUserDAO implements UserDAO {
    ArrayList<UserData> users;
    public MemoryUserDAO() {
        users = new ArrayList<>();
    }

    @Override
    public UserData getUser(String username, String password ){
        if (users.isEmpty()){
            return null;
        }

        for (UserData user : users) {
            if(user.username().equals(username) && user.password().equals(password)) {
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

    @Override
    public ArrayList<UserData> getUsers() {
        return users;
    }
}


