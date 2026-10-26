# ADR 0005: PostgreSQL como banco de dados e Flyway para as migrações

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

O sistema guarda dados financeiros que se relacionam entre si: cada lançamento pertence a uma conta, a uma categoria e a um usuário. Os saldos e a visão geral dependem de somar e agrupar esses lançamentos, e os números precisam estar sempre corretos (RNF07).

Também preciso de um jeito de criar e alterar as tabelas que seja igual em todo lugar onde o sistema rodar (minha máquina, o pipeline e, no futuro, a hospedagem), com histórico de cada mudança (RNF09).

## Opções consideradas

Banco de dados:

- **PostgreSQL:** banco relacional, open source.
- **MySQL:** banco relacional, open source.
- **MongoDB:** banco NoSQL de documentos.
- **H2 ou SQLite:** bancos embutidos na própria aplicação.

Criação e alteração das tabelas:

- **Deixar o Hibernate criar e alterar as tabelas** (`ddl-auto=update`).
- **Flyway:** migrações escritas em SQL, aplicadas em ordem.
- **Liquibase:** migrações escritas em XML, YAML ou SQL, com mais recursos, como rollback.

## Decisão

- Usar o **PostgreSQL** como banco de dados, rodando com Docker Compose na v1.
- Criar e alterar as tabelas com o **Flyway**, usando migrações em SQL na pasta `src/main/resources/db/migration` (`V1__...sql`, `V2__...sql`).
- Configurar o Hibernate com `ddl-auto=validate`: ele não cria nem altera nada, só confere se as entidades batem com as tabelas quando a aplicação sobe.
- Nomes de tabelas e colunas em inglês, em `snake_case`, seguindo o código.

## Justificativa

- Os dados são relacionais e precisam de consistência forte. Um banco relacional garante isso com transações e chaves estrangeiras, como a que liga cada lançamento ao seu usuário (RN01).
- A visão geral é basicamente somar e agrupar lançamentos, que é o que SQL faz melhor.
- PostgreSQL e MySQL atenderiam bem. Escolhi o PostgreSQL porque já usei no springauth e porque é o banco suportado pelos serviços gratuitos que posso usar numa demonstração, como o Neon (ver ADR 0003).
- O MongoDB não combina com dados tão relacionados e com a necessidade de consistência dos saldos.
- H2 e SQLite são simples, mas ficam longe do banco que vai rodar de verdade. No springauth, os testes com H2 deixaram passar problemas que só aparecem num banco real.
- O `ddl-auto=update` não guarda histórico, não lida bem com renomear ou apagar colunas e pode deixar bancos diferentes em estados diferentes.
- Entre Flyway e Liquibase, o Flyway é mais simples: as migrações são SQL puro, e o Spring Boot já roda elas sozinho quando a aplicação sobe. Os recursos extras do Liquibase não fazem falta para o tamanho do projeto.

## Consequências

- Toda mudança no banco vira um arquivo de migração novo. Uma migração que já foi aplicada nunca é editada: se precisar corrigir algo, crio a próxima.
- Eu escrevo o SQL das tabelas na mão, o que dá mais controle e exige conhecer bem o banco.
- Se uma entidade não bater com a tabela, a aplicação não sobe. O erro aparece logo, e não em produção.
- Os testes de integração também precisam rodar num PostgreSQL de verdade, e não em H2. Isso vai ser definido no ADR de testes.
- O PostgreSQL roda num container junto com a aplicação (RNF12), então ninguém precisa instalar banco na máquina.
