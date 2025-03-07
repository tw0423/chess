package service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import model.*;
import dataaccess.*;

import java.util.ArrayList;


public class UserServiceTest {
     AuthDAO authDAO = new MemoryAuthDAO();
     UserDAO userDAO = new MemoryUserDAO();
     UserService userService = new UserService(authDAO, userDAO);

    @Test
    void successfulRegisterTest() {
        RegisterRequest request  = new RegisterRequest("volunteer1","abcde","volunteer1@gmail.com");
        try {
            RegisterResponse response = userService.register(request);
            Assertions.assertNotNull(response);
            ArrayList<UserData> users = userDAO.getUsers();
            Assertions.assertEquals("volunteer1", response.username());
            Assertions.assertEquals(1, users.size());
            Assertions.assertTrue(users.contains(new UserData("volunteer1","abcde","volunteer1@gmail.com")));
        }catch (Exception e) {
            assert false;
        }
    }

    @Test
    void failedRegisterTest() {
        RegisterRequest request  = new RegisterRequest("volunteer1","abcde","volunteer1@gmail.com");
        RegisterRequest request2  = new RegisterRequest("volunteer1","fgh","volunteer1@gmail.com");
        try {
            RegisterResponse response = userService.register(request);
            RegisterResponse response2 = userService.register(request2);

        }catch (BadRequestException | UnsureException e) {
            assert false;
        } catch (AlreadyTakenException e) {
            assert true;
        }
    }

    @Test
    void successfulLoginTest() {
        RegisterRequest request  = new RegisterRequest("volunteer1","abcde","volunteer1@gmail.com");
        try {
            RegisterResponse response = userService.register(request);
            Assertions.assertNotNull(response);


            LoginResponse loginResponse = userService.login(new LoginRequest("volunteer1","abcde"));
            Assertions.assertNotNull(loginResponse);
            AuthData returnedAuthData = authDAO.getAuth(loginResponse.authToken());
            assert returnedAuthData != null;

        }catch (Exception e) {
            assert false;
        }
    }

    @Test
    void unauthorizedLoginTest() {
        RegisterRequest request  = new RegisterRequest("volunteer1","abcde","volunteer1@gmail.com");
        LoginRequest request2  = new LoginRequest(null,"abcde");
        LoginRequest request3  = new LoginRequest("volunteer1","wrongpassWord");
        try {
            RegisterResponse response = userService.register(request);
            Assertions.assertNotNull(response);

            LoginResponse loginResponse = userService.login(request2);
            LoginResponse loginResponse2 = userService.login(request3);


        }catch (UnauthorizedException e) {
            assert true;
        }catch (Exception e) {
            assert false;
        }
    }

    @Test
    void successfulLogoutTest() {
        RegisterRequest request  = new RegisterRequest("volunteer1","abcde","volunteer1@gmail.com");
        try {
            RegisterResponse response = userService.register(request);
            Assertions.assertNotNull(response);
            String authToken = response.authToken();

            userService.logout(authToken);
            assert true;
        }catch (Exception e) {
            assert false;
        }
    }

    @Test
    void unauthorizedLogoutTest() {
        RegisterRequest request  = new RegisterRequest("volunteer1","abcde","volunteer1@gmail.com");
        try {
            RegisterResponse response = userService.register(request);
            Assertions.assertNotNull(response);
            String authToken = "Doesn't matter";

            userService.logout(authToken);

        }catch (UnauthorizedException e) {
            assert true;
        }catch (Exception e) {
            assert false;
        }
    }




}
