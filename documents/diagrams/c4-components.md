# Diagrama de componentes (C4 nível 3)

Abre a caixa da API e mostra os módulos que existem dentro dela e quem depende de quem (ADR 0004).

```mermaid
flowchart TB
    web["<b>Frontend</b><br/>[Container: React]<br/><br/>Telas do sistema<br/>no navegador"]

    subgraph api["API [Container: Java 25 e Spring Boot 4]"]
        identity["<b>identity</b><br/>[Módulo]<br/><br/>Cadastro, login, JWT<br/>e redefinição de senha"]
        finance["<b>finance</b><br/>[Módulo]<br/><br/>Contas, categorias,<br/>lançamentos e saldo"]
        dashboard["<b>dashboard</b><br/>[Módulo]<br/><br/>Visão geral do mês<br/>(só lê dados)"]
        shared["<b>shared</b><br/>[Módulo]<br/><br/>Usado por todos os módulos:<br/>formato de erro e<br/>configuração de segurança"]
    end

    db[("<b>Banco de dados</b><br/>[Container: PostgreSQL]")]
    email["<b>Serviço de email</b><br/>[Sistema externo]"]

    web -- "Cadastro e login<br/>[JSON/HTTPS]" --> identity
    web -- "Contas, categorias<br/>e lançamentos<br/>[JSON/HTTPS]" --> finance
    web -- "Visão geral<br/>[JSON/HTTPS]" --> dashboard

    dashboard -- "Pede totais<br/>e saldos" --> finance
    finance -- "Identifica o<br/>usuário logado" --> identity

    identity -- "Lê e grava<br/>[JDBC]" --> db
    finance -- "Lê e grava<br/>[JDBC]" --> db
    identity -- "Envia o link de redefinição<br/>de senha [SMTP]" --> email

    classDef container fill:#438dd5,stroke:#2e6295,color:#fff
    classDef component fill:#85bbf0,stroke:#5d82a8,color:#000
    classDef external fill:#999999,stroke:#6b6b6b,color:#fff
    class web,db container
    class identity,finance,dashboard,shared component
    class email external
    style api fill:none,stroke:#438dd5,stroke-width:2px,stroke-dasharray:6 4
```

**Legenda:** azul-claro são os módulos da API, azul são os outros containers do meu sistema, cinza é sistema externo e a borda tracejada é o limite da API.

## O que o diagrama mostra

- **identity:** cadastro, login, autenticação com JWT e redefinição de senha (ADR 0010). Guarda os usuários e os tokens, e é o único módulo que envia email.
- **finance:** contas, categorias, lançamentos e o cálculo do saldo. Guarda as contas, as categorias e os lançamentos.
- **dashboard:** monta a visão geral do mês. Não tem tabelas próprias e não acessa o banco: pede os totais e os saldos ao `finance`.
- **shared:** o que é comum a todos, como o formato de erro (ADR 0009) e a configuração de segurança. Todos os módulos usam o `shared`, mas essas setas ficaram de fora para o diagrama não poluir.

## Regras entre os módulos

- As dependências vão num sentido só: `dashboard` → `finance` → `identity`. Nenhum módulo depende de quem está acima dele.
- Um módulo não usa as entidades nem os repositories de outro, só os services que o outro expõe. É por isso que o `dashboard` pede os dados ao `finance` em vez de consultar o banco.
- Essas regras são verificadas por um teste com o Spring Modulith (ADR 0004).

## O que fica de fora

- **As classes de cada módulo** (controllers, services, repositories): mudam o tempo todo, e o próprio código mostra isso melhor que um diagrama.
- **Investimentos:** quando chegarem, na v2, viram um novo módulo neste diagrama.
