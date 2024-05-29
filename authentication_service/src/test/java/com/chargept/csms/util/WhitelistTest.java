package test.java.com.chargept.csms.util;

import main.java.com.chargept.csms.util.Whitelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WhitelistTest {

    private Whitelist whitelist;

    @BeforeEach
    public void setUp() {
        whitelist = new Whitelist();
    }

    @Test
    public void testCheckExistingId() {
        assertTrue(whitelist.check("xY4B8zP0mN5qKdL2Hf9Z"));
        assertTrue(whitelist.check("V4Lk7C9QzN2tMh0GJwXcR3u1pBv5YsWnK8j3T2"));
    }

    @Test
    public void testCheckNonExistingId() {
        assertFalse(whitelist.check("ZLLk7C9QzN2tMh0GJwXcR3u1pBv5YsWfnK8j3RT"));
    }

    @Test
    public void testCheckCardChargeStatusAccepted() {
        assertTrue(whitelist.checkCardChargeStatus("V4Lk7C9QzN2tMh0GJwXcR3u1pBv5YsWnK8j3T2"));
    }

    @Test
    public void testCheckCardChargeStatusRejected() {
        assertFalse(whitelist.checkCardChargeStatus("xY4B8zP0mN5qKdL2Hf9Z"));
    }

    @Test
    public void testCheckCardChargeStatusNonExistingId() {
        assertFalse(whitelist.checkCardChargeStatus("bla"));
    }
}
