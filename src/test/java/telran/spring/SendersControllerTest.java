package telran.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.controller.SenderController;
import telran.spring.model.Message;
import telran.spring.security.jwt.JwtFilter;
import telran.spring.service.Sender;

@Service
class MockSender implements Sender {

	@Override
	public String send(Message message) {

		return "test";
	}

	@Override
	public String getMessageTypeString() {

		return "test";
	}

	@Override
	public Class<? extends Message> getMessageTypeObject() {

		return Message.class;
	}
}

@WithMockUser(roles = { "USER", "ADMIN" }, username = "admin")

@WebMvcTest(controllers = { SenderController.class, MockSender.class, SecurityConfiguration.class }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class) })
class SendersControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	Message message;
	String sendUrl = "http://localhost:8080/sender";
	String getTypesUrl = sendUrl;
	String isTypePathUrl = String.format("%s/type", sendUrl);

	@BeforeEach
	void setUp() {
		message = new Message();
		message.text = "test";
		message.type = "test";
	}

	@Test
	void mockMvcExists() {
		assertNotNull(mockMvc);
	}

	@Test
	void sendRightFlow() throws Exception {
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals("test", response);
	}

	@Test
	@WithMockUser(roles = { "USER" }, username = "admin")
	void sendFlow403() throws Exception {
		String messageJson = mapper.writeValueAsString(message);
		getRequestBase(messageJson).andExpect(status().isForbidden());

	}

	private ResultActions getRequestBase(String messageJson) throws Exception {
		return mockMvc.perform(post(sendUrl).contentType(MediaType.APPLICATION_JSON).content(messageJson))
				.andDo(print());
	}

	@Test
	void sendNotFoundFlow() throws Exception {
		message.type = "abc";
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isNotFound()).andReturn().getResponse()
				.getContentAsString();
		assertEquals("abc type not found", response);
	}

	@Test
	void sendVaildationViolationFlow() throws Exception {
		message.type = "123";
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isBadRequest()).andReturn().getResponse()
				.getContentAsString();
		assertTrue(response.contains("mismatches"));
	}

	@Test
	void getTypesTest() throws Exception {
		String responseJson = mockMvc.perform(get(getTypesUrl)).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		String[] typesResponse = mapper.readValue(responseJson, String[].class);
		assertArrayEquals(new String[] { "test" }, typesResponse);
	}

	@Test
	void isTypePathExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "/test")).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertTrue(booleanResponse);
	}

	@Test
	void isTypePathNotExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "/test1")).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertFalse(booleanResponse);
	}

	@Test
	void isTypePathParamExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "?type=test")).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertTrue(booleanResponse);
	}

	@Test
	void isTypePathParamNotExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "?type=test1")).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertFalse(booleanResponse);
	}

	@Test
	void isTypePathParamMissing() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl)).andDo(print()).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();

		assertEquals("isTypeExistsParam.type: must not be empty", responseJson);
	}

}