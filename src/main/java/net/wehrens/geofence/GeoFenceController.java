package net.wehrens.geofence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GeoFenceController {

    @Autowired
    private GeoFence geoFence;

    // {device=EAE2AECC-05CC-4BD5-BD56-4031D8E436AA, id=0291F76A-DD0D-46E8-873A-49353DD29863, latitude=52.59502, longitude=13.42826, timestamp=1466365675.084566, trigger=test}


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public void addUserToLocation(@RequestParam Map<String, String> allRequestParams) {
        System.out.println(getUserName());
        System.out.println(allRequestParams);
    }

    private String getUserName() {
        return ((User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
    }

//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @RequestMapping(value = "/locations/{location}", method = RequestMethod.DELETE)
//    public void removeUserFromLocation(@PathVariable String location) {
//        geoFence.userLeavesLocation(getUserName(), location);
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @RequestMapping(value = "/locations/{location}/leave", method = RequestMethod.POST)
//    public void removeUserFromLocationForGeofencyBecauseItHasJustPOSTAndGET(@PathVariable String location) {
//        geoFence.userLeavesLocation(getUserName(),location);
//    }
//
//    @RequestMapping(value = "/locations/{location}/{user}", method = RequestMethod.GET, produces = "application/json")
//    public void location(@PathVariable String location, @PathVariable String user, HttpServletResponse response) {
//        if (geoFence.isUserAtLocation(user, location)) {
//            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//        } else {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//        }
//    }
//
//    @RequestMapping(value = "/locations", produces = "application/json")
//    @ResponseBody
//    public Map getLocations() {
//        return geoFence.userLocations;
//    }
//
//    @RequestMapping(value = "/locations/{location}", produces = "application/json")
//    @ResponseBody
//    public List<String> getUserAtLocations(@PathVariable String location) {
//        return geoFence.getUsersAtLocation(location);
//    }

}
