# Escopo do projeto

## O que é

Um sistema web para eu controlar minhas finanças pessoais: lançar entradas e saídas, ver para onde meu dinheiro está indo e, mais pra frente, acompanhar meus investimentos.

No começo vai ser só para mim, mas quero que no futuro outras pessoas também possam usar para gerenciar suas próprias finanças

## Por que estou fazendo

Hoje eu controlo tudo numa planilha. O problema é que, mesmo registrando tudo:

- não tenho uma boa visão dos meus gastos;
- não sei quanto meus investimentos estão rendendo;
- não sei se o que registrei está batendo com o saldo das minhas contas.

Além de resolver minha dor, quero aplicar o que estou aprendendo na pós (arquitetura, DevOps, segurança), mas na medida certa para o tamanho do projeto.

## O que entra na v1

A ideia da v1 é ter algo funcional para o dia a dia:

- **Cadastro e login**
- **Contas** de bancos os quais eu sou cliente e meu saldo em cada um
- **Categorias** de entrada e de saída (alimentação, moradia, salário...), já com algumas prontas e podendo criar as minhas
- **Lançamentos** de entrada e saída, com data, valor, descrição, categoria e conta
- **Visão geral do mês**: quanto entrou, quanto saiu, quanto sobrou e quanto gastei em cada categoria

Ainda estou decidindo se **transferências** entre contas entram na v1. Sem elas, passar dinheiro de uma conta para outra teria que ser lançado como uma saída e uma entrada, o que bagunça a visão dos gastos.

## O que fica para depois

- Investimentos: aportes, resgates e quanto estão rendendo
- Contas fixas que se repetem todo mês
- Orçamento por categoria
- Cartão de crédito: fatura e compras parceladas
- Trazer os dados da minha planilha para o sistema

## O que não vou fazer, ou pelo menos por enquanto

- Outras moedas além do real
- App de celular (o acesso vai ser pelo navegador)
- Integração automática com banco

## Limitações

- Não quero gastar com hospedagem por enquanto, então a v1 vai rodar na minha máquina com Docker.
- Só eu vou desenvolver, principalmente nos fins de semana.
- Backend em Java com Spring Boot e frontend em React (a princípio), os dois no mesmo repositório. Vou fazer o backend primeiro.
- Curva de aprendizado ao lidar com novas tecnologias

## Critério de aceite da v1

Quando eu conseguir usar o sistema no lugar da planilha por um mês inteiro e o saldo bater com o do banco.

## Alguns termos

- **Lançamento**: uma entrada ou uma saída de dinheiro.
- **Categoria**: o tipo do lançamento (mercado, aluguel, salário...).
- **Conta**: onde o dinheiro está (conta corrente, poupança, carteira).
- **Transferência**: dinheiro que passa de uma conta minha para outra. Não é gasto nem entrada.

## Dúvidas que ainda preciso resolver

- Transferências entre contas entram na v1?
- Lanço o gasto no dia da compra ou no dia em que pago? Isso muda em qual mês o gasto aparece, principalmente no cartão.
- Quais categorias vou usar? Vou olhar as que já uso na planilha.
- Qual vai ser o nome do meu produto?