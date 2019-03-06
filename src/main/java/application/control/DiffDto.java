package application.control;

import application.Messages;

public class DiffDto {
	private String message;
	private int offset;
	private int length;

	public DiffDto() {
	}

	public DiffDto(final int offset) {
		message = Messages.getString("different");
		this.offset = offset;
	}

	public DiffDto(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(final int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(final int length) {
		this.length = length;
	}
}
