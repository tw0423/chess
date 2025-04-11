package server;
import excpetion.UnsureException;
import server.sessions.ConnectionsManager;
import service.*;
import dataaccess.*;
import spark.*;


public class Server {

    UserHandler userHandler;
    GameHandler gameHandler;


    static UserService userService;
    static GameService gameService;

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    static ConnectionsManager connections;

//    private WebsocketHandler webSocketHandler;
    public Server() {
//change it here
        try {
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


        userService = new UserService(authDAO, userDAO);
        gameService = new GameService(authDAO, gameDAO);

        userHandler = new UserHandler(userService);
        gameHandler = new GameHandler(gameService);

        connections = new ConnectionsManager();

//        webSocketHandler = new WebsocketHandler();



    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", WebsocketHandler.class);

        Spark.delete("/db", this::clear);
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);

        Spark.get("/game", gameHandler::listGames);
        Spark.post("/game", gameHandler::createGame);
        Spark.put("/game", gameHandler::joinGame);

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
    //??ask tomorrow how to handle this error here
    public void clearDB() throws UnsureException {

        userService.clear();
        gameService.clear();

    }

    private Object clear(Request req, Response resp) {

        try {
            clearDB();
        } catch (UnsureException e) {
            resp.status(200);
            return e.getMessage();
        }

        resp.status(200);
        return "{}";
    }

}
