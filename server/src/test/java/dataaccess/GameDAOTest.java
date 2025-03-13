package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class GameDAOTest {

    GameDAO gameDAO;

    {
        try {
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }



    private UserData volunteer1 = new UserData("volunteer1","abcde","volunteer1@gmail.com");
    private GameData game1 = new GameData(1, null, null, "game1", new ChessGame());
    private GameData repeatGame = new GameData(1, null, null, "game1", new ChessGame());

    private AuthData badAuth = new AuthData("volunteer2",null);

    @Test
    public void testPositiveCreatGames() throws SQLException {
        try {
            gameDAO.createGame(game1);
            ArrayList<GameData> lists = gameDAO.listGames();

            Assertions.assertEquals(1, lists.size());

        } catch (DataAccessException e) {
            assert false;
        }
    }





}
