package net.wehrens.geofence;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class GeoFenceController {

    @RequestMapping(value = "/location/{location}/in", method = RequestMethod.POST)
    public void locationIn(@PathVariable String location) {
        System.out.println("In Location: "+location);
        User userSession = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        System.out.println(userSession.getUsername());
    }

    @RequestMapping(value = "/location/{location}/out", method = RequestMethod.POST)
    public void locationOut(@PathVariable String location) {
        System.out.println("Out Location: "+location);
        User userSession = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        System.out.println(userSession.getUsername());
    }


}
