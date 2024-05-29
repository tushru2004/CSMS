package main.java.com.chargept.csms.model.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AuthenticationRequest {
    @lombok.Setter
    private UUID stationUuid;
    private DriverIdentifier driverIdentifier;
}

