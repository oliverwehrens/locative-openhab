package net.wehrens.geofence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Config {

    private final Logger log = LoggerFactory.getLogger(Config.class);

    @Value("${openhabserver}")
    public String openHabServerUrl = "http://myopenhabservice.local:8080";

    @Value("${switchprefix}")
    public String switchPrefix = "Presence";

    @Value("${geofenceuserfile}")
    public String geoFenceUserFile = "/etc/geofenceuserfile.pw";


    @PostConstruct
    public void outputConfig() {
        log.info("Starting with config: "+toString());
    }

    @Override
    public String toString() {
        return "Config{" +
                "openHabServerUrl='" + openHabServerUrl + '\'' +
                ", switchPrefix='" + switchPrefix + '\'' +
                ", geoFenceUserFile='" + geoFenceUserFile + '\'' +
                '}';
    }
}
