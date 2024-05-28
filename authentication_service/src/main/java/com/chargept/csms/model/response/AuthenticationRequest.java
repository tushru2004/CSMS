package main.java.com.chargept.csms.model.response;

import java.util.UUID;

public class AuthenticationRequest {
    private UUID stationUuid;

    public DriverIdentifier getDriverIdentifier() {
        return driverIdentifier;
    }

    public void setDriverIdentifier(DriverIdentifier driverIdentifier) {
        this.driverIdentifier = driverIdentifier;
    }

    public UUID getStationUuid() {
        return stationUuid;
    }

    public void setStationUuid(UUID stationUuid) {
        this.stationUuid = stationUuid;
    }

    private DriverIdentifier driverIdentifier;
}

