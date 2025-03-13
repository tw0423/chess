package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.sql.ResultSet;

public class SQLGameDAO implements GameDAO {

    private final String createGameStatements =
            """            
                    CREATE TABLE if NOT EXISTS gameTable (
                                    gameID INT NOT NULL,
                                    whiteUsername VARCHAR(255),
                                    blackUsername VARCHAR(255),
                                    gameName VARCHAR(255),
                                    jsonChessGame TEXT,
                                    PRIMARY KEY (gameID)
                                    )""";

    public SQLGameDAO() throws DataAccessException {
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
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> gameDataArrayList = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, jsonChessGame FROM gameTable";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        gameDataArrayList.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return gameDataArrayList;
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            var SQLRequest = "INSERT INTO gameTable ( gameID, whiteUsername, blackUsername, gameName, jsonChessGame) VALUES (?, ?, ?, ?, ?)";
            try (var statement = conn.prepareStatement(SQLRequest)) {
                statement.setInt(1, game.gameID());
                statement.setString(2, game.whiteUsername());
                statement.setString(3, game.blackUsername());
                statement.setString(4, game.gameName());
                var jsonGame = new Gson().toJson(game.game());
                statement.setString(5, jsonGame);
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, jsonChessGame from gameTable WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setInt(1, gameID);
                ResultSet rs = statement.executeQuery();
                return readGame(rs);

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

        String sql = "UPDATE gameTable" +
                "set whiteUsername = ?, blackUsername = ?, gameName = ?, jsonChessGame = ? WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {

                statement.setString(1, game.whiteUsername());
                statement.setString(2, game.blackUsername());
                statement.setString(3, game.gameName());
                var jsonGame = new Gson().toJson(game.game());
                statement.setString(4, jsonGame);
                statement.setInt(5, game.gameID());

                if( statement.executeUpdate() !=1){
                    throw new DataAccessException(String.format("Unable to update game: %s", game.gameName()));
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }




    @Override
    public void clear() throws DataAccessException {
        String sql = "TRUNCATE auth";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        }catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        var jasonGame = rs.getString("jasonChesGame");
        ChessGame chessGame = new Gson().fromJson(jasonGame, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
    }
}
