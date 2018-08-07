package com.example.config;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

public class DemoSecurityConfiguration {

    @KeycloakConfiguration
    @ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true", matchIfMissing = true)
    public static class KeycloakConfigurationAdapter extends KeycloakWebSecurityConfigurerAdapter {

        /**
         *  Définition de la stratégie d'authentification de session.
         *
          */
        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
          return new NullAuthenticatedSessionStrategy();
        }
    }
}
