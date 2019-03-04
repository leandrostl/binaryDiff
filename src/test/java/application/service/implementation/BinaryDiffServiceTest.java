package application.service.implementation;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Base64;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.service.BinaryDiffService;

public class BinaryDiffServiceTest {
	private final BinaryDiffService service = new BinaryDiffServiceImpl();

	@Test
	public void testGetDiff_equals() throws Exception {
		final String obj64 = toBase64("teste teste teste");
		assertThat(service.getDiff(obj64, obj64), equalTo("true"));
	}

	@Test
	public void testGetDiff_size() throws Exception {
		final String obj1 = toBase64("teste teste teste");
		final String obj2 = toBase64("teste teste teste teste");
		assertThat(service.getDiff(obj1, obj2), equalTo("size"));
	}

	@Test
	public void testGetDiff_diff() throws Exception {
		final String obj1 = toBase64("teste texsa teste teste");
		final String obj2 = toBase64("teste teste teste quest");
		assertThat(service.getDiff(obj1, obj2), equalTo("[offset: 9, length: 3], [offset: 19, length: 5]"));
	}

	private static String toBase64(final Object obj) throws JsonProcessingException {
		return Base64.getEncoder().encodeToString(new ObjectMapper().writeValueAsString(obj).getBytes());
	}

}
