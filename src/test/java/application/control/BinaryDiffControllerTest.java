package application.control;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BinaryDiffControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void testInicial() throws Exception {
		mvc.perform(get("/v1/diff/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Binary Diff funcionando!")));
	}

	@Test
	public void testSetLeftFile() throws Exception {
		mvc.perform(post("/v1/diff/1/left").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonBase64String("teste teste teste"))).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Sucesso")));
	}

	@Test
	public void testSetRightFile() throws Exception {
		mvc.perform(post("/v1/diff/1/right").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonBase64String("teste teste teste"))).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Sucesso")));
	}

	private static String asJsonBase64String(final Object obj) {
		try {
			return "{ \"data\":\""
					+ Base64.getEncoder().encodeToString(new ObjectMapper().writeValueAsString(obj).getBytes()) + "\"}";
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
