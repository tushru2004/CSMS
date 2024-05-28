package com.chargept.csms.util;

import java.util.HashMap;
import java.util.Map;


public class Whitelist {
    //This whitelist represents a database that holds the driver identifiers.
    // The key of the map is the driver identifier and the value is boolean for whether the card is successful or not
    private final Map<String, Boolean> whitelist;

    public Whitelist() {
        this.whitelist = new HashMap<>();
        // False value represents card rejected
        this.whitelist.put("xY4B8zP0mN5qKdL2Hf9Z", false);
        // True value represents card accepted
        this.whitelist.put("V4Lk7C9QzN2tMh0GJwXcR3u1pBv5YsWnK8j3T2", true);
    }
    public boolean check(String id) {
        return this.whitelist.containsKey(id);
    }

    public boolean checkCardChargeStatus(String id) {
        return this.whitelist.get(id);
    }
}
