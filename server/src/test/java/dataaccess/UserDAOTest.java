package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAOTest {

    UserDAO userDAO;

    {
        try {
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private UserData volunteer1 = new UserData("volunteer1","abcde","volunteer1@gmail.com");
    private UserData badUser = new UserData(null,null,"null");

    private AuthData badAuth = new AuthData("volunteer2",null);

    @Test
    public void testPositiveAddUsers() throws SQLException {
        try {
            userDAO.clear();
            userDAO.createUser(volunteer1);
            ArrayList<UserData> lists = userDAO.getUsers();
            Assertions.assertEquals(1, lists.size());

        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    public void testNegaitveAddUsers() throws SQLException {
        try {
            userDAO.clear();
            userDAO.createUser(badUser);
            assert false;

        } catch (DataAccessException e) {
            assert true;
        }
    }

    @Test
    public void testPositiveGetUsers() throws SQLException {
        try {
            userDAO.clear();
            userDAO.createUser(volunteer1);
            ArrayList<UserData> lists = userDAO.getUsers();
            Assertions.assertEquals(1, lists.size());

            UserData data = userDAO.getUser("volunteer1","abcde");
            Assertions.assertNotNull(data);

        } catch (DataAccessException e) {
            assert false;
        }
    }



    @Test
    public void testNegativeGetUsers() throws SQLException {
        try {
            userDAO.clear();
            userDAO.createUser(volunteer1);
            ArrayList<UserData> lists = userDAO.getUsers();
            Assertions.assertEquals(1, lists.size());

            UserData data = userDAO.getUser("randomguy","abcde");
            Assertions.assertNull(data);

        } catch (DataAccessException e) {
            assert false;
        }
    }
    @Test
    public void testWrongPasswordGetUsers() throws SQLException {
        try {
            userDAO.clear();
            userDAO.createUser(volunteer1);
            ArrayList<UserData> lists = userDAO.getUsers();
            Assertions.assertEquals(1, lists.size());

            UserData data = userDAO.getUser("volunteer1","..");
            Assertions.assertNull(data);

        } catch (DataAccessException e) {
            assert false;
        }
    }




}
