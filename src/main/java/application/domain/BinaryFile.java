package application.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

public @Data @AllArgsConstructor class BinaryFile implements Serializable {
	private static final long serialVersionUID = -1187982972327125580L;

	private Long id;
	private String data;
}
