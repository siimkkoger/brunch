package com.example.brunch.security;

import com.example.brunch.BrunchUserDetailsService;
import com.example.brunch.security.filters.BrunchBasicAuthenticationFilter;
import com.example.brunch.security.filters.JwtAuthenticationFilter;
import com.example.brunch.security.roles.BrunchRole;
import com.example.brunch.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@Import({
        JwtUtils.class,
        BrunchUserDetailsService.class
})
public class SecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtUtils jwtUtils;
    private final BrunchUserDetailsService brunchUserDetailsService;

    @Autowired
    public SecurityConfig(JwtUtils jwtUtils, BrunchUserDetailsService brunchUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.brunchUserDetailsService = brunchUserDetailsService;
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(brunchUserDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(daoAuthenticationProvider());
    }

    @Bean
    public BasicAuthenticationFilter brunchBasicAuthenticationFilter() {
        return new BrunchBasicAuthenticationFilter(authenticationManagerBean(), jwtUtils);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, brunchUserDetailsService);
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .and()
                .authorizeRequests()
                .mvcMatchers("/admin/**").hasAuthority(BrunchRole.ROLE_ADMIN)
                .mvcMatchers("/api/**", "/login", "/logout").authenticated()
                .mvcMatchers("/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilter(brunchBasicAuthenticationFilter());
        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/my/index")
                        .logoutSuccessHandler(customLogoutSuccessHandler())
                        .invalidateHttpSession(false)
                        .addLogoutHandler(customLogoutHandler())
                        .deleteCookies()
                );
        return http.build();
    }

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                LOGGER.debug("Logged out YAY!!!!!!");
            }
        };
    }

    @Bean
    public LogoutHandler customLogoutHandler() {
        return new LogoutHandler() {
            @Override
            public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                LOGGER.debug("In logouthandler whatever it is....");
            }
        };
    }

}
