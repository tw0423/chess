package server;
import service.*;
import spark.Request;
import spark.Response;
import model.*;
import com.google.gson.Gson;

public class UserHandler {
    UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }



    public Object register(Request req, Response resp)  {
        try {
            UserData userData = new Gson().fromJson(req.body(), UserData.class);
            RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
            RegisterResponse registerResponse = userService.register(registerRequest);

            resp.status(200);
            return new Gson().toJson(registerResponse);

        }catch (BadRequestException e) {
            resp.status(400);
            //Do I have to make it Gson?????
//            return new FailureResponse(e.getMessage());
            return e.getMessage();
        }catch (AlreadyTakenException e){
            resp.status(403);
//            return new FailureResponse(e.getMessage());
            return e.getMessage();
        }catch (UnsureException e){
            resp.status(500);
//            return new FailureResponse(e.getMessage());
            return e.getMessage();
        }
    }

    public Object login(Request req, Response resp) {
        try {
            LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResponse response = userService.login(request);

            resp.status(200);
            return new Gson().toJson(response);

        } catch (UnauthorizedException e) {
            resp.status(401);
            return e.getMessage();
        } catch (UnsureException e) {
            resp.status(500);
//            return new FailureResponse(e.getMessage());
            return e.getMessage();
        }
    }

    public Object logout(Request req, Response resp) {
        try {
            String authToken = req.headers("authorization");
            userService.logout(authToken);

            resp.status(200);
            return new Gson().toJson("");

        } catch (UnauthorizedException e) {
            resp.status(401);
            return e.getMessage();
        } catch (UnsureException e) {
            resp.status(500);
//            return new FailureResponse(e.getMessage());
            return e.getMessage();
        }
    }



}
