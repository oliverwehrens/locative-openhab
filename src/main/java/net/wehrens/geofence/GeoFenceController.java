package net.wehrens.geofence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class GeoFenceController {

    @Autowired
    private GeoFence geoFence;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/locations/{location}", method = RequestMethod.POST)
    public void addUserToLocation(@PathVariable String location) {
        geoFence.usersEntersLocation(getUserName(), location);
    }

    private String getUserName() {
        return ((User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/locations/{location}", method = RequestMethod.DELETE)
    public void removeUserFromLocation(@PathVariable String location) {
        geoFence.userLeavesLocation(getUserName());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/locations/{location}/leave", method = RequestMethod.POST)
    public void removeUserFromLocationForGeofancyBecauseItHasJustPOSTAndGET(@PathVariable String location) {
        geoFence.userLeavesLocation(getUserName());
    }

    @RequestMapping(value = "/locations/{location}/{user}", method = RequestMethod.GET, produces = "application/json")
    public void location(@PathVariable String location, @PathVariable String user, HttpServletResponse response) {
        if (geoFence.isUserAtLocation(user, location)) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/locations", produces = "application/json")
    public
    @ResponseBody
    Map getLocations() {
        return geoFence.userLocations;
    }

    @RequestMapping(value = "/locations/{location}", produces = "application/json")
    public
    @ResponseBody
    List<String> getUserAtLocations(@PathVariable String location) {
        return geoFence.getUsersAtLocation(location);
    }

}
