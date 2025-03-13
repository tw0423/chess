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
    private GameData updatedGame1 = new GameData(1, "volunteer", null, "game1", new ChessGame());
    private GameData badUpdatedGame1 = new GameData(2, "volunteer", null, "game1", new ChessGame());





    @Test
    public void testPositiveCreatGames() throws SQLException {
        try {
            gameDAO.clear();
            gameDAO.createGame(game1);
            ArrayList<GameData> lists = gameDAO.listGames();

            Assertions.assertEquals(1, lists.size());

        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    public void testNegativeGetGame() throws SQLException {
        try {
            gameDAO.clear();
            gameDAO.createGame(game1);
            ArrayList<GameData> lists = gameDAO.listGames();

            Assertions.assertEquals(1, lists.size());
            GameData data = gameDAO.getGame(2);
            Assertions.assertNull(data);

        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    public void testPositiveGetGame() throws SQLException {
        try {
            gameDAO.clear();
            gameDAO.createGame(game1);
            ArrayList<GameData> lists = gameDAO.listGames();

            Assertions.assertEquals(1, lists.size());
            GameData data = gameDAO.getGame(1);
            Assertions.assertNotNull(data);
            Assertions.assertEquals(data, game1);


        } catch (DataAccessException e) {
            assert false;
        }
    }

    @Test
    public void testPositiveUpdateGame() throws SQLException {
        try {
            gameDAO.clear();
            gameDAO.createGame(game1);
            ArrayList<GameData> lists = gameDAO.listGames();

            Assertions.assertEquals(1, lists.size());
            GameData data = gameDAO.getGame(1);
            Assertions.assertNotNull(data);
            Assertions.assertEquals(data, game1);

            gameDAO.updateGame(updatedGame1);
            GameData updatedData = gameDAO.getGame(1);
            Assertions.assertNotEquals(updatedData, data);


        } catch (DataAccessException e) {
            assert false;
        }
    }


    @Test
    public void testNegativeUpdateGame() throws SQLException {
        try {
            gameDAO.clear();
            gameDAO.createGame(game1);
            ArrayList<GameData> lists = gameDAO.listGames();

            Assertions.assertEquals(1, lists.size());
            GameData data = gameDAO.getGame(1);
            Assertions.assertNotNull(data);
            Assertions.assertEquals(data, game1);

            gameDAO.updateGame(badUpdatedGame1);
            assert false;



        } catch (DataAccessException e) {
            assert true;
        }
    }






}
