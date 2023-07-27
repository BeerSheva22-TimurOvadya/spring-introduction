package telran.spring.conroller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.spring.model.EmailMessage;
import telran.spring.model.Message;
import telran.spring.model.SmsMessage;
import telran.spring.service.Sender;

@RestController
@RequestMapping("sender")
@RequiredArgsConstructor
@Slf4j
public class SenderController {
	final Map<String, Sender> senders;
	@PostMapping
	String send(@RequestBody @Valid Message message) {
		log.debug("Controller received message {}, message");
		Sender sender = senders.get(message.type);
		String res = "Wrong message type "+ message.type;
		if(sender != null) {
			res = sender.send(message);
		} else {
			log.error(res);
		}
		return res;		
	}
	@PostConstruct
	void init() {		
		log.info("registred senders: {}", senders.keySet());
	}
	@PreDestroy
	void shutdown() {
		log.info("context closed");
	}
	
}
