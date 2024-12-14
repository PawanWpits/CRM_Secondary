package com.wpits.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.wpits.security.JwtAuthenticationEntryPoint;
import com.wpits.security.JwtAuthenticationFilter;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    private final String[] PUBLIC_URLS={
            "/swagger-ui/**",
            "/webjars/**",
			"/swagger-resources/**",
			"/v3/api-docs",
			"/v2/api-docs"
									 
       };

//    @Bean
//    public UserDetailsService userDetailsService() {

//        UserDetails normal = User.builder()
//                .username("Ankit")
//                .password(passwordEncoder().encode("ankit"))
//                .roles("NORMAL")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("Durgesh")
//                .password(passwordEncoder().encode("durgesh"))
//                .roles("ADMIN")
//                .build();
    //users create
    //InMemoryUserDetailsManager- is implementation class of UserDetailService
//        return new InMemoryUserDetailsManager(normal, admin);
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


//
//        http.authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and().
//                formLogin()
//                .loginPage("login.html")
//                .loginProcessingUrl("/process-url")
//                .defaultSuccessUrl("/dashboard")
//                .failureUrl("/error")
//                .and()
//                .logout()
//                .logoutUrl("/do-logout");


        http.csrf()
                .disable()
				.cors()
				.disable()
                .authorizeRequests()
                .antMatchers("/api/login")
                .permitAll()
                .antMatchers(PUBLIC_URLS)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                
                

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

	/*
		// CORS Configuration
		@Bean
		public FilterRegistrationBean corsFilter() {
		
		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    CorsConfiguration configuration = new CorsConfiguration();
		    configuration.setAllowCredentials(true);
		    configuration.setAllowedOrigins(Arrays.asList("https://bssproxy01.neotel.nr/crm/","http://172.17.1.20:9091","http://127.0.0.1:5173"));
		    configuration.addAllowedOriginPattern("*"); //if allow all ip 
		    //configuration.setAllowCredentials(true);
		    configuration.addAllowedHeader("Authorization");
		    configuration.addAllowedHeader("Content-Type");
		    configuration.addAllowedHeader("Accept");
		    configuration.addAllowedMethod("GET");
		    configuration.addAllowedMethod("POST");
		    configuration.addAllowedMethod("DELETE");
		    configuration.addAllowedMethod("PUT");
		    configuration.addAllowedMethod("OPTIONS");
		    configuration.setMaxAge(3600L);
		    source.registerCorsConfiguration("/**", configuration);
		
		    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));
		    filterRegistrationBean.setOrder(-110);
		    return filterRegistrationBean;
		
		
		}*/
	 
	// CORS Configuration (for Security)
	/*@Bean
	public CorsFilter corsFilter() {
		
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowCredentials(true);
	    configuration.setAllowedOrigins(Arrays.asList("https://bssproxy01.neotel.nr/crm/", "http://172.17.1.20:9091", "http://127.0.0.1:5173","http://localhost:3000"));
	    configuration.addAllowedOriginPattern("*"); // all IPs
	    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
	    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
	    configuration.setMaxAge(3600L);
	    source.registerCorsConfiguration("/**", configuration);
	    return new CorsFilter(source);
	}
	
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilterRegistration(CorsFilter corsFilter) {
	    FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(corsFilter);
	    filterRegistrationBean.setOrder(-110);
	    return filterRegistrationBean;
	}*/
    
    
	/*@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}*/
	
}
