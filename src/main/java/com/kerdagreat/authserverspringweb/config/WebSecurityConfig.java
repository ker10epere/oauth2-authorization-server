package com.kerdagreat.authserverspringweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
public class WebSecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(httpRequest ->
                        httpRequest
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/**")).permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var u1 = User.withUsername("user")
                .password("123456")
                .authorities("read")
                .build();
        return new InMemoryUserDetailsManager(u1);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Using this for development testing allows
        // password in UserDetailsService without {noop} decoder id prefix
        return NoOpPasswordEncoder.getInstance();
    }
}
