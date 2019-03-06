package application;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

	private Messages() {
	}

	public static String getString(final String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getString(final String key, final Object... params) {
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
