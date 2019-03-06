package application.control.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import application.Messages;
import application.service.exception.BinaryDiffException;

public class JsonUtils {
	public static String getData(final String file) {
		try {
			return new ObjectMapper().readTree(file).get(Messages.getString("data")).asText();
		} catch (final IOException e) {
			throw new BinaryDiffException(Messages.getString("error_read_data"));
		}
	}

	private JsonUtils() {
	}
}
