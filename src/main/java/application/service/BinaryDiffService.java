package application.service;

public interface BinaryDiffService {

	/**
	 * Diferencia arrays de bytes codificados como string Base64.
	 *
	 * Caso os arrays sejam nulos, lança exceção com mensagem apontando o array que
	 * deve ser preenchido.
	 *
	 * @param left  String base 64 do primeiro array.
	 * @param right String base 64 do segundo array.
	 * @return Para arrays iguais - true; Para arrays de tamanhos diferentes - size;
	 *         Para arrays diferentes - Posição da diferença e comprimento da mesma.
	 *
	 */
	String getDiff(final String left, final String right);
}
