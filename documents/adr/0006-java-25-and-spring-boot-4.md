# ADR 0006: Java 25, Spring Boot 4.1 e Maven

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

Antes de criar o projeto, preciso escolher as versões do Java e do Spring Boot e a ferramenta de build.

## Opções consideradas

- **Java 21 com Spring Boot 3.5:** as versões que já conheço.
- **Java 21 com Spring Boot 4.1:** a linha atual do Spring Boot, mantendo o Java que já uso.
- **Java 25 com Spring Boot 4.1:** as versões mais recentes com suporte longo.

Para o build:

- **Maven**
- **Gradle**

## Decisão

- **Java 25**, a versão LTS (suporte de longo prazo) mais recente.
- **Spring Boot 4.1**, a linha atual, com suporte gratuito até julho de 2027.
- **Maven**, usando o wrapper (`./mvnw`) para ninguém precisar instalar o Maven.

## Justificativa

- Começar um projeto novo numa versão sem suporte seria começar com dívida técnica e risco de segurança desde o primeiro dia.
- O Java 25 é LTS, então vai continuar recebendo atualizações por bastante tempo, e o Spring Boot 4 suporta até o Java 25.
- O Maven é o que já uso. O Gradle é mais flexível, mas não preciso dessa flexibilidade.

## Consequências

- Algumas coisas mudaram do Spring Boot 3 para o 4, como o Spring Security 7, o Jackson 3 e a organização dos starters.
- Todas as bibliotecas precisam de uma versão compatível com o Spring Boot 4, como o Spring Modulith 2.
- O Spring Boot lança uma versão nova a cada seis meses. Preciso atualizar com frequência para não ficar sem suporte de novo, e o Dependabot ajuda nisso (ADR 0008).
- A imagem Docker usa uma base com Java 25.
