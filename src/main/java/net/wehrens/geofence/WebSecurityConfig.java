package net.wehrens.geofence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${userfile:/tmp/x}")
    public String userFile;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/location/**/**").authenticated()
                .and()
                .csrf().disable()
                .httpBasic();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        Stream<String> lines = Files.lines(Paths.get(userFile));
        lines.forEach(s -> {
                    System.out.println(s);
                    String user = s.substring(0, s.indexOf(":"));
                    String pw = s.substring(s.indexOf(":") + 1, s.length());
                    try {
                        auth
                                .inMemoryAuthentication()
                                .withUser(user).password(pw).roles("USER");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );


    }
}
