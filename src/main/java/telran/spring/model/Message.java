package telran.spring.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;


@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(value = {
		@JsonSubTypes.Type(value = EmailMessage.class), 
		@JsonSubTypes.Type(value = SmsMessage.class) })
@Data
public class Message {
	public String type;
	public String text;
}
