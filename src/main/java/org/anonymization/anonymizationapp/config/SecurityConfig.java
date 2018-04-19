package org.anonymization.anonymizationapp.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()		
				.antMatchers("/*")
				.permitAll() 
                .anyRequest()
                .authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
	            .logout()                                    
	                .permitAll();	
	}

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
        authenticationMgr.inMemoryAuthentication().withUser("testUser").password("testPassword")
            .authorities("ROLE_USER").and().withUser("demoUser").password("demoPassword")
            .authorities("ROLE_USER").and().withUser("newUser").password("demoPassword")
            .authorities("ROLE_USER");
    }
}