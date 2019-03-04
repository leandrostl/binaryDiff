package application.control;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.Messages;
import application.domain.BinaryFile;
import application.service.BinaryDiffService;
import application.service.exception.BinaryDiffException;

@Controller
@RequestMapping("/v1")
public class BinaryDiffController {

	private static final String KEY_RESULT = Messages.getString("result"); //$NON-NLS-1$

	private static final String RESULT_SUCCESS = createJsonResult(KEY_RESULT, Messages.getString("sucesso")); //$NON-NLS-1$

	@Autowired
	private BinaryDiffService service;

	@Resource
	private BinaryDiffFiles files;

	@PostMapping(path = "/diff/{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String setLeftFile(@PathVariable("id") final Long id, @RequestBody final String file) {
		files.setLeft(createBinaryFile(id, file));
		return RESULT_SUCCESS;
	}

	@PostMapping(path = "/diff/{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String setRightFile(@PathVariable("id") final Long id, @RequestBody final String file) {
		files.setRight(createBinaryFile(id, file));
		return RESULT_SUCCESS;
	}

	@GetMapping(path = "/diff")
	public @ResponseBody String getDiff() {
		return createJsonResult(KEY_RESULT,
				service.getDiff(Optional.ofNullable(files.getLeft()).map(BinaryFile::getData).orElse(null),
						Optional.ofNullable(files.getRight()).map(BinaryFile::getData).orElse(null)));
	}

	private BinaryFile createBinaryFile(final Long id, final String file) {
		try {
			return new BinaryFile(id, new ObjectMapper().readTree(file).get(Messages.getString("data")).asText());
		} catch (final IOException e) {
			throw new BinaryDiffException(Messages.getString("error_read_data"));
		}
	}

	private static String createJsonResult(final String key, final String value) {
		final Map<String, String> map = new HashMap<>();
		map.put(key, value);
		try {
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (final JsonProcessingException e) {
			return "";
		}
	}
}
