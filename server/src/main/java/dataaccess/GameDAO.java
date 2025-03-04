package dataaccess;
import model.*;
import java.util.ArrayList;

public interface GameDAO {

    ArrayList<GameData> listGames();
    void createGame(GameData game);
    GameData getGame(int gameID) throws EmptyDataException, AccessDeniedException;
    boolean ExistGame(int gameID) throws EmptyDataException, AccessDeniedException;

    void updateGame(GameData game) throws AccessDeniedException;
    void clear();
}

