package net.wehrens.geofence;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.StrictAssertions.assertThat;


public class GeoFenceTest {

    private final static String USER = "oliver";
    private final static String LOCATION_HOME = "home";
    private final static String LOCATION_WORK = "work";

    private GeoFence geoFence;

    @Before
    public void setUp() throws Exception {
        geoFence = new GeoFence();
    }

    @Test
    public void testUsersEntersLocationAndIsThere() throws Exception {
        geoFence.usersEntersLocation(USER, LOCATION_HOME);
        assertThat(geoFence.userLocations.get(USER)).isEqualTo(LOCATION_HOME);
    }

    @Test
    public void testUsersEntersNewLocationAndIsThere() throws Exception {
        geoFence.usersEntersLocation(USER, LOCATION_WORK);
        geoFence.usersEntersLocation(USER, LOCATION_HOME);
        assertThat(geoFence.userLocations.get(USER)).isEqualTo(LOCATION_HOME);
    }

    @Test
    public void testUserLeavesLocation() throws Exception {
        geoFence.userLocations.put(USER, LOCATION_HOME);
        geoFence.userLeavesLocation(USER, LOCATION_HOME);
        assertThat(geoFence.userLocations.containsKey(USER)).isFalse();
    }

    @Test
    public void testUserLeavesWrongLocationAndIsStillThere() throws Exception {
        geoFence.userLocations.put(USER, LOCATION_HOME);
        geoFence.userLeavesLocation(USER, LOCATION_WORK);
        assertThat(geoFence.userLocations.get(USER)).isEqualTo(LOCATION_HOME);
    }

    @Test
    public void testIsUserAtLocation() throws Exception {
        geoFence.userLocations.put(USER, LOCATION_HOME);
        assertThat(geoFence.isUserAtLocation(USER, LOCATION_HOME));
    }

    @Test
    public void testGetUsersAtLocation() throws Exception {
        geoFence.userLocations.put(USER, LOCATION_HOME);
        assertThat(geoFence.getUsersAtLocation(LOCATION_HOME).contains(USER));
    }
}