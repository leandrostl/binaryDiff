package application.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import application.service.BinaryDiffService;

@Controller
@RequestMapping("/v1")
public class BinaryDiffController {

	@Autowired
	private BinaryDiffService service;

	@PostMapping(path = "/diff/{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String setLeftFile(@PathVariable("id") final Long id, @RequestBody final String file) {
		try {
			service.setLeftFile(id, file);
			return "Sucesso";
		} catch (final Exception e) {
			return "Erro - Não conseguiu guardar arquivo da esquerda.";
		}
	}

	@PostMapping(path = "/diff/{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String setRightFile(@PathVariable("id") final Long id, @RequestBody final String file) {
		try {
			service.setRightFile(id, file);
			return "Sucesso";
		} catch (final Exception e) {
			return "Erro - Não conseguiu guardar arquivo da direita.";
		}
	}

	@GetMapping(path = "/diff")
	public String getDiff() {
		try {
			return service.getDiff();
		} catch (final Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping
	public @ResponseBody String greeting() {
		return "Binary Diff funcionando!";
	}

}
