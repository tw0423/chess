package dataaccess;
import model.AuthData;

import java.sql.SQLException;
import java.sql.ResultSet;
public class SQLAuthDAO implements AuthDAO {

    private final String createAuthStatements =
            """
                        CREATE TABLE if NOT EXISTS auth (
                                        username VARCHAR(255) NOT NULL,
                                        authToken VARCHAR(255) NOT NULL,
                                        PRIMARY KEY (authToken)
                                        )
            """;

    public SQLAuthDAO() throws DataAccessException {

        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createAuthStatements)) {
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                throw new DataAccessException(e.getMessage());

            }
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    public void addAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, authData.username());
                statement.setString(2, authData.authToken());
                statement.executeUpdate();
            }
        }catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void removeAuth(String authToken) throws DataAccessException {
        String sql = "DELETE FROM auth WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, authToken);
                statement.executeUpdate();
            }
        }catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public AuthData getAuth(String passedInAuthToken) throws DataAccessException {

        String sql = "SELECT username, authToen from auth WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, passedInAuthToken);
                ResultSet rs = statement.executeQuery();
                return new AuthData(rs.getString("username"), rs.getString("authToken"));

            }
        }catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearAuth() throws DataAccessException {
        String sql = "TRUNCATE auth";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        }catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}





