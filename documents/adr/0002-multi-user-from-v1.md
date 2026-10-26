# ADR 0002: Sistema multiusuário desde a v1

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

Na v1 só eu vou usar o sistema, mas quero que no futuro outras pessoas também possam usar para gerenciar as próprias finanças.

Como são dados financeiros, uma pessoa nunca pode ver nem alterar os dados de outra (RN01).

## Opções consideradas

- **Fazer a v1 para um usuário só** e adaptar o sistema quando abrir para outras pessoas.
- **Fazer a v1 já multiusuário**, com todo dado ligado ao usuário dono dele.

## Decisão

O sistema é multiusuário desde a v1. Contas, categorias e lançamentos pertencem a um usuário, e toda consulta filtra pelo usuário que está logado.

## Justificativa

- Ligar cada dado a um usuário agora custa pouco. Fazer isso depois exigiria mudar todas as tabelas e consultas e migrar os dados que já existem.
- A quebra de controle de acesso é a primeira categoria do OWASP Top 10. Tratar o isolamento dos dados desde o início evita esse tipo de falha, em vez de tentar corrigir depois.
- O cadastro de usuários (RF02) já faz parte da v1, então o conceito de usuário existe de qualquer jeito.

## Consequências

- Toda tabela com dados do usuário tem uma coluna que indica o dono (`user_id`).
- Toda consulta precisa filtrar pelo usuário logado. Esquecer esse filtro em um lugar já expõe dados de outra pessoa, então isso precisa de testes automatizados (RNF03).
- Quando alguém tentar acessar um dado de outro usuário, a resposta é "não encontrado" (404), para não revelar que o dado existe.
- Na v1 o sistema vai ter um usuário só, então o isolamento vai ser validado principalmente pelos testes.
