package dataaccess;
import model.*;
import java.util.ArrayList;

public interface GameDAO {

    ArrayList<GameData> listGames() throws DataAccessException;
    void createGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID)  throws DataAccessException;

    void updateGame(GameData game)  throws DataAccessException;
    void clear() ;
}

