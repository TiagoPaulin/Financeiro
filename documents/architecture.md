# Arquitetura

Visão geral da arquitetura do sistema, organizada nas 12 seções do [arc42](https://arc42.org). Quando o assunto já está em outro documento, a seção só resume e aponta para ele.

## 1. Introdução e objetivos

Um sistema web para eu controlar minhas finanças pessoais e substituir a planilha que uso hoje: registrar entradas e saídas, saber o saldo de cada conta e ver para onde o dinheiro está indo.

As qualidades mais importantes para o sistema são a segurança dos dados financeiros, os valores sempre corretos e não perder dados.

O problema, o que entra na v1 e o critério de aceite estão no [escopo](scope.md).

## 2. Restrições

- Sem custo de hospedagem por enquanto: a v1 roda na minha máquina com Docker (ADR 0003).
- Só eu desenvolvo, principalmente nos fins de semana.
- Backend em Java com Spring Boot e frontend em React, os dois no mesmo repositório (ADR 0001 e ADR 0006).

Mais detalhes nas [limitações do escopo](scope.md#limitações).

## 3. Contexto

O usuário usa o sistema pelo navegador, e o sistema conversa com um serviço de email para enviar o link de redefinição de senha. Não existe integração com bancos.

Diagrama: [Contexto (C4 nível 1)](diagrams/c4-context.md).

## 4. Estratégia da solução

As decisões que mais definem o sistema:

- **Monolito modular:** uma aplicação só, dividida em módulos por assunto do negócio (ADR 0004).
- **PostgreSQL com migrações do Flyway** (ADR 0005).
- **API REST**, com os erros no formato Problem Details (ADR 0009).
- **Autenticação com JWT** e refresh token (ADR 0010).
- **Dinheiro em centavos e saldo calculado na hora**, para os valores estarem sempre corretos (ADR 0011 e ADR 0012).
- **Rodar localmente com Docker**, com um pipeline que deixa cada versão pronta para implantar (ADR 0003 e ADR 0008).

Em todas elas, a regra foi escolher o que o problema pede, sem over-engineering.

## 5. Blocos de construção

- [Containers (C4 nível 2)](diagrams/c4-containers.md): frontend, API, banco de dados e serviço de email.
- [Componentes (C4 nível 3)](diagrams/c4-components.md): os módulos da API (`identity`, `finance`, `dashboard` e `shared`).
- [Modelo de dados](diagrams/data-model.md): as tabelas de cada módulo e como se relacionam.

## 6. Visão de execução

- [Fluxos](diagrams/flows.md): o passo a passo de registrar um lançamento, com os caminhos de erro.

## 7. Implantação

Na v1, tudo roda na minha máquina com um único `docker compose up` (RNF12):

- **API:** a imagem Docker da aplicação, com Java 25, na porta 8080.
- **PostgreSQL:** na porta 5432, com os dados guardados num volume do Docker para não se perderem ao reiniciar.
- **Mailpit:** recebe os emails na porta 1025 (SMTP) e mostra numa página na porta 8025.
- **Frontend:** entra no Docker Compose quando for desenvolvido.
- **Grafana (opcional):** sobe só com `docker compose --profile observability up`, com os painéis na porta 3000 (ADR 0013, ainda em proposta).

As configurações e senhas vêm de variáveis de ambiente, num arquivo `.env` que não vai para o repositório (RNF05).

A cada nova versão, o pipeline publica a imagem da API no GitHub Container Registry (ADR 0008). Quando houver hospedagem, essa mesma imagem vai ser implantada lá.

## 8. Conceitos transversais

Assuntos que valem para o sistema inteiro:

- **Segurança:** autenticação com JWT (ADR 0010), senhas com BCrypt (RNF01) e segredos em variáveis de ambiente (RNF05). Cada usuário só acessa os próprios dados, e um dado de outro usuário responde 404 (ADR 0002).
- **Erros:** todas as respostas de erro seguem o formato Problem Details (ADR 0009).
- **Dinheiro e datas:** valores em centavos, data do lançamento sem hora e UTC para data e hora (ADR 0011).
- **Transações:** toda operação que altera dados salva tudo ou não salva nada (RNF07).
- **Testes:** unitários, de integração com PostgreSQL de verdade (Testcontainers) e das fronteiras entre os módulos (ADR 0007).
- **Logs e monitoramento:** logs estruturados com um identificador por requisição, health check e métricas com os golden signals (RNF14, RNF15 e RNF18). Os dados vão por OpenTelemetry para o Grafana (ADR 0013, ainda em proposta).

## 9. Decisões de arquitetura

Cada decisão importante está registrada num ADR, com o contexto, as opções consideradas e os trade-offs: [lista de ADRs](README.md#decisões-de-arquitetura-adrs).

## 10. Requisitos de qualidade

Estão nos [requisitos não funcionais](requirements.md#requisitos-não-funcionais), sempre que possível com uma meta que dá para conferir, como responder em menos de 500 ms com até 10 mil lançamentos (RNF16).

## 11. Riscos e dívidas técnicas

O que eu já sei que é risco ou limitação, e o que fazer a respeito:

- **O access token não pode ser cancelado antes de expirar.** Depois de um logout, ele ainda vale por até 15 minutos. A validade curta reduz o risco (ADR 0010).
- **Os dados ficam só na minha máquina.** Enquanto o backup automático não existir (RNF08), perder o disco é perder o histórico.
- **Não há limite de tentativas de login.** Rodando localmente não é um problema, mas precisa ser resolvido antes de hospedar (ADR 0010).
- **Ainda não há modelagem de ameaças.** Ela fica para antes de hospedar o sistema.
- **Curva de aprendizado.** Algumas tecnologias são novas para mim, como o Testcontainers, o Spring Modulith e o Spring Boot 4, e podem atrasar as primeiras entregas.
- **Decisões em aberto:** transferências entre contas, lançamento com data futura e exclusão da própria conta (pontos a decidir dos requisitos), e o tipo do `id` das tabelas (modelo de dados).

## 12. Glossário

Os termos do domínio estão em [alguns termos](scope.md#alguns-termos), no escopo.
