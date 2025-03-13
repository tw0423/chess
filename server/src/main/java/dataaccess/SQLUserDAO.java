package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class SQLUserDAO implements UserDAO {


    public SQLUserDAO() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }

        try (var conn = DatabaseManager.getConnection()) {
            String createGameStatements = """
                    CREATE TABLE if NOT EXISTS userTable (
                            username VARCHAR(255),
                            password VARCHAR(255),
                            email VARCHAR(255),
                            PRIMARY KEY (username)
                            )
                    """;
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
        String sql = "SELECT username,password,email FROM userTable WHERE username = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    String passwordFromDB = rs.getString("password");
                    String emailFromDB = rs.getString("email");


                    if (checkHashedPassword( passwordFromDB, password)) {
                        return new UserData(username, password, emailFromDB);
                    } else {
                        return null;
                    }
                }
                return null;

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            var sql = "INSERT INTO userTable ( username, password, email) VALUES (?, ?, ?)";
            try (var statement = conn.prepareStatement(sql)) {
                updateStatement(statement, user.username(), 1);
                String hashedPassword = createHashPassword(user.password());
                updateStatement(statement, hashedPassword, 2);
                updateStatement(statement, user.email(), 3);
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
    public ArrayList<UserData> getUsers() throws DataAccessException{
        ArrayList<UserData> userDataArrayList = new ArrayList<UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username,password,email FROM userTable";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        userDataArrayList.add(readUser(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return userDataArrayList;
    }


    private UserData readUser(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    private void updateStatement(PreparedStatement statement, String string, int index) throws SQLException {
        statement.setString(index, string);
    }

    private boolean checkHashedPassword(String hashedPassword, String providedClearTextPassword) {
        // read the previously hashed password from the database

        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

    private String createHashPassword(String clearTextPassword) {

        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
        // write the hashed password in database along with the user's other information
    }





}
