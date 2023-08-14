package telran.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import telran.spring.security.AccountingConfiguration;
import telran.spring.security.SecurityConfiguration;
import telran.spring.security.jwt.JwtFilter;
import telran.spring.security.jwt.JwtUtil;

@WebMvcTest({ JwtFilter.class, JwtUtil.class, SecurityConfiguration.class, AccountingConfiguration.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtFilterTest {
	static String jwt;
	@Autowired
	MockMvc mockMvc;

	@Autowired
	JwtFilter jwtFilter;
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	UserDetailsService userDetailsService;

	@Test
	void test() {
		assertNotNull(mockMvc);
		assertNotNull(jwtFilter);
	}

	@Test
	void authenticationErrorTest() throws Exception {
		mockMvc.perform(get("http://localhost:8080/kuku")).andDo(print()).andExpect(status().isUnauthorized());
	}

	@Test
	@Order(1)
	void authenticationNormalTest() throws Exception {
		jwt = jwtUtil.createToken(userDetailsService.loadUserByUsername("admin"));
		mockMvc.perform(get("http://localhost:8080/kuku").header("Authorization", "Bearer " + jwt)).andDo(print())
				.andExpect(status().isNotFound());

	}

	@Test
	@Order(2)
	void authenticationExpiredTest() throws Exception {
		Thread.sleep(2500);
		mockMvc.perform(get("http://localhost:8080/kuku").header("Authorization", "Bearer " + jwt)).andDo(print())
				.andExpect(status().isUnauthorized());

	}

}
