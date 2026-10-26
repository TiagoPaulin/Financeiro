# ADR 0004: Backend organizado como monolito modular

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

O backend vai começar pequeno, mas vai crescer a cada versão. Preciso decidir como organizar o código para que ele continue fácil de entender e de mudar conforme cresce, sem complicar demais um projeto que só eu desenvolvo.

## Opções consideradas

- **Arquitetura em camadas:** o código dividido por tipo técnico
- **Clean Architecture ou Hexagonal no sistema inteiro:** regras de negócio isoladas de frameworks e banco de dados por meio de interfaces em todas as partes.
- **Microsserviços:** cada parte do sistema como uma aplicação separada.
- **Monolito modular:** uma única aplicação, dividida por assunto do negócio, com camadas dentro de cada módulo.

## Decisão

Organizar o backend como um monolito modular, com estes módulos na v1:

- `identity`: usuários, cadastro e login
- `finance`: contas, categorias, lançamentos e saldo
- `dashboard`: visão geral do mês (só lê dados)
- `shared`: o que é comum a todos, como o formato de erro e a configuração de segurança

Regras entre os módulos:

1. Cada módulo é dono das próprias tabelas. Outro módulo não usa as entidades nem os repositories dele, só os services que ele expõe.
2. As dependências vão num sentido só, sem ciclos: `dashboard` → `finance` → `identity`.
3. Dentro de cada módulo, as camadas de sempre: controllers e DTOs, services, e entidades e repositories numa parte interna que só o próprio módulo enxerga.
4. As fronteiras são verificadas por um teste com o Spring Modulith, que falha se um módulo usar algo interno de outro.
5. Portas e adaptadores (Hexagonal) só onde houver uma dependência externa para isolar, como o serviço de cotações dos investimentos.

Contas, categorias e lançamentos ficam no mesmo módulo porque dependem muito um do outro: o lançamento precisa validar a conta e a categoria, e o saldo da conta precisa somar os lançamentos. Em módulos separados, isso viraria uma dependência circular. A divisão é por assunto do negócio, não por tabela.

## Justificativa

- Continua simples de rodar como um monolito: uma aplicação e um deploy só.
- Tudo sobre um assunto fica num lugar só. Para mexer em lançamentos, eu abro só o `finance`.
- Cada versão nova vira um módulo novo (como `investments`) sem mexer nos módulos que já existem.
- Uma mudança dentro de um módulo não quebra os outros, desde que o que ele expõe continue igual.
- Com as regras verificadas por teste, a arquitetura não depende só da minha disciplina.
- Clean ou Hexagonal no sistema inteiro traria várias interfaces e mapeamentos sem necessidade, já que a v1 não tem nenhuma integração externa.
- Microsserviços trariam rede, vários deploys e consistência distribuída para um sistema de um usuário, o que é over-engineering para o meu contexto.
- Camadas puras seriam simples hoje, mas com o tempo tudo tende a ficar ligado a tudo.

## Consequências

- Preciso decidir em qual módulo cada coisa fica e o que cada módulo expõe para os outros.
- O teste do Spring Modulith roda no pipeline. Se eu quebrar uma regra, o build falha.
- Consultas que juntam dados de mais de um módulo, como a visão geral, passam pelos services públicos em vez de consultar direto as tabelas dos outros módulos. Isso pode ser um pouco menos eficiente, mas não pesa no tamanho do sistema.
- Se um módulo um dia precisar virar um serviço separado, a fronteira dele já existe.
