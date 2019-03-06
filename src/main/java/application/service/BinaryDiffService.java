package application.service;

import java.util.List;

import application.service.implementation.BinaryDiffServiceImpl.Diff;

public interface BinaryDiffService {

	/**
	 * Diferencia arrays de bytes codificados como strings base64 gravados na
	 * sessão.
	 *
	 * Caso os arrays sejam nulos, lança exceção com mensagem apontando o array que
	 * deve ser preenchido.
	 *
	 * @return Para arrays iguais - true; Para arrays de tamanhos diferentes - size;
	 *         Para arrays diferentes - Posição da diferença e comprimento da mesma.
	 *
	 */
	List<Diff> getDiff();

	/**
	 * Cria e preenche o arquivo da esquerda.
	 *
	 * Preenche o arquivo da esquerda e o mantem associado à sessão no stefull
	 * service.
	 *
	 * @param id
	 * @param base64 String base 64 dos dados binários.
	 */
	void setLeftFile(final Long id, final String base64);

	/**
	 * Cria e preenche o arquivo da esquerda.
	 *
	 * Preenche o arquivo da direita e o mantem associado à sessão no stefull
	 * service.
	 *
	 * @param id
	 * @param base64 String base 64 dos dados binários.
	 */
	void setRightFile(final Long id, final String base64);
}
