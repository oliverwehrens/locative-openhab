package net.wehrens.geofence;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.StrictAssertions.assertThat;


public class GeoFenceTest {

    private GeoFence geoFence;

    @Before
    public void setUp() throws Exception {
        geoFence = new GeoFence();
    }

    @Test
    public void testUsersEntersLocationAndIsThere() throws Exception {
        geoFence.usersEntersLocation("oliver", "home");
        assertThat(geoFence.userLocations.get("oliver")).isEqualTo("home");
    }

    @Test
    public void testUsersEntersNewLocationAndIsThere() throws Exception {
        geoFence.usersEntersLocation("oliver", "work");
        geoFence.usersEntersLocation("oliver", "home");
        assertThat(geoFence.userLocations.get("oliver")).isEqualTo("home");
    }

    @Test
    public void testUserLeavesLocation() throws Exception {
        geoFence.userLocations.put("oliver", "home");
        geoFence.userLeavesLocation("oliver");
        assertThat(geoFence.userLocations.containsKey("oliver")).isFalse();
    }

    @Test
    public void testIsUserAtLocation() throws Exception {
        geoFence.userLocations.put("oliver", "home");
        assertThat(geoFence.isUserAtLocation("oliver", "home"));
    }

    @Test
    public void testGetUsersAtLocation() throws Exception {
        geoFence.userLocations.put("oliver", "home");
        assertThat(geoFence.getUsersAtLocation("home").contains("oliver"));
    }
}