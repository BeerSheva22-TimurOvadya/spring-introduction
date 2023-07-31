package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.EmailMessage;
import telran.spring.model.Message;
import telran.spring.model.SmsMessage;

@Service
@Slf4j
public class SmsSender implements Sender {

	@Override
	public String send(Message message) {
		log.debug("SMS service received message {}", message);
		String res = errorMessage;
		if (message instanceof SmsMessage) {
			SmsMessage smsMessage = (SmsMessage) message;
			res = String.format("SMS sender - text: %s has been sent to phone %s", smsMessage.text,
					smsMessage.phoneNumber);
		} else {			
			throw new IllegalArgumentException(res);
		}
		return res;
	}

	@Override
	public String getMessageTypeString() {

		return "sms";
	}

	@Override
	public Class<? extends Message> getMessageTypeObject() {

		return SmsMessage.class;
	}

}