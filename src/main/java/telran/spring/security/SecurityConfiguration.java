package telran.spring.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SecurityConfiguration {
	
	@Value("${app.security.user.name:user}")
	String usernameForUser;
	@Value("${app.security.admin.name:admin}")
	String usernameForAdmin;
	
	@Value("${app.security.user.password}")
	String passwordForUser;
	@Value("${app.security.admin.password}")
	String passwordForAdmin;
	
	@Bean
	SecurityFilterChain configure(HttpSecurity httpSec) throws Exception {
		return httpSec.csrf(custom -> custom.disable()).cors(custom -> custom.disable())
				.authorizeHttpRequests(custom -> custom.requestMatchers(HttpMethod.GET).authenticated()
						.anyRequest().hasRole("ADMIN"))
				.sessionManagement(custom -> custom.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
				.httpBasic(Customizer.withDefaults()).build();
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService getUserDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager() {
			@Override
			public UserDetails loadUserByUsername(String username) {
				log.debug("User with username {} is trying access the application", username);
				return super.loadUserByUsername(username);
			}
		};
		manager.createUser(User.withUsername(usernameForUser).password(passwordForUser)
				.passwordEncoder(s -> getPasswordEncoder().encode(s)).roles("USER").build());
		manager.createUser(User.withUsername(usernameForAdmin).password(passwordForAdmin)
				.passwordEncoder(s -> getPasswordEncoder().encode(s)).roles("USER", "ADMIN").build());
		return manager;
	}
}
