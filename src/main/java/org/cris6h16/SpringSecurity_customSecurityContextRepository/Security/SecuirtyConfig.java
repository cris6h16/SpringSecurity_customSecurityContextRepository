package org.cris6h16.SpringSecurity_customSecurityContextRepository.Security;

import org.cris6h16.SpringSecurity_customSecurityContextRepository.Security.SecurityContextRepository.FileSecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;

@EnableWebSecurity
@Configuration
public class SecuirtyConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        SecurityContextRepository repo = new HttpSessionSecurityContextRepository();
        SecurityContextRepository repo = new FileSecurityContextRepository();
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf((csrf) -> csrf
                        .disable())

                // ..................................
                .securityContext((context) -> context
                        .securityContextRepository(repo)
                );
        return http.build();
    }
    /*
    sets the `SecurityContextRepository` on the
        `SecurityContextHolderFilter`and participating authentication filters,
         like `UsernamePasswordAuthenticationFilter`

    take in mind that this isn't a stateless approach..
     */


    @Bean
    UserDetailsService user() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("cris6h16")
                .password("cris6h16")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
    @Bean(name = "myAuthenticationManager")
    AuthenticationManager authenticationManager(
            UserDetailsService user,
            PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(user);
        provider.setPasswordEncoder(encoder);

        ProviderManager manager = new ProviderManager(provider);


        return manager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
