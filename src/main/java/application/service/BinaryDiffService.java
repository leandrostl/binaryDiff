package application.service;

import application.service.exception.BinaryDiffException;

public interface BinaryDiffService {

	String getDiff(final String left, final String right) throws BinaryDiffException;
}
