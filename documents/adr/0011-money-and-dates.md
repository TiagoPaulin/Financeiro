# ADR 0011: Representação de dinheiro e de datas

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

O RNF06 já define que os valores ficam guardados em centavos, como `bigint` no banco e `long` no Java. Ainda falta decidir:

- como os valores aparecem na API;
- como evitar misturar reais e centavos no código;
- como arredondar os percentuais da visão geral (RF20);
- como tratar as datas sem erro de fuso horário.

## Opções consideradas

Valores na API:

- **Em reais, com casas decimais** (`10.50`).
- **Em centavos, como número inteiro** (`1050`).

Valores no código:

- **`long` direto** em todo lugar.
- **Um tipo próprio** para dinheiro.

Data do lançamento:

- **Data e hora.**
- **Só a data.**

## Decisão

- A API recebe e devolve os valores **em centavos**, com nomes que deixam isso claro, como `amountInCents`. O frontend converte para reais na hora de mostrar e de receber o que o usuário digitar.
- No código, os valores usam um **record `Money`**, que guarda os centavos e sabe somar e subtrair. As regras de negócio nunca trabalham com um `long` solto.
- Os **percentuais** da visão geral (RF20) são calculados com `BigDecimal` e arredondados para uma casa decimal. Eles são só para exibição e nunca alteram um valor guardado.
- A **data do lançamento** é só a data, sem hora nem fuso: `LocalDate` no Java e `date` no banco (RN06).
- As **datas e horas de registro**, como "criado em" e "alterado em", ficam em UTC: `Instant` no Java e `timestamptz` no banco. O frontend converte para o horário de quem está usando.
- A aplicação e o banco rodam configurados em UTC.

## Justificativa

- Com centavos também na API, o valor é o mesmo do banco até a tela: o backend não converte nem arredonda nada.
- Em JavaScript, números com casas decimais viram ponto flutuante (`0.1 + 0.2` não dá exatamente `0.3`). Mandar inteiros evita esse problema no frontend. Várias APIs de pagamento, como a do Stripe, trabalham assim.
- O record `Money` impede de somar reais com centavos por engano e deixa claro, no código, que aquele número é dinheiro.
- Um lançamento do dia 31/10 é do dia 31/10 em qualquer fuso. Guardar só a data evita que um gasto "mude de mês" por causa do horário, e o mês da visão geral fica sem ambiguidade.
- Usar UTC para data e hora evita o tipo de bug que aconteceu no springauth.

## Consequências

- O frontend precisa converter centavos para reais nos dois sentidos, e a documentação da API precisa deixar claro que os valores são em centavos.
- O `Money` precisa de um mapeamento para a coluna `bigint` no JPA.
- Como o sistema só trabalha com real (BRL), o `Money` não guarda a moeda. Se um dia houver mais de uma moeda, ele passa a guardar também.
