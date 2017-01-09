package ru.ifmo.shelf.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Autowired
    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        Locale.setDefault(Locale.US);
        final String findUserQuery = "SELECT NAME, PASSWORD, 1 AS ENABLED FROM USERS WHERE NAME = ?";

        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(findUserQuery);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/static/**").permitAll()
                .and()
                .formLogin().loginPage("/").failureUrl("/")
                .usernameParameter("username").passwordParameter("password")
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .csrf().disable();
    }
}