package application.service.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import application.Messages;
import application.service.BinaryDiffService;
import application.service.exception.BinaryDiffException;

@Service
public class BinaryDiffServiceImpl implements BinaryDiffService {

	@Override
	public String getDiff(final String left64, final String right64) {
		validateNotNull(left64, right64);

		final byte[] left = Base64.getDecoder().decode(left64);
		final byte[] right = Base64.getDecoder().decode(right64);

		if (left.length != right.length)
			return Messages.getString("diff_size");

		if (Arrays.equals(left, right))
			return Messages.getString("diff_equals");

		return diffBytes(left, right);
	}

	/*
	 * Verifica se as strings são não nulas.
	 *
	 * Caso sejam, lança exceção.
	 */
	private void validateNotNull(final String left64, final String right64) {
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
	private String diffBytes(final byte[] left, final byte[] right) {
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

		return diffs.stream().map(Diff::toString).collect(Collectors.joining(", "));
	}

	private static final class Diff {
		final int offset;
		int length;

		public Diff(final int offset) {
			this.offset = offset;
		}

		@Override
		public String toString() {
			return Messages.getString("diff", offset, length);
		}
	}

}
