# ADR 0007: Estratégia de testes com PostgreSQL de verdade (Testcontainers)

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

O sistema precisa de testes de unidade e de integração com banco real rodando a cada push (RNF10), e de testes garantindo que um usuário não acessa dados de outro (RNF03).

## Opções consideradas

- **Testes de integração com H2** em memória, como no springauth.
- **Testes de integração com PostgreSQL de verdade**, rodando num container com Testcontainers.
- **Só testes unitários com mocks**, sem banco.

## Decisão

- **Testes unitários** (JUnit 5 e AssertJ, com Mockito só quando precisar) para as regras de negócio. Eles não sobem o Spring e rodam rápido.
- **Testes de integração com Testcontainers:** um PostgreSQL de verdade sobe num container, e as migrações do Flyway rodam nele antes dos testes. Um único container é compartilhado entre os testes, para não ficar lento.
- **Testes da API** passando pela segurança e pelo banco de verdade, sem mock no meio. Para cada recurso, existe um teste em que um usuário tenta acessar o dado de outro e recebe 404 (RNF03 e ADR 0002).
- **Teste de arquitetura** com o Spring Modulith, verificando as fronteiras entre os módulos (ADR 0004).
- Não uso H2 em nenhum teste.

## Justificativa

- Os testes rodam no mesmo banco que o sistema usa (ADR 0005). O que passa no teste funciona de verdade, incluindo chaves estrangeiras, tipos e as próprias migrações.
- Os testes de isolamento entre usuários são a garantia principal do ADR 0002, já que na v1 só existe um usuário de verdade.
- O Spring Boot já tem integração pronta com o Testcontainers, então a configuração é pequena.
- Só mocks testam se as partes conversam do jeito combinado, mas não se o sistema funciona de verdade. Foi isso que falhou no springauth.

## Consequências

- Preciso do Docker instalado para rodar os testes, tanto na minha máquina quanto no pipeline (os runners do GitHub Actions já têm Docker).
- Os testes de integração demoram alguns segundos a mais que os unitários. Compartilhar o container ajuda a manter o tempo baixo.
- Não vou definir uma meta de cobertura no começo. Os critérios de aceite de cada issue viram testes, e isso guia o que testar.
