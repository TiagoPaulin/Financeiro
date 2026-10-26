# Diagrama de containers (C4 nível 2)

Abre a caixa do sistema e mostra as partes que rodam separadas: o frontend, a API e o banco de dados, e como elas conversam.

```mermaid
flowchart TB
    usuario["<b>Usuário</b><br/>[Pessoa]<br/><br/>Pessoa que controla<br/>as próprias finanças"]

    subgraph financeiro["Financeiro [Sistema]"]
        web["<b>Frontend</b><br/>[Container: React]<br/><br/>Telas do sistema<br/>no navegador"]
        api["<b>API</b><br/>[Container: Java 25 e Spring Boot 4]<br/><br/>Regras de negócio,<br/>autenticação com JWT<br/>e API REST"]
        db[("<b>Banco de dados</b><br/>[Container: PostgreSQL]<br/><br/>Usuários, contas,<br/>categorias e lançamentos")]
    end

    email["<b>Serviço de email</b><br/>[Sistema externo]<br/><br/>Mailpit na v1"]

    usuario -- "Usa pelo navegador<br/>[HTTPS]" --> web
    web -- "Chama a API<br/>[JSON/HTTPS]" --> api
    api -- "Lê e grava<br/>[JDBC]" --> db
    api -- "Envia o link de redefinição<br/>de senha [SMTP]" --> email
    email -. "Entrega o email" .-> usuario

    classDef person fill:#08427b,stroke:#052e56,color:#fff
    classDef container fill:#438dd5,stroke:#2e6295,color:#fff
    classDef external fill:#999999,stroke:#6b6b6b,color:#fff
    class usuario person
    class web,api,db container
    class email external
    style financeiro fill:none,stroke:#1168bd,stroke-width:2px,stroke-dasharray:6 4
```

**Legenda:** azul-escuro é pessoa, azul-claro são os containers do meu sistema, cinza é sistema externo e a borda tracejada é o limite do sistema.

No C4, "container" é qualquer parte que roda separada, como uma aplicação ou um banco de dados. Não é a mesma coisa que um container Docker, embora aqui cada um deles também rode num container Docker.

## O que o diagrama mostra

- **Frontend:** as telas que o usuário acessa pelo navegador, feitas em React. Ele fica no mesmo repositório do backend, mas é uma aplicação separada e só conversa com a API (ADR 0001). Vai ser feito depois do backend.
- **API:** o backend em Java 25 com Spring Boot 4 (ADR 0006). Tem as regras de negócio, a autenticação com JWT (ADR 0010) e as rotas REST em `/api` (ADR 0009). Por dentro, é organizada em módulos (ADR 0004).
- **Banco de dados:** o PostgreSQL, onde ficam usuários, contas, categorias e lançamentos. As tabelas são criadas pelas migrações do Flyway (ADR 0005).
- **Serviço de email:** usado só para o link de redefinição de senha (RF05). Na v1 é o Mailpit, que mostra os emails numa página local sem enviar de verdade.