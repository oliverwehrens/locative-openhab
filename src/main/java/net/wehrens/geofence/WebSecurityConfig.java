package net.wehrens.geofence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
    private Config config;

    public WebSecurityConfig(Config config) {
        this.config = config;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**", "/").authenticated()
                .and()
                .csrf().disable()
                .httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        Stream<String> lines = Files.lines(Paths.get(config.geoFenceUserFile));
        lines.forEach(userNameWithPassword -> {
                    if (userNameWithPassword.contains(":")) {
                        String user = userNameWithPassword.substring(0, userNameWithPassword.indexOf(":"));
                        String pw = userNameWithPassword.substring(userNameWithPassword.indexOf(":") + 1, userNameWithPassword.length());
                        log.debug("Configure user '{}' with password '{}'", user, pw);
                        try {
                            auth
                                    .inMemoryAuthentication()
                                    .withUser(user).password(pw).roles("USER");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
