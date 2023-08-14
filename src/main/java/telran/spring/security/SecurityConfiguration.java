package telran.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import telran.spring.security.jwt.JwtFilter;

@Configuration

public class SecurityConfiguration {
	@Autowired
	JwtFilter jwtFilter;

	@Bean
	@Order(5)
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http.cors(custom -> custom.disable()).csrf(custom -> custom.disable())
				.authorizeHttpRequests(custom -> custom.requestMatchers("/login").permitAll()
						.requestMatchers(HttpMethod.GET).authenticated().anyRequest().hasRole("ADMIN"))
				.httpBasic(Customizer.withDefaults())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
}
