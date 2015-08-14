package net.wehrens.geofence;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GeoFenceController {

    Map<String, String> userLocations = new HashMap<>();

    @RequestMapping(value = "/location/{location}/in", method = RequestMethod.POST)
    public void locationIn(@PathVariable String location) {
        userLocations.put(getUserName(), location);
    }

    private String getUserName() {
        return ((User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
    }

    @RequestMapping(value = "/location/{location}/out", method = RequestMethod.POST)
    public void locationOut(@PathVariable String location) {
        if (userLocations.containsKey(getUserName())) {
            userLocations.remove(getUserName());
        }
    }


}
