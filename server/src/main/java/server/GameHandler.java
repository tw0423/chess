package server;

import com.google.gson.Gson;
import service.*;
import spark.Request;
import spark.Response;
import reqres.*;
import excpetion.*;

public class GameHandler {
    GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }


    public Object listGames(Request req, Response resp) {
        try {
            String authToken = req.headers("authorization");
            ListGameReponse response = gameService.listGames(authToken);

            resp.status(200);
            return new Gson().toJson(response);
        } catch (UnauthorizedException e){
            resp.status(401);
            return e.getMessage();
        } catch (UnsureException e){
            resp.status(500);
            return e.getMessage();
        }

    }

    public Object createGame(Request req, Response resp) {
        try {

            if (!req.body().contains("\"gameID\":")) {

                String authToken = req.headers("authorization");
                CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);

                CreateGameResponse response = gameService.createGame(authToken, request);

                resp.status(200);
                return new Gson().toJson(response);

                //UnsureException
                // UnauthorizedException
            }
        }catch (UnsureException e){
            resp.status(500);
            return e.getMessage();
        } catch (UnauthorizedException e){
            resp.status(401);
            return e.getMessage();
        } catch (BadRequestException e){
            resp.status(400);
            return e.getMessage();
        }
        return null;
    }

    public Object joinGame(Request req, Response resp) throws BadRequestException{
        try {

            if (!req.body().contains("\"gameID\":")) {
                resp.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            }

            String authToken = req.headers("authorization");
            JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
            gameService.joinGame(authToken, joinGameRequest);

            resp.status(200);
            return "{}";

            // UnsureException, UnauthorizedException, BadRequestException, AlreadyTakenException
        } catch (BadRequestException e){
            resp.status(400);
            return e.getMessage();
        } catch (UnauthorizedException e){
            resp.status(401);
            return e.getMessage();
        }catch (AlreadyTakenException e) {
            resp.status(403);
            return e.getMessage();
        }catch (UnsureException e) {
            resp.status(500);
            return e.getMessage();
        }
    }

}
