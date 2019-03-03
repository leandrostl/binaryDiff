package application.control;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import application.domain.BinaryFile;
import lombok.Data;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public @Data class BinaryDiffFiles {
	private BinaryFile left;
	private BinaryFile right;
}
