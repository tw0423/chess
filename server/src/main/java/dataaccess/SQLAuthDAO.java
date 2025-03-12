package dataaccess;
import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO {

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
            var SQLRequest = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
            try (var statement = conn.prepareStatement(SQLRequest)) {
                statement.setString(1, authData.username());
                statement.setString(2, authData.authToken());
                statement.executeUpdate();
            }
        }catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }




    private final String createAuthStatements =
        """
                    CREATE TABLE if NOT EXISTS auth (
                                    username VARCHAR(255) NOT NULL,
                                    authToken VARCHAR(255) NOT NULL,
                                    PRIMARY KEY (authToken)
                                    )""";


}
