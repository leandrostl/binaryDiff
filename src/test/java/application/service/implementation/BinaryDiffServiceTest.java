package application.service.implementation;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Base64;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.Messages;
import application.service.BinaryDiffService;
import application.service.exception.BinaryDiffException;

public class BinaryDiffServiceTest {
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	private final BinaryDiffService service = new BinaryDiffServiceImpl();

	@Test
	public void testGetDiff_equals() throws Exception {
		final String obj64 = toBase64("teste teste teste");
		service.setLeftFile(1L, obj64);
		service.setRightFile(1L, obj64);
		assertThat(service.getDiff(), equalTo(Messages.getString("diff_equals")));
	}

	@Test
	public void testGetDiff_size() throws Exception {
		service.setLeftFile(1L, toBase64("teste teste teste"));
		service.setRightFile(1L, toBase64("teste teste teste teste"));
		assertThat(service.getDiff(), equalTo(Messages.getString("diff_size")));
	}

	@Test
	public void testGetDiff_null() throws Exception {
		expectedEx.expect(BinaryDiffException.class);
		expectedEx.expectMessage(Messages.getString("error_both"));
		service.getDiff();
	}

	@Test
	public void testGetDiff_leftNull() throws Exception {
		expectedEx.expect(BinaryDiffException.class);
		expectedEx.expectMessage(Messages.getString("error_left"));
		service.setRightFile(1L, toBase64("teste teste teste teste"));
		service.getDiff();
	}

	@Test
	public void testGetDiff_rightNull() throws Exception {
		expectedEx.expect(BinaryDiffException.class);
		expectedEx.expectMessage(Messages.getString("error_right"));
		service.setLeftFile(1L, toBase64("teste teste teste teste"));
		service.getDiff();
	}

	@Test
	public void testGetDiff_diff() throws Exception {
		service.setLeftFile(1L, toBase64("teste texsa teste teste"));
		service.setRightFile(1L, toBase64("teste teste teste quest"));
		assertThat(service.getDiff(),
				equalTo(Messages.getString("diff", 9, 3) + ", " + Messages.getString("diff", 19, 5)));
	}

	private static String toBase64(final Object obj) throws JsonProcessingException {
		return Base64.getEncoder().encodeToString(new ObjectMapper().writeValueAsString(obj).getBytes());
	}

}
