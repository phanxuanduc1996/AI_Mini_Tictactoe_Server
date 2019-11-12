package com.codelovers.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String EXPIRED_URL = "/session-expired";

    private static final String INVALID_URL = "/session-invalid";

    private static final String LOGIN_ERROR_URL = "/login-error";

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/matchBot", "/matchBot/**","/register","/","/match","/document","/match/**","/gs-guide-websocket/**","/js/**", "/css/**", "/login").permitAll().antMatchers("/**").authenticated();
        http.formLogin().loginPage("/login").failureUrl(LOGIN_ERROR_URL)
                .defaultSuccessUrl("/", true).usernameParameter("username").passwordParameter("password").permitAll()
                .and();

        http
                .sessionManagement()
                .maximumSessions(10)
                .expiredUrl(EXPIRED_URL)
                .maxSessionsPreventsLogin(true)
                .and()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl(INVALID_URL)
                .and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(ajaxAuthenticationEntryPoint(), ajaxRequestMatcher());

        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/clearStoring")
                .deleteCookies("JSESSIONID").invalidateHttpSession(true).permitAll();

        CustomAccessDeniedHandler h = new CustomAccessDeniedHandler(new SimpleRedirectInvalidSessionStrategy(INVALID_URL));
        http.exceptionHandling().accessDeniedHandler(h);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder(11));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public AuthenticationEntryPoint ajaxAuthenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        };
    }

    @Bean
    public RequestMatcher ajaxRequestMatcher() {
        return new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest");
    }

    /**
     * HTTP-403
     */
    static class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {
        InvalidSessionStrategy invalidSessionStrategy;

        public CustomAccessDeniedHandler(InvalidSessionStrategy strategy) {
            this.invalidSessionStrategy = strategy;
        }

        @Override
        public void handle(HttpServletRequest request,
                           HttpServletResponse response,
                           AccessDeniedException accessDeniedException)
                throws IOException, ServletException {

            if (accessDeniedException instanceof MissingCsrfTokenException) {
                invalidSessionStrategy.onInvalidSessionDetected(request, response);
            } else {
                super.handle(request, response, accessDeniedException);
            }
        }
    }

}
