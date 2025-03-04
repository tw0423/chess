package dataaccess;
import java.util.ArrayList;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    ArrayList<AuthData> authDataList;
    public MemoryAuthDAO() {
        this.authDataList = new ArrayList<>();
    }

    @Override
    public void addAuth(AuthData authData){
        this.authDataList.add(authData);
    }

    @Override
    public void removeAuth(String authToken) {
        for (AuthData authData : this.authDataList) {
            if(authData.authToken().equals(authToken)){
                this.authDataList.remove(authData);
                break;
            }
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws EmptyDataException, AuthTokenNotFound{
        if (this.authDataList.isEmpty()) {
            throw new EmptyDataException("AuthData is empty");
        }
        for (AuthData authData : this.authDataList) {
            if(authData.authToken().equals(authToken)){
                return authData;
            }
        }
        throw new AuthTokenNotFound("AuthToken is not valid");
        //going to throw an exception.
    }

    @Override
    public void clearAuth(){
        this.authDataList.clear();
    }


}

