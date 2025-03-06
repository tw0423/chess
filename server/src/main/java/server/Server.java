package server;
import service.*;
import dataaccess.*;
import spark.*;
import com.google.gson.Gson;

public class Server {

    UserHandler userHandler;


    UserService userService;
    GameService gameService;

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public Server() {

        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();

        userService = new UserService(authDAO, userDAO);
//        gameService = new GameService(gameDAO, authDAO);

        userHandler = new UserHandler(userService);
//        gameHandler = new GameHandler(gameService);



    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", userHandler::register);


        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
