package server;

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
    public GameData getGame(int gameID) {
        if (gamesList.isEmpty()) {
            return null;
        }
        for (GameData game: gamesList) {
            if(game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public boolean gameExists(int gameID) {
        if (gamesList.isEmpty()) {
            return false;
        }
        for (GameData game: gamesList) {
            if(game.gameID() == gameID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateGame(GameData game) {
        for(GameData originalGame: gamesList) {
            if(game.gameID() == originalGame.gameID()) {
                gamesList.remove(originalGame);
                gamesList.add(game);
            }
        }



    }

    @Override
    public void clear() {
        gamesList.clear();

    }
}
