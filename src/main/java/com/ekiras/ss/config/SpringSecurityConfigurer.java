package com.ekiras.ss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

/**
 * @author ekansh
 * @since 30/3/16
 */
@EnableWebSecurity
public class SpringSecurityConfigurer extends WebSecurityConfigurerAdapter{

    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ADMIN > USER");
        return roleHierarchy;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("ekansh")
                .password("password")
                .authorities("USER", "ROLE");
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("admin")
                .authorities("ADMIN");
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .expressionHandler(webExpressionHandler())
                .antMatchers("/admin**").hasAuthority("ADMIN")
                .antMatchers("/user**").hasAuthority("USER")
                .antMatchers("/css/**","/js/**","/dist/**","/bower_components/**","/less/**","/fonts/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/auth/login")
                .permitAll()
            .and()
            .logout()
        ;
    }

}
