package application.service.implementation;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Base64;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.Messages;
import application.service.BinaryDiffService;
import application.service.exception.BinaryDiffException;
import application.service.implementation.BinaryDiffServiceImpl.Diff;

public class BinaryDiffServiceTest {
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	private final BinaryDiffService service = new BinaryDiffServiceImpl();

	@Test
	public void testGetDiff_equals() throws Exception {
		final String obj64 = toBase64("teste teste teste");
		service.setLeftFile(1L, obj64);
		service.setRightFile(1L, obj64);
		final Diff diff1 = new Diff(Messages.getString("diff_equals"));
		diff1.setOffset(0);
		diff1.setLength(0);
		final List<Diff> diffs = service.getDiff();
		assertThat(diffs.get(0).getMessage(), equalTo(diff1.getMessage()));
		assertThat(diffs.get(0).getOffset(), equalTo(diff1.getOffset()));
		assertThat(diffs.get(0).getLength(), equalTo(diff1.getLength()));
	}

	@Test
	public void testGetDiff_size() throws Exception {
		service.setLeftFile(1L, toBase64("teste teste teste"));
		service.setRightFile(1L, toBase64("teste teste teste teste"));
		final Diff diff1 = new Diff(Messages.getString("diff_size"));
		diff1.setOffset(0);
		diff1.setLength(0);
		final List<Diff> diffs = service.getDiff();
		assertThat(diffs.get(0).getMessage(), equalTo(diff1.getMessage()));
		assertThat(diffs.get(0).getOffset(), equalTo(diff1.getOffset()));
		assertThat(diffs.get(0).getLength(), equalTo(diff1.getLength()));
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
		final Diff diff1 = new Diff(Messages.getString("different"));
		diff1.setOffset(9);
		diff1.setLength(3);
		final Diff diff2 = new Diff(Messages.getString("different"));
		diff2.setOffset(19);
		diff2.setLength(5);

		service.setLeftFile(1L, toBase64("teste texsa teste teste"));
		service.setRightFile(1L, toBase64("teste teste teste quest"));

		final List<Diff> diffs = service.getDiff();
		assertThat(diffs.get(0).getMessage(), equalTo(diff1.getMessage()));
		assertThat(diffs.get(0).getOffset(), equalTo(diff1.getOffset()));
		assertThat(diffs.get(0).getLength(), equalTo(diff1.getLength()));

		assertThat(diffs.get(1).getMessage(), equalTo(diff2.getMessage()));
		assertThat(diffs.get(1).getOffset(), equalTo(diff2.getOffset()));
		assertThat(diffs.get(1).getLength(), equalTo(diff2.getLength()));
	}

	private static String toBase64(final Object obj) throws JsonProcessingException {
		return Base64.getEncoder().encodeToString(new ObjectMapper().writeValueAsString(obj).getBytes());
	}

}
