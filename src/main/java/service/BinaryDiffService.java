package service;

public interface BinaryDiffService {

	void setLeftFile(final Long id, final String file);

	void setRightFile(final Long id, final String file);

	String getDiff();

}
