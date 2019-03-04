package application.service.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import application.service.BinaryDiffService;
import application.service.exception.BinaryDiffException;

@Service
public class BinaryDiffServiceImpl implements BinaryDiffService {
	static final class Diff {
		final int offset;
		int length;

		public Diff(final int offset) {
			this.offset = offset;
		}

		@Override
		public String toString() {
			return "[offset: " + offset + ", length: " + length + "]";
		}
	}

	@Override
	public String getDiff(final String left64, final String right64) throws BinaryDiffException {
		final byte[] left = Base64.getDecoder().decode(left64);
		final byte[] right = Base64.getDecoder().decode(right64);

		if (left.length != right.length)
			return "size";

		if (Arrays.equals(left, right))
			return "true";

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

}
