package com.icici.athena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.icici.athena.CustomAuthenticationProvider;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebMVCSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().ignoringAntMatchers("/css/**").and().csrf()
				.ignoringAntMatchers("/js/**").and().csrf().ignoringAntMatchers("/images/**").and().csrf()
				.ignoringAntMatchers("/assets/**").and().csrf().ignoringAntMatchers("/data/**").and().csrf()
				.ignoringAntMatchers("/fonts/**").and().authorizeRequests().anyRequest().fullyAuthenticated().and()
				.formLogin().loginPage("/adminlogin").loginProcessingUrl("/LoginServlet")
				.failureUrl("/adminlogin?error").permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/LogoutServlet")).logoutSuccessUrl("/adminlogin")
				.permitAll();

		if (securityProperties.isEnableCsrf()) {
			http.addFilterAfter(new WebSecurityConfig(), CsrfFilter.class);
		} else {
			http.csrf().disable();
		}
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**").and().ignoring().antMatchers("/js/**").and().ignoring()
				.antMatchers("/assets/**").and().ignoring().antMatchers("/data/**").and().ignoring()
				.antMatchers("/images/**");
		//web.ignoring().antMatchers("/resources/**");// #3
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.ldapAuthentication();
		// auth.jdbcAuthentication();

		auth.authenticationProvider(authProvider);
		// SecurityContext securityContext = SecurityContextHolder.getContext();

	}

}
