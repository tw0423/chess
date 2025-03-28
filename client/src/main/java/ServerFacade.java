
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import model.*;
import service.CreateGameResponse;
import service.ListGameReponse;
import service.LoginResponse;

import service.UnauthorizedException;
import service.AlreadyTakenException;
import service.BadRequestException;

import chess.ChessGame.TeamColor;

public class ServerFacade {
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        this.serverUrl = url;

    }
    public UserData registerUser(String username, String password, String email) throws AlreadyTakenException ,BadRequestException{
        var path = "/user";
//        RegisterRequest registerRequest = new RegisterRequest(username, password, email);

        Map map = Map.of("username", username, "password", password, "email", email);
        try {
            UserData data = this.makeRequest("POST", path, map, UserData.class);
            return data;
        }catch (ResponseException e){
            if(e.StatusCode() == 403) {
                throw new AlreadyTakenException("Username has been alrealdy taken. Choose another Username.");

            }
            if(e.StatusCode() == 401) {
                throw new BadRequestException("Missing some required information");
            }
            //just throw error and catch error in client, it will be easier
        }
        return null;


    }

    public LoginResponse loginUser(String username, String password) throws UnauthorizedException{
        var path = "/session";
        Map map = Map.of("username", username, "password", password);
        try {
            LoginResponse response = this.makeRequest("POST", path, map, LoginResponse.class);
            //save authToken at here
            this.authToken = response.authToken();
            return response;
        }catch (ResponseException e){
            if (e.StatusCode() == 401) {
                throw new UnauthorizedException("Wrong Password");

            }
        }
        return null;

    }

    public CreateGameResponse createGame(String gameName)  throws UnauthorizedException, BadRequestException{
        var path = "/game";
        Map map = Map.of("gameName", gameName);
        try {
            return this.makeRequest("POST", path, map, CreateGameResponse.class);
        }catch (ResponseException e){
            if (e.StatusCode() == 403) {
                throw new UnauthorizedException("You need to log in first before using this command.");
            }
            if(e.StatusCode() == 400) {
                throw new BadRequestException("You are required to type in the gameName");
            }
        }
        return null;

    }

    public ListGameReponse getGames()  throws UnauthorizedException{
        var path = "/game";
        try {
            return this.makeRequest("GET", path, null, ListGameReponse.class);
        }catch (ResponseException e){
            if (e.StatusCode() == 401){
                throw new UnauthorizedException("You need to log in first before using this command.");
            }
        }
        return null;
    }

    public void joinGame(String playerColor, int gameID ) throws AlreadyTakenException, UnauthorizedException, BadRequestException{
        var path = "/game";
        String color;
        if(playerColor.equals("white")){
            color = "WHITE";
        }else{
            color = "BLACK";
        }
        Map map = Map.of("playerColor",color,"gameID", gameID);
        try {
            this.makeRequest("PUT", path, map, null);
        }catch (ResponseException e){
            if (e.StatusCode() == 400) {
                throw new BadRequestException("Missing required information to call join command.");

            }
            else if(e.StatusCode() == 401) {
                throw new UnauthorizedException("You are required to log in before using the joinGame command");

            }
            else if(e.StatusCode() == 403) {
                throw new AlreadyTakenException("The color you choosed has been taken");
            }
        }
    }

    public void logoutUser()  throws UnauthorizedException{
        var path = "/session";

        try {
            this.makeRequest("DELETE", path, null, null);
            this.authToken = null;

        }catch (ResponseException e){
            if(e.StatusCode() == 401) {
                throw new UnauthorizedException("User not logged in");
            }

        }

    }



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            //put AuthToken into the body
            if (this.getAuthToken() != null) {
                http.addRequestProperty("authorization", getAuthToken());
            }


            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
//            try (InputStream respErr = http.getErrorStream()) {
//                if (respErr != null) {
//                    // why the fromJson doesn't work
//                    throw ResponseException.fromJson(respErr);
//                }
//            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }

        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private String getAuthToken() {
        return this.authToken;
    }

    public void clear(){
        var path = "/db";


        try {
            this.makeRequest("DELETE", path, null, null);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        this.authToken = null;



    }

}