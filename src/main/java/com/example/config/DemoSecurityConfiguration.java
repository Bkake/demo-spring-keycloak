package com.example.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DemoSecurityConfiguration {

    /**
     * See https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#jc-custom-dsls
     * <ul><li>Manage paths securisation here !You must use this configuration in tests to validate routes securisation</li>
     * <li>Use with   .and().apply(new CommonVitodocSecuritAdapter()) on http dsl</li>
     * </ul>
     */
    public static class CommonSpringKeyCloakConfigurationAdapter extends
            AbstractHttpConfigurer<CommonSpringKeyCloakConfigurationAdapter, HttpSecurity> {

        @Override
        public void init(HttpSecurity http) throws Exception {
            // toute méthode qui ajoute un autre configuration
            // doit être fait dans la méthode init
            http
                    // désactiver csrf à cause du mode API
                    .csrf().disable()

                    // configuration de la chaine de sécurité de sesssion
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                    .and()
                    // getstion de la sécurité des routes
                    .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                    .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers("/logout", "/", "/unsecured").permitAll()
                    .antMatchers("/user").hasRole("USER")
                    .antMatchers("/admin").hasRole("ADMIN");
        }

    }

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(
            prePostEnabled = true,
            securedEnabled = true,
            jsr250Enabled = true)
    @ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true", matchIfMissing = true)
    @ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
    public static class KeycloakConfigurationAdapter extends KeycloakWebSecurityConfigurerAdapter {

        /**
         * Définition de la stratégie d’authentification de session.
         * @return
         */
        @Bean
        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
          return new NullAuthenticatedSessionStrategy();
        }

        /**
         * Définition de la stratégie de nommage des rôles
         *
         * Enregistre le KeycloakAuthenticationProvider avec
         * le gestionnaire d'authentification.
         *
         * @param auth
         * @throws Exception
         */
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
            // simple mappeur d'autorité pour éviter ROLE_
            keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
            auth.authenticationProvider(keycloakAuthenticationProvider);
        }

        /**
         * Définition du resolver Spring Boot
         * Nécéssaire pour gérer les configurations de démarrage de spring boot
         * @return
         */
        @Bean
        public KeycloakConfigResolver KeycloakConfigResolver() {
            return new KeycloakSpringBootConfigResolver();
        }

        /**
         * Configuration spécifique à keycloak(ajouts de filtres, etc)
         * @param http
         * @throws Exception
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // configuration de la chaine de sécurité pour qu’elle utilise notre politique de sesssion, déclarée précedemment
                    .sessionManagement()
                        .sessionAuthenticationStrategy(sessionAuthenticationStrategy())

                    // utilisation des filtres de sécurités fournit par Keycloak (qui permettent de valider les tokens à chaque appel, etc)
                    .and()
                        .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                        .addFilterBefore(keycloakAuthenticationProcessingFilter(), X509AuthenticationFilter.class)
                        .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())

                    // delégation de l'enpoint logout à spring security
                    // Redéfinition de l'endpoint de déconnexion et la réponse de ce dernier en cas de succès
                    .and()
                        .logout()
                        .addLogoutHandler(keycloakLogoutHandler())
                        .logoutUrl("/logout").logoutSuccessHandler(
                        // gestionnaire de déconnexion pour API
                        (HttpServletRequest request, HttpServletResponse response, Authentication authentication) ->
                                response.setStatus(HttpServletResponse.SC_OK)
                        )

                    // utilisation de la configuration communes
                    // definie plus haut
                    .and().apply(new CommonSpringKeyCloakConfigurationAdapter());
        }
    }
}
