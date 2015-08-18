package net.wehrens.geofence;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeoFence {

    Map<String, String> userLocations = new ConcurrentHashMap<>();

    public void usersEntersLocation(String user, String location) {
        userLocations.put(user, location);
    }

    public void userLeavesLocation(String user, String location) {
        if (userLocations.containsKey(user)) {
            if (userLocations.get(user).equals(location)) {
                userLocations.remove(user);
            }
        }
    }

    public List<String> getUsersAtLocation(String location) {
        List<String> usersAtLocation = new ArrayList<>();

        userLocations.keySet().forEach(user -> {
            if (userLocations.get(user).equals(location)) {
                usersAtLocation.add(user);
            }
        });
        return usersAtLocation;
    }

    public boolean isUserAtLocation(String user, String location) {
        return (userLocations.containsKey(user) && userLocations.get(user).equals(location));
    }

}
