package application.control.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.Messages;
import application.service.exception.BinaryDiffException;
import lombok.experimental.UtilityClass;

public @UtilityClass class JsonUtils {
	public static String createJsonResult(final String key, final String value) {
		final Map<String, String> map = new HashMap<>();
		map.put(key, value);
		try {
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (final JsonProcessingException e) {
			return Messages.getString("error_json");
		}
	}

	public static String getData(final String file) {
		try {
			return new ObjectMapper().readTree(file).get(Messages.getString("data")).asText();
		} catch (final IOException e) {
			throw new BinaryDiffException(Messages.getString("error_read_data"));
		}
	}
}
