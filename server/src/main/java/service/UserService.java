package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class UserService {
    AuthDAO authDAO;
    UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public RegisterResponse register(RegisterRequest request)  throws BadRequestException,AlreadyTakenException,UnsureException {
        try {
            String username;
            String password;
            String email;
            UserData userData;
            if (request.username() == null || request.email() == null || request.password() == null) {
                throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
            } else {
                username = request.username();
                password = request.password();
                email = request.email();
                userData = new UserData(username, password, email);
            }
            UserData userData1 = userDAO.getUser(username);
            if (userData1 != null) {
                throw new AlreadyTakenException("{ \"message\": \"Error: already taken\" }");
            }

            //create User
            userDAO.createUser(userData);
            AuthData newAuthData = this.createAddAuth(username);
            return new RegisterResponse(username, newAuthData.authToken());

        }catch(DataAccessException e) {
            throw new UnsureException(e.getMessage());
        }
    }

    public LoginResponse login(LoginRequest request) throws UnauthorizedException, UnsureException {
        try{
            String username = request.username();
            String password = request.password();
            UserData userData = userDAO.getUser(username);

            if(!userData.password().equals(password)) {
                throw new UnauthorizedException("{ \"message\": \"Error: unauthorized\" }");
            }
            AuthData NewAuthData = this.createAddAuth(username);
            return new LoginResponse(username, NewAuthData.authToken());
        }catch(DataAccessException e) {
            throw new UnsureException(e.getMessage());
        }
    }

    public void logout(String authToken) throws UnauthorizedException, UnsureException {
        try{
            if(this.checkAuthorization(authToken)){
                authDAO.removeAuth(authToken);
            }
            else{
                throw new UnauthorizedException("{ \"message\": \"Error: unauthorized\" }");
            }
        }catch(DataAccessException e) {
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

            userDAO.clear();
            authDAO.clearAuth();

    }

    private AuthData createAddAuth(String username) throws DataAccessException{
        try {
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(username, authToken);
            authDAO.addAuth(authData);
            return authData;
        }catch(DataAccessException e) {
            throw e;
        }
    }
}
