# ADR 0012: Saldo das contas calculado na hora

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

O saldo de uma conta é o saldo inicial mais as entradas menos as saídas daquela conta (RN07). Ele aparece na lista de contas (RF09), na visão geral (RF21) e na consulta de saldo em uma data (RF23).

Os lançamentos podem ser editados e excluídos a qualquer momento, inclusive os antigos. A visão geral e a listagem do mês precisam responder em menos de 500 ms com até 10 mil lançamentos (RNF16).

## Opções consideradas

- **Calcular o saldo na hora**, somando os lançamentos no banco.
- **Guardar o saldo numa coluna da conta** e atualizar essa coluna a cada lançamento criado, editado ou excluído.

## Decisão

Calcular o saldo na hora, com uma consulta SQL que soma os lançamentos de cada conta. O saldo nunca fica guardado.

A tabela de lançamentos tem um índice por conta e data para essa soma ser rápida.

## Justificativa

- O saldo está sempre certo. Editar ou excluir um lançamento antigo muda o saldo automaticamente, sem risco de a coluna ficar desatualizada se algo falhar no meio ou se dois lançamentos forem salvos ao mesmo tempo.
- A consulta de saldo em uma data (RF23) é a mesma soma, só que considerando os lançamentos até aquela data.
- O volume é pequeno: finanças pessoais geram alguns milhares de lançamentos por ano. Com o índice, a soma fica bem abaixo dos 500 ms do RNF16.
- Guardar o saldo é uma otimização para um volume que o sistema não tem, e traz problemas de concorrência e de consistência que eu teria que resolver.

## Consequências

- Toda tela que mostra saldo faz uma consulta de soma. A visão geral calcula o saldo de todas as contas numa consulta só, agrupada por conta, em vez de uma consulta por conta.
- O índice de conta e data é criado numa migração do Flyway (ADR 0005).
- Se um dia ficar lento, a solução seria guardar o saldo de cada mês fechado e somar só o mês atual. Isso seria registrado num novo ADR, e só depois de medir que existe um problema.
