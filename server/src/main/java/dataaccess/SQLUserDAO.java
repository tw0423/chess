package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class SQLUserDAO implements UserDAO {

    private final String createGameStatements = """

    CREATE TABLE if NOT EXISTS userTable (
            username VARCHAR(255),
            password VARCHAR(255),
            email VARCHAR(255),
            PRIMARY KEY (username)
            )
    """;




    public SQLUserDAO() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createGameStatements)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());

            }
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }


    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        String sql = "SELECT username,password,email from gameTable WHERE username = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();

                String passwordFromDB = rs.getString("password");
                String emailFromDB = rs.getString("email");

                if(checkPassword(password, passwordFromDB)) {
                    return new UserData(username, password, emailFromDB);
                }else{
                    return null;
                }

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            var SQLRequest = "INSERT INTO userTable ( username, password, email) VALUES (?, ?, ?)";
            try (var statement = conn.prepareStatement(SQLRequest)) {
                statement.setString(1, user.username());
                statement.setString(2, user.password());
                statement.setString(3, user.email());
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void clear() throws DataAccessException{
        String sql = "TRUNCATE userTable";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        }catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public ArrayList<UserData> getUsers() {
        return null;
    }

    private boolean checkPassword(String password, String password2) {
        return password.equals(password2);

    }

//    private void storeUserPassword(String username, String clearTextPassword) {
//        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
//
//        // write the hashed password in database along with the user's other information
//        writeHashedPasswordToDatabase(username, hashedPassword);
//    }
}
