package com.example.brunch;

import com.example.brunch.security.SecurityConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({
        SecurityConfig.class,
        AppProperties.class
})
public class AppConfiguration {

}
