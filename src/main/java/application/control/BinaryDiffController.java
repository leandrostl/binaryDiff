package application.control;

import static application.control.utils.JsonUtils.createJsonResult;
import static application.control.utils.JsonUtils.getData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import application.Messages;
import application.service.BinaryDiffService;

@Controller
@RequestMapping("/v1")
public class BinaryDiffController {

	private static final String KEY_RESULT = Messages.getString("result");

	private static final String RESULT_SUCCESS = createJsonResult(KEY_RESULT, Messages.getString("sucesso"));

	@Autowired
	private BinaryDiffService service;

	@PostMapping(path = "/diff/{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String setLeftFile(@PathVariable("id") final Long id, @RequestBody final String file) {
		service.setLeftFile(id, getData(file));
		return RESULT_SUCCESS;
	}

	@PostMapping(path = "/diff/{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String setRightFile(@PathVariable("id") final Long id, @RequestBody final String file) {
		service.setRightFile(id, getData(file));
		return RESULT_SUCCESS;
	}

	@GetMapping(path = "/diff")
	public @ResponseBody String getDiff() {
		return createJsonResult(KEY_RESULT, service.getDiff());
	}
}
