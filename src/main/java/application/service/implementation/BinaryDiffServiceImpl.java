package application.service.implementation;

import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import application.Messages;
import application.domain.BinaryFile;
import application.service.BinaryDiffService;
import application.service.exception.BinaryDiffException;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BinaryDiffServiceImpl implements BinaryDiffService {

	private BinaryFile leftFile;
	private BinaryFile rightFile;

	@Override
	public void setLeftFile(final Long id, final String base64) {
		leftFile = new BinaryFile(id, base64);
	}

	@Override
	public void setRightFile(final Long id, final String base64) {
		rightFile = new BinaryFile(id, base64);
	}

	@Override
	public List<Diff> getDiff() {
		validateNotNull();

		final byte[] left = Base64.getDecoder().decode(leftFile.getData());
		final byte[] right = Base64.getDecoder().decode(rightFile.getData());

		if (left.length != right.length)
			return Arrays.asList(new Diff(Messages.getString("diff_size")));

		if (Arrays.equals(left, right))
			return Arrays.asList(new Diff(Messages.getString("diff_equals")));

		return diffBytes(left, right);
	}

	/*
	 * Verifica se as strings são não nulas.
	 *
	 * Caso sejam, lança exceção.
	 */
	private void validateNotNull() {
		final String left64 = ofNullable(leftFile).map(BinaryFile::getData).orElse(null);
		final String right64 = ofNullable(rightFile).map(BinaryFile::getData).orElse(null);
		if (left64 == null && right64 == null)
			throw new BinaryDiffException(Messages.getString("error_both"));
		if (left64 == null)
			throw new BinaryDiffException(Messages.getString("error_left"));
		if (right64 == null)
			throw new BinaryDiffException(Messages.getString("error_right"));
	}

	/*
	 * Diferencia dois arrays de bytes.
	 *
	 * Retorna as posições em que as diferenças foram encontradas e o comprimento
	 * contíguo das diferenças.
	 */
	private List<Diff> diffBytes(final byte[] left, final byte[] right) {
		final List<Diff> diffs = new ArrayList<>();
		Diff current = null;

		for (int i = 0; i < left.length; i++) {
			final byte leftByte = left[i];
			final byte rightByte = right[i];

			if (leftByte != rightByte) {
				if (current == null)
					current = new Diff(i);

				current.length++;
				continue;
			}

			if (current != null) {
				diffs.add(current);
				current = null;
			}
		}

		return diffs;
	}

	public static final class Diff {
		private String message;
		private int offset;
		private int length;

		public Diff() {
		}

		public Diff(final int offset) {
			message = Messages.getString("different");
			this.offset = offset;
		}

		public Diff(final String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(final String message) {
			this.message = message;
		}

		public int getOffset() {
			return offset;
		}

		public void setOffset(final int offset) {
			this.offset = offset;
		}

		public int getLength() {
			return length;
		}

		public void setLength(final int length) {
			this.length = length;
		}
	}

}
