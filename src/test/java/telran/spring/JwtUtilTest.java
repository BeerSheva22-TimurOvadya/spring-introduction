package telran.spring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtUtilTest {
	@Autowired
	static String jwt;
	static final String USER_NAME = "user";

	@Test
	@Order(1)
	void test() {
		fail("Not yet implemented");
	}

}
