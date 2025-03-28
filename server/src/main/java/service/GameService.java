package service;
import reqres.CreateGameRequest;
import reqres.CreateGameResponse;
import reqres.JoinGameRequest;
import reqres.ListGameReponse;
import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import excpetion.*;
public class GameService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGameReponse listGames(String authToken)  throws UnsureException, UnauthorizedException {
        try {

            if (!this.checkAuthorization(authToken)) {
                throw new UnauthorizedException("{ \"message\": \"Error: unauthorized\" }");
            }
            ArrayList<GameData> games = gameDAO.listGames();

            return new ListGameReponse(gameDAO.listGames());
        } catch (DataAccessException e) {
            throw new UnsureException(e.getMessage());
        }

    }
    //will the int be zero if missing some statement
    //if Game doesn't exit, is it a badRequest??
    public void joinGame(String authToken, JoinGameRequest request)
            throws UnsureException, UnauthorizedException, BadRequestException, AlreadyTakenException {
        try{
            if( request.playerColor() == null){
                throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
            }
            if (!this.checkAuthorization(authToken)) {
                throw new UnauthorizedException("{ \"message\": \"Error: unauthorized\" }");
            }

            String playerColor = request.playerColor();
            int gameID = request.gameID();
            AuthData authData = authDAO.getAuth(authToken);
            GameData currentGameData = gameDAO.getGame(gameID);


            String whiteUser = currentGameData.whiteUsername();
            String blackUser = currentGameData.blackUsername();

            switch (playerColor) {
                case "WHITE":
                    if (whiteUser == null || whiteUser.equals(authData.username())) {
                        whiteUser = authData.username();  // Spot is available or user is rejoining
                    } else {
                        throw new AlreadyTakenException("{ \"message\": \"Error: already taken\" }");
                    }
                    break;
                case "BLACK":
                    if (blackUser == null || blackUser.equals(authData.username())) {
                        blackUser = authData.username();  // Spot is available or user is rejoining
                    } else {
                        throw new AlreadyTakenException("{ \"message\": \"Error: already taken\" }");
                    }
                    break;
                default:
                    throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
            }

            GameData newGameData = new GameData(gameID, whiteUser, blackUser, currentGameData.gameName(), currentGameData.game());
            gameDAO.updateGame(newGameData);
        }catch(DataAccessException e){
            throw new UnsureException(e.getMessage());
        }
    }
    //what is the bad request for ???????
    public CreateGameResponse createGame(String authToken, CreateGameRequest request)
            throws BadRequestException, UnsureException, UnauthorizedException{
        try {
            if (request.gameName() == null) {
                throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
            }
            if (checkAuthorization(authToken)) {

                int gameID = ThreadLocalRandom.current().nextInt(1, 10000);
                ChessGame game = new ChessGame();
                gameDAO.createGame(new GameData(gameID, null, null, request.gameName(), game));
                return new CreateGameResponse(gameID);
            }
            else{
                throw new UnauthorizedException("{ \"message\": \"Error: unauthorized\" }");
            }
        }catch(DataAccessException e){
            throw new UnsureException(e.getMessage());
        }
    }


    private boolean checkAuthorization(String authToken) throws DataAccessException {
        try {
            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null) {
                return false;
            }
            return true;
        }catch (DataAccessException e) {
            throw e;
        }
    }

    public void clear() {

        try {
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }







}
