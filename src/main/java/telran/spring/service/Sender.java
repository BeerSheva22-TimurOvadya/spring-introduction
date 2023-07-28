package telran.spring.service;

import telran.spring.model.Message;

public interface Sender {
String send(Message message);
String getMessageTypeString();
Class<? extends Message> getMessageTypeObject();
}