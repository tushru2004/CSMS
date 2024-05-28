package main.java.com.chargept.csms.model.response;

public class AuthenticationResponse {
    public String getAuthorizationStatus() {
        return authorizationStatus;
    }

    public void setAuthorizationStatus(String authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    private String authorizationStatus;
}
