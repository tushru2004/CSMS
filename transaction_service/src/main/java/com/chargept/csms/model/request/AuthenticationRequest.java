package main.java.com.chargept.csms.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class AuthenticationRequest {
    private UUID stationUuid;
    private DriverIdentifier driverIdentifier;

}

