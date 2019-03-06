package application.control;

import static application.control.utils.JsonUtils.getData;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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

	@Autowired
	private BinaryDiffService service;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping(path = "/diff/{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResultDto setLeftFile(@PathVariable("id") final Long id, @RequestBody final String file) {
		service.setLeftFile(id, getData(file));
		return new ResultDto(Messages.getString("success"));
	}

	@PostMapping(path = "/diff/{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResultDto setRightFile(@PathVariable("id") final Long id, @RequestBody final String file) {
		service.setRightFile(id, getData(file));
		return new ResultDto(Messages.getString("success"));
	}

	@GetMapping(path = "/diff")
	public @ResponseBody List<DiffDto> getDiff() {
		return service.getDiff().stream().map(d -> modelMapper.map(d, DiffDto.class)).collect(Collectors.toList());
	}
}
