package ru.ifmo.shelf;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by nikge on 08.01.2017.
 */
public class Beans {
    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .username("")
                .password("")
                .url("")
                .driverClassName("")
                .build();
    }
}
