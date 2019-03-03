package application.control;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import application.domain.BinaryFile;
import application.service.BinaryDiffService;
import application.service.exception.BinaryDiffException;

@Controller
@RequestMapping("/v1")
public class BinaryDiffController {

	@Autowired
	private BinaryDiffService service;

	@Resource
	private BinaryDiffFiles files;

	@PostMapping(path = "/diff/{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String setLeftFile(@PathVariable("id") final Long id, @RequestBody final String file) {
		try {
			files.setLeft(new BinaryFile(id, new ObjectMapper().readTree(file).get("data").asText()));
			return "Sucesso";
		} catch (final Exception e) {
			return e.getMessage();
		}
	}

	@PostMapping(path = "/diff/{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String setRightFile(@PathVariable("id") final Long id, @RequestBody final String file) {
		try {
			files.setRight(new BinaryFile(id, new ObjectMapper().readTree(file).get("data").asText()));
			return "Sucesso";
		} catch (final Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping(path = "/diff")
	public String getDiff() {
		try {
			return service.getDiff(files.getLeft().getData(), files.getRight().getData());
		} catch (final BinaryDiffException e) {
			return e.getMessage();
		}
	}

	@GetMapping
	public @ResponseBody String greeting() {
		return "Binary Diff funcionando!";
	}

}
