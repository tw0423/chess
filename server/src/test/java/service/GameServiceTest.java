package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

public class GameServiceTest {
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    UserDAO userDAO = new MemoryUserDAO();
    UserService userService = new UserService(authDAO, userDAO);
    GameService gameService = new GameService(authDAO, gameDAO);

    @Test
    void successfulCreateGameTest() {
        RegisterRequest request  = new RegisterRequest("volunteer1","abcde","volunteer1@gmail.com");
        CreateGameRequest createGameRequest = new CreateGameRequest("TestingGame");

        try {
            RegisterResponse response = userService.register(request);
            Assertions.assertNotNull(response);
            String authToken = response.authToken();
            Assertions.assertNotNull(authToken);
            CreateGameResponse createGameResponse = gameService.createGame(authToken, createGameRequest);

            GameData data = gameDAO.getGame(createGameResponse.gameID());
            Assertions.assertNotNull(data);
        }catch (Exception e) {
            assert false;
        }
    }

    @Test
    void badRequestCreateGameTest() {
        RegisterRequest request  = new RegisterRequest("volunteer1","abcde","volunteer1@gmail.com");
        CreateGameRequest createGameRequest = new CreateGameRequest(null);

        try {
            RegisterResponse response = userService.register(request);
            Assertions.assertNotNull(response);
            String authToken = response.authToken();
            Assertions.assertNotNull(authToken);
            CreateGameResponse createGameResponse = gameService.createGame(authToken, createGameRequest);
            assert false;

        }catch (BadRequestException e) {
            assert true;
        }catch (Exception e) {
            assert false;
        }
    }

    @Test
    void unauthorizedCreateGameTest() {
        RegisterRequest request  = new RegisterRequest("volunteer1","abcde","volunteer1@gmail.com");
        CreateGameRequest createGameRequest = new CreateGameRequest("game");

        try {
            RegisterResponse response = userService.register(request);
            Assertions.assertNotNull(response);
            String authToken = "response.authToken();";

            CreateGameResponse createGameResponse = gameService.createGame(authToken, createGameRequest);
            assert false;

        }catch (UnauthorizedException e) {
            assert true;
        }catch (Exception e) {
            assert false;
        }
    }

    @Test
    void successfulListTest() {
        RegisterRequest request  = new RegisterRequest("volunteer1","abcde","volunteer1@gmail.com");
        CreateGameRequest createGame = new CreateGameRequest("game1");
        CreateGameRequest createGame2 = new CreateGameRequest("game2");
        CreateGameRequest createGame3 = new CreateGameRequest("game3");
        CreateGameRequest createGame4 = new CreateGameRequest("game4");
        try {
            RegisterResponse response = userService.register(request);
            Assertions.assertNotNull(response);

            String authToken = response.authToken();
            gameService.createGame(authToken, createGame);
            gameService.createGame(authToken, createGame2);
            gameService.createGame(authToken, createGame3);
            gameService.createGame(authToken, createGame4);

            ListGameReponse listGameReponse = gameService.listGames(authToken);
            Assertions.assertNotNull(listGameReponse);
            ArrayList<GameData> datas = gameDAO.listGames();

            Assertions.assertEquals(datas, listGameReponse.games());
        }catch (Exception e) {
            assert false;
        }
    }



}
