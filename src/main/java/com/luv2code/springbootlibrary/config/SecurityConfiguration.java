package com.luv2code.springbootlibrary.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Disable Cross Site Request Forgery
        //http.csrf().disable();
        http.csrf(AbstractHttpConfigurer::disable);

        // Protect endpoints at /api/<type>/secure
        http.authorizeHttpRequests(configurer ->
                configurer.requestMatchers("/api/books/secure/**", "/api/reviews/secure/**")
                        .authenticated()
                        .requestMatchers("/api/books/**", "/api/reviews/**")
                        .permitAll())
                .oauth2ResourceServer((oath2) -> oath2.jwt(Customizer.withDefaults())
                );

        // add cors filter
        http.cors(Customizer.withDefaults());
        // add content negotation strategy
        http.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());

        // Force a non-empty response body to make unauthorized response body more friendly
        Okta.configureResourceServer401ResponseBody(http);

        return http.build();
    }
}
