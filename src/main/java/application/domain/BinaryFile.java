package application.domain;

import java.io.Serializable;

public class BinaryFile implements Serializable {
	private static final long serialVersionUID = -1187982972327125580L;

	private Long id;
	private String data;

	public BinaryFile(final Long id, final String data) {
		super();
		this.id = id;
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(final String data) {
		this.data = data;
	}

}
