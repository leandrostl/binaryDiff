package application.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BinaryDiffException extends RuntimeException {
	private static final long serialVersionUID = 5728151291858594686L;

	public BinaryDiffException(final String mensagem) {
		super(mensagem);
	}

}
