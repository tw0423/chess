package server;
import model.*;
import java.util.List;

public interface GameDAO {

    List<GameData> listGames();
    void createGame(GameData game);
    GameData getGame(int gameID);
    boolean gameExists(int gameID);

    void updateGame(GameData game);
    void clear();
}

