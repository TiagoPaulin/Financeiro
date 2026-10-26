# ADR 0001: Backend e frontend no mesmo repositório

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

O sistema vai ter um backend em Java com Spring Boot e, depois, um frontend em React. Preciso decidir se os dois ficam no mesmo repositório ou em repositórios separados.

Só eu desenvolvo o projeto, e toda a documentação (escopo, requisitos e ADRs) vale para o sistema inteiro, não só para uma das partes.

## Opções consideradas

- **Repositórios separados:** um repositório para o backend e outro para o frontend.
- **Mesmo repositório (monorepo):** cada aplicação na sua própria pasta, com build, testes e Dockerfile próprios.

## Decisão

Usar um único repositório, organizado assim:

- `backend/`: a API em Java com Spring Boot
- `frontend/`: a camada de visualização do sistema desenvolvida em React
- `documents/`: a documentação do sistema

## Justificativa

- A documentação é do sistema todo, então faz sentido ficar num lugar só, junto do código.
- Quando o contrato da API mudar, o backend e o frontend podem ser ajustados no mesmo commit.
- Um único `docker compose up` sobe o sistema inteiro.
- Repositórios separados resolvem problemas de times diferentes, com ritmos de entrega diferentes, o que não é o meu caso.
- Estar no mesmo repositório não junta as duas aplicações: elas continuam independentes e conversam só pela API.

## Consequências

- Cada pasta é um projeto independente. O frontend nunca importa código do backend, e a única ligação entre os dois é o contrato da API.
- O pipeline precisa rodar só a parte que mudou, usando filtro de pastas no GitHub Actions.
- Se um dia fizer sentido separar, é só mover a pasta para um repositório novo.
