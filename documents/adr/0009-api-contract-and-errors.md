# ADR 0009: Padrões da API e formato de erro

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

O backend vai ser usado pelo frontend em React, que fica no mesmo repositório (ADR 0001), e a API precisa ser documentada com OpenAPI (RNF11). Antes do primeiro endpoint, preciso definir as convenções: como são as rotas, como a documentação é feita, qual o formato dos erros e se a API tem versão.

## Opções consideradas

Documentação:

- **Gerada a partir do código** (code-first), com o springdoc.
- **Escrita antes do código** num arquivo OpenAPI (design-first).

Formato de erro:

- **Formato próprio**, como o `StandardError` do springauth.
- **Problem Details** (RFC 9457), o formato padrão para erros em APIs HTTP.

Versão:

- **Versão na URL**, como `/api/v1/accounts`.
- **Sem versão na URL.**

## Decisão

- REST com JSON. Todas as rotas começam com `/api`, e os recursos ficam no plural: `/api/accounts`, `/api/categories`, `/api/transactions`, `/api/dashboard`.
- O OpenAPI é **gerado a partir do código** com o springdoc, e o Swagger UI fica disponível no ambiente local.
- Os erros seguem o **Problem Details**, que o Spring já suporta com o `ProblemDetail`. Erros de validação trazem também a lista de campos com problema.
- **Sem versão na URL.**
- Códigos de status:
  - `201` com o cabeçalho `Location` ao criar
  - `204` quando a operação dá certo e não tem corpo, como excluir
  - `400` para dados inválidos
  - `401` quando não está autenticado
  - `404` quando o recurso não existe, inclusive quando ele pertence a outro usuário (ADR 0002)
  - `409` para conflitos, como email ou nome de conta repetido (RN02 e RN10), ou excluir uma conta que já tem lançamentos (RN08)
- Datas no formato ISO (`2026-10-26`) e listas paginadas.

## Justificativa

- Gerar a documentação a partir do código é mais simples para quem desenvolve sozinho. O design-first, que aparece nas minhas anotações da pós, faz mais sentido quando times diferentes precisam combinar o contrato antes de começar. Aqui o frontend também é meu e fica no mesmo repositório.
- O Problem Details é um padrão: bibliotecas e outros desenvolvedores já conhecem o formato, e o Spring já gera ele, então escrevo menos código do que com um formato próprio.
- Versão na URL serve para não quebrar clientes que eu não controlo. O único cliente da API é o meu frontend, no mesmo repositório, então uma mudança que quebra o contrato é ajustada no mesmo commit dos dois lados.
- O prefixo `/api` permite que o frontend e a API fiquem no mesmo endereço, separados só pelo caminho. Isso evita configurar CORS e facilita guardar o refresh token num cookie, se essa for a escolha (ADR 0010).

## Consequências

- Se um dia a API tiver clientes externos, como um app de celular, vou precisar versionar e registrar isso num novo ADR.
- O frontend só precisa saber tratar um formato de erro.
- As respostas de erro de cada endpoint também aparecem na documentação do OpenAPI.
