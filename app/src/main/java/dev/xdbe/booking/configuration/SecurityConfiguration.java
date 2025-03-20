package dev.xdbe.booking.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
		.requestMatchers("/").permitAll()
                .requestMatchers("/dashboard").hasRole("admin")
                .anyRequest().permitAll()
            )
	    .formLogin(withDefaults())
            .csrf((csrf) -> csrf
                .ignoringRequestMatchers("/h2-console/*")
            )
            .headers(headers -> headers.frameOptions().disable())
            .build();
    }

    @Bean
    public UserDetailsService users() {
        return new InMemoryUserDetailsManager(
            User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$hMgwa/AxGJ0MsESFP.JTCucxnhDyIooMJO/QogM.pBh4qDCf5or4u")
                .roles("admin")
                .build(),
            User.builder()
                .username("user")
                .password("{bcrypt}$2a$10$m6.b7nQtV8c4I2XIHpHd2uEoMBGqmDhIv/eHYKmOaPtXZLyYds9Xi")
                .roles("user")
                .build()
        );
    }
}
