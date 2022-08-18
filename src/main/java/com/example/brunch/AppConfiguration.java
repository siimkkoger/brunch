package com.example.brunch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class AppConfiguration {

    private final DbProperties dbProperties;

    @Autowired
    public AppConfiguration(DbProperties dbProperties) {
        this.dbProperties = dbProperties;
    }

    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .username(dbProperties.getUsername())
                .password(dbProperties.getPassword())
                .url(dbProperties.getUrl())
                .build();
    }

}
