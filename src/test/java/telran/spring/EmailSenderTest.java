package telran.spring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.spring.model.*;
import telran.spring.service.EmailSender;

@SpringBootTest
class EmailSenderTest {
	@Autowired
	EmailSender sender;

	@Test
	void EmailSenderRightFlow() {
		EmailMessage message = new EmailMessage();
		message.type = "email";
		message.text = "text";
		message.emailAddress = "test@gmail.com";
		String expected = String.format("email sender -  text: %s has been sent to mail %s", message.text,
				message.emailAddress);
		assertEquals(expected, sender.send(message));
	}

	@Test
	void EmailSenderWrongType() {
		TcpMessage message = new TcpMessage();
		message.type = "email";
		message.text = "text";
		message.setHostName("test@gmail.com");
		assertThrowsExactly(IllegalArgumentException.class, () -> sender.send(message));
	}

}
