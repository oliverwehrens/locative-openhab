package net.wehrens.geofence;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GeoFenceController {

    Map<String, String> userLocations = new HashMap<>();

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/locations/{location}", method = RequestMethod.POST)
    public void addUserToLocation(@PathVariable String location) {
        userLocations.put(getUserName(), location);
    }

    private String getUserName() {
        return ((User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/locations/{location}", method = RequestMethod.DELETE)
    public void removeUserFromLocation(@PathVariable String location,  HttpServletResponse response) {
       if (!removeUserFromLocationMap(location)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
       }
    }

    private boolean removeUserFromLocationMap(@PathVariable String location) {
        if (userLocations.containsKey(getUserName())) {
            if (userLocations.get(getUserName()).equals(location)) {
                userLocations.remove(getUserName());
                return true;
            }
        }
        return false;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/locations/{location}/leave", method = RequestMethod.POST)
    public void removeUserFromLocationForGeofancyBecauseItHasJustPOSTandGET(@PathVariable String location, HttpServletResponse response) {
        if (!removeUserFromLocationMap(location)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/locations/{location}/{user}", method = RequestMethod.GET, produces = "application/json")
    public void location(@PathVariable String location, @PathVariable String user, HttpServletResponse response) {
        if (userLocations.containsKey(user)) {
            if (userLocations.get(user).equals(location)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @RequestMapping(value = "/locations", produces = "application/json")
    public @ResponseBody Map getLocations() {
        return userLocations;
    }

    @RequestMapping(value = "/locations/{requestLocation}", produces = "application/json")
    public @ResponseBody List<String> getUserForLocations(@PathVariable String requestLocation) {
        List<String> usersAtLocation = new ArrayList<>();

        userLocations.keySet().forEach(user -> {
            if (userLocations.get(user).equals(requestLocation)) {
                usersAtLocation.add(user);
            }
        });
        return usersAtLocation;
    }

}
