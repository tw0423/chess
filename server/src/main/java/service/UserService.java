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
