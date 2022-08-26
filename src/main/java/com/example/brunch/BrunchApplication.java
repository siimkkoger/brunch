package com.example.brunch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BrunchApplication {

    public static void main(String[] args) {
        final ApplicationContext applicationContext = SpringApplication.run(BrunchApplication.class, args);

        for (final String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
            if (!beanDefinitionName.startsWith("org.springframework") &&
                    !beanDefinitionName.startsWith("spring")) {
                System.out.println(beanDefinitionName);
            }
        }

    }

}

