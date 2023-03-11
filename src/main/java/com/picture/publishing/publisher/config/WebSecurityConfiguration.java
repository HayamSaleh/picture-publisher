package com.picture.publishing.publisher.config;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.picture.publishing.publisher.model.UserRole;
import com.picture.publishing.publisher.service.impl.UserAuthnticationService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	private UserAuthnticationService userAuthnticationService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private final Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userAuthnticationService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		String loginUrl = "/login";
		String logoutURL = "/logout";

		http.authorizeRequests().antMatchers("/").permitAll().antMatchers(loginUrl).permitAll()
				.antMatchers("/register", "/pictures/accepted", "swagger-ui/**").permitAll()//
				.antMatchers("/admin/**").hasAuthority(UserRole.ADMIN_ROLE.name()).anyRequest().authenticated()//
				.and().csrf().disable()//
				.formLogin().failureHandler((request, response, exception) -> {
					logger.error("{}:{}", exception, "Login Failed!.");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					exception.printStackTrace();
					response.getWriter().write("Bad Credentials!");
				})//
				.successHandler((request, response, authentication) -> {
					logger.info("{}:{}", authentication, "Logged Successful.");
					response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
					response.getWriter().write("Logged In!");
				})//
				.and().logout().logoutUrl(logoutURL).logoutSuccessUrl(loginUrl).and().exceptionHandling();

		http.headers().frameOptions().disable();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/v2/api-docs",
				"/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui/**",
				"/webjars/**");
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").addResourceLocations("file:images\\");
	}

}
