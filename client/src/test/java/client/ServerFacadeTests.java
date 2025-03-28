package client;

import excpetion.AlreadyTakenException;
import excpetion.BadRequestException;
import excpetion.UnauthorizedException;
import excpetion.UnsureException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import reqres.CreateGameResponse;
import reqres.ListGameReponse;
import reqres.LoginResponse;
import reqres.RegisterResponse;
import server.Server;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:"  + port);
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerAndLoginTest() {
        try {
            serverFacade.clear();
            RegisterResponse res1 = serverFacade.registerUser("volunteer","aa","aa");
            Assertions.assertNotNull(res1);
            Assertions.assertTrue((res1.username().equals("volunteer")));
            LoginResponse res = serverFacade.loginUser("volunteer", "aa");
            Assertions.assertNotNull(res);
            assert true;

        } catch (AlreadyTakenException | BadRequestException | UnauthorizedException | UnsureException e) {
            assert false;
        }

    }

    @Test
    public void faliedRegisteation() {
        try {
            serverFacade.clear();
            RegisterResponse data = serverFacade.registerUser("volunteer","aa","aa");
            Assertions.assertNotNull(data);
            RegisterResponse data1 = serverFacade.registerUser("volunteer","dd","aa");
            assert false;

        } catch (AlreadyTakenException e) {
            assert true;
        } catch (BadRequestException e) {
            assert false;
        } catch(UnsureException e) {
            assert true;
        }
    }

    @Test
    public void faliedRegisteation2() {
        try {
            serverFacade.clear();
            RegisterResponse data = serverFacade.registerUser("volunteer",null,"aa");
            assert false;
        } catch (AlreadyTakenException e) {
            assert false;
        } catch (BadRequestException e) {
            assert true;
        } catch (UnsureException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void faliedLogin1() {
        try {
            serverFacade.clear();
            RegisterResponse data = serverFacade.registerUser("volunteer","aa","aa");
            Assertions.assertNotNull(data);
            Assertions.assertTrue((data.username().equals("volunteer")));
            LoginResponse res = serverFacade.loginUser("volunteer", "wrong");

            assert false;
        } catch (AlreadyTakenException | BadRequestException | UnsureException e) {
            assert false;
        } catch (UnauthorizedException e) {
            assert true;
        }
    }

    @Test
    public void faliedLogin2() {
        try {
            serverFacade.clear();
            RegisterResponse data = serverFacade.registerUser("volunteer","aa","aa");
            Assertions.assertNotNull(data);
            Assertions.assertTrue((data.username().equals("volunteer")));
            LoginResponse res = serverFacade.loginUser("volunteer", null);

            assert false;
        } catch (AlreadyTakenException | UnsureException e) {
            assert false;
        } catch (BadRequestException e) {
            assert true;
        } catch (UnauthorizedException e) {
            assert true;
        }
    }

    @Test
    public void faliedLogin3() {
        try {
            serverFacade.clear();
            RegisterResponse data = serverFacade.registerUser("volunteer","aa","aa");
            Assertions.assertNotNull(data);
            Assertions.assertTrue((data.username().equals("volunteer")));
            LoginResponse res = serverFacade.loginUser("abc", "a");

            assert false;
        } catch (AlreadyTakenException | UnsureException e) {
            assert false;
        } catch (BadRequestException e) {
            assert true;
        } catch (UnauthorizedException e) {
            assert true;
        }
    }

    @Test
    public void goodCreateAndList() {
        try {
            serverFacade.clear();
            RegisterResponse data = serverFacade.registerUser("volunteer","aa","aa");
            Assertions.assertNotNull(data);
            Assertions.assertEquals("volunteer", data.username());
            LoginResponse res = serverFacade.loginUser("volunteer", "aa");
            Assertions.assertNotNull(res);


            serverFacade.createGame("game1");
            ListGameReponse response = serverFacade.getGames();
            Assertions.assertNotNull(response);
            Assertions.assertEquals(1, response.games().size());

            assert true;
        } catch (AlreadyTakenException | BadRequestException | UnsureException e) {
            assert false;
        } catch (UnauthorizedException e) {
            assert true;
        }
    }

    @Test
    public void createAndJoinGame() {
        try {
            serverFacade.clear();
            RegisterResponse data = serverFacade.registerUser("volunteer","aa","aa");
            Assertions.assertNotNull(data);
            Assertions.assertEquals("volunteer", data.username());
            LoginResponse res = serverFacade.loginUser("volunteer", "aa");
            Assertions.assertNotNull(res);


            CreateGameResponse createRes = serverFacade.createGame("game1");
            ListGameReponse response = serverFacade.getGames();
            Assertions.assertNotNull(response);
            Assertions.assertNotNull(createRes);
            Assertions.assertEquals(1, response.games().size());

            serverFacade.joinGame("white", createRes.gameID());
            response = serverFacade.getGames();
            ArrayList<GameData> games = response.games();

            for(GameData game : games){
                if(game.gameName().equals("game1")){
                    Assertions.assertEquals(game.gameID(), createRes.gameID());
                }
            }
            assert true;
        } catch (AlreadyTakenException | BadRequestException | UnauthorizedException | UnsureException e) {
            assert false;
        }
    }

    @Test
    public void joinGameNotExit() {
        try {
            serverFacade.clear();
            RegisterResponse data = serverFacade.registerUser("volunteer","aa","aa");
            Assertions.assertNotNull(data);
            Assertions.assertEquals("volunteer", data.username());
            LoginResponse res = serverFacade.loginUser("volunteer", "aa");
            Assertions.assertNotNull(res);


            CreateGameResponse createRes = serverFacade.createGame("game1");



            serverFacade.joinGame("orange", createRes.gameID());
            ListGameReponse response = serverFacade.getGames();
            ArrayList<GameData> games = response.games();

            assert false;
        } catch (AlreadyTakenException | UnauthorizedException | UnsureException e) {
            assert false;
        }catch(BadRequestException e){
            assert true;
        }
    }

    @Test
    public void joinGameBadID() {
        try {
            serverFacade.clear();
            RegisterResponse data = serverFacade.registerUser("volunteer","aa","aa");
            Assertions.assertNotNull(data);
            Assertions.assertEquals("volunteer", data.username());
            LoginResponse res = serverFacade.loginUser("volunteer", "aa");
            Assertions.assertNotNull(res);


            CreateGameResponse createRes = serverFacade.createGame("game1");



            serverFacade.joinGame("white", 1234);
            ListGameReponse response = serverFacade.getGames();
            ArrayList<GameData> games = response.games();

            assert false;
        } catch (AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            assert false;
        }catch(UnsureException e){
            assert true;
        }
    }


    @Test
    public void unAuthroizedCreateGame() {

        try {
            serverFacade.clear();
            CreateGameResponse createRes = serverFacade.createGame("game1");


            assert false;
        } catch (BadRequestException e) {
            assert false;
        }catch(UnauthorizedException e){
            assert true;
        }
    }

    @Test
    public void logout() {
        try {
            serverFacade.clear();
            RegisterResponse data = serverFacade.registerUser("volunteer","aa","aa");
            Assertions.assertNotNull(data);
            Assertions.assertEquals("volunteer", data.username());
            LoginResponse res = serverFacade.loginUser("volunteer", "aa");
            Assertions.assertNotNull(res);

            serverFacade.logoutUser();
            CreateGameResponse createRes = serverFacade.createGame("game1");



            assert false;
        } catch (AlreadyTakenException | UnsureException | BadRequestException e) {
            assert false;
        }catch(UnauthorizedException e){
            assert true;
        }
    }



}
