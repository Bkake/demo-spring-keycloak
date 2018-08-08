package com.example.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 *  Utilisez cette classe de configuration pour tester
 *  si votre chemin est sécurisé
 *  Elle se base sur la classe
 *  {@link DemoSecurityConfiguration.CommonSpringKeyCloakConfigurationAdapter}
 *  qui définit tous les matchers de sécurité
 */
@TestConfiguration
@EnableWebSecurity
public class DemoSecurityTestConfiguration extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // utilise common configuration pour valider les matchers
        http.apply(new DemoSecurityConfiguration.CommonSpringKeyCloakConfigurationAdapter());

    }
}
