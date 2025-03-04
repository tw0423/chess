package dataaccess;
import model.GameData;
import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    ArrayList<GameData> gamesList;
    public MemoryGameDAO() {
        gamesList = new ArrayList<>();
    }
    @Override
    public ArrayList<GameData> listGames() {
        return gamesList;
    }

    @Override
    public void createGame(GameData game) {
        gamesList.add(game);

    }

    @Override
    public GameData getGame(int gameID) throws AccessDeniedException, EmptyDataException {
        if (gamesList.isEmpty()) {
            throw new EmptyDataException("Game list is empty");
        }
        for (GameData game: gamesList) {
            if(game.gameID() == gameID) {
                return game;
            }
        }
        throw new AccessDeniedException("Game not found");
    }

    @Override
    public boolean ExistGame(int gameID) throws EmptyDataException, AccessDeniedException {
        if (gamesList.isEmpty()) {
            throw new EmptyDataException("Game list is empty");
        }
        for (GameData game: gamesList) {
            if(game.gameID() == gameID) {
                return true;
            }
        }
        throw new AccessDeniedException("Game not found");
    }

    @Override
    public void updateGame(GameData game) throws AccessDeniedException {
        for(GameData originalGame: gamesList) {
            if(game.gameID() == originalGame.gameID()) {
                gamesList.remove(originalGame);
                gamesList.add(game);
            }
            else {
                throw new AccessDeniedException("Game not found");
            }
        }
    }

    @Override
    public void clear() {
        gamesList.clear();

    }
}
