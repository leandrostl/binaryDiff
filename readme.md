# Comparador de Binários Base64

Essa aplicação permite inserir dois Json com binários codificados como String Base64, cada um em em *endpoint* e em um terceiro *endpoint* obter a diferença entre esses arquivos.

### Endpoint
* Arquivo da esquerda: http://localhost:8080/v1/diff/{id}/left
* Arquivo da direita: http://localhost:8080/v1/diff/{id}/right
* Diferença: http://localhost:8080/v1/diff

Os endpoints para inserção de arquivos da direita e da esquerda são requisições **POST** que recebem como *Request Parameter* um id para o arquivo e como *Request Body* um Json com o campo **data** contendo uma string representando o arquivo binário codificado para base64. O resultado é um Json com o campo *result* iqual a *sucesso*.

**Ex:**
http://localhost:8080/v1/diff/1/left
```json
{
	"data":"TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQ="
}
```
Deve retornar:
```json
{
	"result":"Sucesso"
}
```
O endpoint para obter a diferença entre os aquivos é uma requisição **GET** que retorna um json com o campo **result** apresentando o resultado da diferenciação, que pode ser:
* **true:** os arquivos são idênticos;
```json
{
	"result":"true"
}
``` 
* **size:** os arquivos se diferem pelo tamanho;
```json
{
	"result":"size"
}
```
* **diff:** os arquivos tem o mesmo tamanho mas possuem diferenças. Nesse caso, retorna cada um das diferenças como a posição no array de bytes do arquivo decodificado e o comprimento dessa diferença no formato: *[Offset:<int>, Length:<int>]*.
Diferenciando os textos *"test test test test"* e *"azul test flor test"* obtemos a resposta:
```json
{
	"result":"[Offset: 0, Length: 4], [Offset: 10, Length: 4]"
}
```

**Exceções:**
* Caso o usuário tente usar o diff antes de enviar os arquivos, ele deverá receber uma resposta com status *bad request* e mensagem explicativa.
* Quando não for possível criar uma resposta Json haverá uma resposta com status *bad request* também com uma mensagem explicativa. 
* Se não for possível ler o campo **data** do Json de entrada do arquivo, haverá também uma resposta com status *bad request* e mensagem explicativa.
```json
{
    "timestamp": "2019-03-04T15:41:33.508+0000",
    "status": 400,
    "error": "Bad Request",
    "message": "Please, post left and right files before diff.",
    "path": "/v1/diff/"
}
```

## Stack
O projeto foi criado utilizando Java 8, Maven, Sprint Boot. Foram criados testes unitários para o *service* e testes de integração sobre o *controler*.
#### Session Scoped
Há inúmeras maneiras de se persistir os arquivos a serem comparados, poderia ter sido utilizado um banco em memória, que poderia acarretar em estouro de memória; poderia ser utilizado um banco de dados ou, como foi feito, gravar os arquivos em um *Stateful Service* com escopo de sessão.

As características da aplicação levaram a escolha do *Session Storage*. Como vantagens há a associação entre o usuário e os arquivos a serem comparados, não vazando informações entre usuários mesmo não sendo necessário fazer login e a limpeza da sessão assim que a conexão com o usuário é desfeita, não mantendo os arquivos em disco. Mesmo se o usuário quiser comparar outros dois arquivos, basta enviá-los que eles sobrescreverão os arquivos na sessão.

Poderia ter sido utilizado um banco de dados, mas para isso seria necessário o usuário fazer uma autenticação para que os dados gravados no banco pudessem ser acessados apenas por esse usuário. Seria necessário ainda limpar o banco quando o usuário deixasse a aplicação. Ressalta-se os custos de transações com o banco tanto para inserir os arquivos quanto para apagá-los posteriormente e os custos financeiros de se ter uma banco de dados à disposição mesmo com o uso esparso.

### Algorítmo
O *business* da aplicação é bastante simples e o algorítimo utilizado tem complexidade **O(n)**. 
