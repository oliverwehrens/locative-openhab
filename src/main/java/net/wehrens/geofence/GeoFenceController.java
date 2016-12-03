package net.wehrens.geofence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
public class GeoFenceController {

    private final static String ACTION_NAME = "trigger";
    private final static Logger log = LoggerFactory.getLogger(GeoFenceController.class);
    private RestTemplate restTemplate = new RestTemplate();
    private Config config;

    public GeoFenceController(Config config) {
        this.config = config;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity managePresence(@RequestParam Map<String, String> allRequestParams) {
        String location = allRequestParams.containsKey("id") ? allRequestParams.get("id") : "Unknown";
        String openHabSwitchName = config.switchPrefix + "_" + location.toLowerCase() + "_" + getUserName().toLowerCase();
        try {
            String openHabSwitchValue = getSwitchValue(allRequestParams);
            log.info("Trying to set location {} for user {} to {}.", location, getUserName(), openHabSwitchValue);
            log.info("Using Switch named {}.", openHabSwitchName);
            ResponseEntity<String> response = restTemplate.getForEntity(config.openHabServerUrl + "/CMD?{switch}={value}", String.class, openHabSwitchName, openHabSwitchValue);
            log.info("Got {} response code from openhab server at {}", response.getStatusCode().value(), config.openHabServerUrl);
            return new ResponseEntity(response.getStatusCode());

        } catch (IllegalArgumentException e) {
            log.error("Error: User {}: {} for location {}.", getUserName(), e.getMessage(), location);
            return new ResponseEntity(BAD_REQUEST);
        }
    }

    private String getSwitchValue(Map<String, String> allRequestParams) throws IllegalArgumentException {
        String result = "OFF";
        if (allRequestParams.containsKey(ACTION_NAME)) {
            switch (allRequestParams.get(ACTION_NAME)) {
                case "enter":
                    result = "ON";
                    break;
                case "exit":
                    result = "OFF";
                    break;
                default:
                    throw new IllegalArgumentException("Value for action " + allRequestParams.get(ACTION_NAME) + " is invalid.");
            }
        }
        return result;
    }

    private String getUserName() {
        return ((User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
    }
}
