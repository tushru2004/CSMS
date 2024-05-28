package com.chargept.csms.model.request;

import java.util.UUID;

public class AuthenticationRequest {
    private UUID stationUuid;
    private DriverIdentifier driverIdentifier;

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

}

