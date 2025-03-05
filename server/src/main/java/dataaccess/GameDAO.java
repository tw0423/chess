package dataaccess;
import model.*;
import java.util.ArrayList;

public interface GameDAO {

    ArrayList<GameData> listGames();
    void createGame(GameData game);
    GameData getGame(int gameID)  throws DataAccessException;
    boolean ExistGame(int gameID)  throws DataAccessException;

    void updateGame(GameData game)  throws DataAccessException;
    void clear();
}

