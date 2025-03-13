package dataaccess;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

import java.sql.SQLException;

public class AuthDAOTest {
    AuthDAO authDAO;

    {
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private UserData userData = new UserData("volunteer1","abcde","volunteer1@gmail.com");
    private AuthData volunteer1 = new AuthData("volunteer1","doesn't matter");
    private AuthData volunteer2 = new AuthData("volunteer2","who cares");
    private AuthData badAuth = new AuthData("volunteer2",null);

    @Test
    public void testPositiveAddAuth() throws SQLException {
        try {
            authDAO.clearAuth();
            authDAO.addAuth(volunteer1);
            AuthData data = authDAO.getAuth("doesn't matter");
            Assertions.assertNotNull(data);
            Assertions.assertEquals(volunteer1, data);

        } catch (DataAccessException e) {
            assert false;
        }
    }
    @Test
    public void testNegativeAddAuth() throws SQLException {
        try {
            authDAO.addAuth(badAuth);
            assert false;
        } catch ( DataAccessException e) {
            assert true;
        }
    }

    @Test
    public void testPositiveGetAuth() throws SQLException {
        try {
            authDAO.clearAuth();
            authDAO.addAuth(volunteer1);
            authDAO.addAuth(volunteer2);
            AuthData data = authDAO.getAuth("doesn't matter");
            Assertions.assertNotNull(data);
            Assertions.assertEquals(volunteer1, data);


        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    public void testNegativeGetAuth() throws SQLException {
        try {
            authDAO.clearAuth();
            authDAO.addAuth(volunteer1);
            authDAO.addAuth(volunteer2);
            AuthData data = authDAO.getAuth("wrong authToken");
            Assertions.assertNull(data);

        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    public void testBadInputGetAuth() throws SQLException {
        try {
            authDAO.clearAuth();
            authDAO.addAuth(volunteer1);
            authDAO.addAuth(volunteer2);
            AuthData data = authDAO.getAuth("");
            Assertions.assertNull(data);

        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    public void testPositiveRemoveAuth() throws SQLException {
        try {
            authDAO.clearAuth();
            authDAO.addAuth(volunteer1);
            authDAO.addAuth(volunteer2);
            authDAO.removeAuth("doesn't matter");
            authDAO.removeAuth("who cares");
            AuthData data = authDAO.getAuth("doesn't matter");
            Assertions.assertNull(data);
        } catch (DataAccessException e) {
            assert false;
        }
    }







}
