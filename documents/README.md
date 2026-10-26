# Documentação

Aqui ficam o planejamento e as decisões do projeto.

- [Escopo](scope.md): o que o sistema vai fazer, o que entra na v1 e o que fica para depois
- [Requisitos](requirements.md): requisitos funcionais, regras de negócio e requisitos não funcionais da v1

## Decisões de arquitetura (ADRs)

- [ADR 0001](adr/0001-monorepo.md): backend e frontend no mesmo repositório
- [ADR 0002](adr/0002-multi-user-from-v1.md): sistema multiusuário desde a v1
- [ADR 0003](adr/0003-defer-hosting.md): rodar localmente e deixar a hospedagem para depois
- [ADR 0004](adr/0004-modular-monolith.md): backend organizado como monolito modular
- [ADR 0005](adr/0005-postgresql-and-flyway.md): PostgreSQL como banco de dados e Flyway para as migrações
- [ADR 0006](adr/0006-java-25-and-spring-boot-4.md): Java 25, Spring Boot 4.1 e Maven
- [ADR 0007](adr/0007-testing-strategy.md): estratégia de testes com PostgreSQL de verdade (Testcontainers)
- [ADR 0008](adr/0008-ci-cd-pipeline.md): pipeline de CI/CD com GitHub Actions
- [ADR 0009](adr/0009-api-contract-and-errors.md): padrões da API e formato de erro
- [ADR 0010](adr/0010-jwt-authentication.md): autenticação com JWT
- [ADR 0011](adr/0011-money-and-dates.md): representação de dinheiro e de datas
- [ADR 0012](adr/0012-calculated-balance.md): saldo das contas calculado na hora

Próximo documento: a arquitetura do sistema.
