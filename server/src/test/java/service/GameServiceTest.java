package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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



}
