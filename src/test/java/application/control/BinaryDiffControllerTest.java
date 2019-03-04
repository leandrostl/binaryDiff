package application.control;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.Messages;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BinaryDiffControllerTest {

	private static final String RETURN_SUCESSO = Messages.getString("sucesso");
	private static final String URL_DIFF = "/v1/diff";
	private static final String URL_RIGHT = "/v1/diff/1/right";
	private static final String URL_LEFT = "/v1/diff/1/left";

	@Autowired
	private MockMvc mvc;

	@Test
	public void testSetLeftFile() throws Exception {
		testSetFile(URL_LEFT, "data", RETURN_SUCESSO);
	}

	@Test
	public void testSetRightFile() throws Exception {
		testSetFile(URL_RIGHT, "data", RETURN_SUCESSO);
	}

	@Test
	public void testGetDiff_equals() throws Exception {
		final MockHttpSession session = new MockHttpSession();
		final String data = "test test test";
		testSetFile(URL_LEFT, data, RETURN_SUCESSO, session);
		testSetFile(URL_RIGHT, data, RETURN_SUCESSO, session);

		mvc.perform(get(URL_DIFF).session(session)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$." + Messages.getString("result"), equalTo(Messages.getString("diff_equals"))));
	}

	@Test
	public void testGetDiff_size() throws Exception {
		final MockHttpSession session = new MockHttpSession();
		testSetFile(URL_LEFT, "test test test", RETURN_SUCESSO, session);
		testSetFile(URL_RIGHT, "test test", RETURN_SUCESSO, session);

		mvc.perform(get(URL_DIFF).session(session)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$." + Messages.getString("result"), equalTo(Messages.getString("diff_size"))));
	}

	@Test
	public void testGetDiff_diff() throws Exception {
		final MockHttpSession session = new MockHttpSession();
		testSetFile(URL_LEFT, "teste texsa teste teste", RETURN_SUCESSO, session);
		testSetFile(URL_RIGHT, "teste teste teste quest", RETURN_SUCESSO, session);

		mvc.perform(get(URL_DIFF).session(session)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$." + Messages.getString("result"),
						equalTo(Messages.getString("diff", 9, 3) + ", " + Messages.getString("diff", 19, 5))));
	}

	@Test
	public void testGetDiff_null() throws Exception {
		final MockHttpSession session = new MockHttpSession();
		assertThat(getDiffErrorMessage(session), equalTo(Messages.getString("error_both")));
	}

	@Test
	public void testGetDiff_leftNull() throws Exception {
		final MockHttpSession session = new MockHttpSession();
		testSetFile(URL_RIGHT, "teste", RETURN_SUCESSO, session);
		assertThat(getDiffErrorMessage(session), equalTo(Messages.getString("error_left")));
	}

	@Test
	public void testGetDiff_rightNull() throws Exception {
		final MockHttpSession session = new MockHttpSession();
		testSetFile(URL_LEFT, "teste", RETURN_SUCESSO, session);
		assertThat(getDiffErrorMessage(session), equalTo(Messages.getString("error_right")));
	}

	private void testSetFile(final String url, final String data, final String expected) throws Exception {
		testSetFile(url, data, expected, new MockHttpSession());
	}

	private void testSetFile(final String url, final String data, final String expected, final MockHttpSession session)
			throws Exception {
		mvc.perform(
				post(url).session(session).contentType(MediaType.APPLICATION_JSON).content(asJsonBase64String(data)))
				.andExpect(status().isOk()).andExpect(jsonPath("$." + Messages.getString("result"), equalTo(expected)));
	}

	private String getDiffErrorMessage(final MockHttpSession session) throws Exception {
		return mvc.perform(get(URL_DIFF).session(session)).andDo(print()).andExpect(status().isBadRequest()).andReturn()
				.getResolvedException().getMessage();
	}

	private static String asJsonBase64String(final Object obj) throws JsonProcessingException {
		final Map<String, String> map = new HashMap<>();
		map.put(Messages.getString("data"),
				Base64.getEncoder().encodeToString(new ObjectMapper().writeValueAsString(obj).getBytes()));
		return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
	}

}
