package net.wehrens.geofence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
            String url = config.openHabServerUrl + "/rest/items/" + openHabSwitchName + "/state";
            log.info("Sending Request to '{}' with value '{}'.", url, openHabSwitchValue);
            if (!location.equals("test")) {
                ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(openHabSwitchValue, getHeaders()), Void.class);
                log.info("Got {} response code from openhab server at {}", response.getStatusCode().value(), config.openHabServerUrl);
                ResponseEntity<String> newValue = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
                log.info("New value for {} at Openhab is: {}.", openHabSwitchName, newValue.getBody());
                return new ResponseEntity(response.getStatusCode());
            } else {
                log.info("Location 'test' was detected. Not calling OpenHab. Returning status code 200.");
                return new ResponseEntity(HttpStatus.OK);
            }

        } catch (IllegalArgumentException | HttpClientErrorException e) {
            log.error("Error: User {}: {} for location {}.", getUserName(), e.getMessage(), location);
            return new ResponseEntity(BAD_REQUEST);
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.TEXT_PLAIN);
        ArrayList acceptableMediaTypes = new ArrayList();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(acceptableMediaTypes);
        return requestHeaders;
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
