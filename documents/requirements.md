# Elicitação de requisitos do sistema

Este documento registra as principais funcionalidades que vão definir o comportamento do meu sistema.

## Requisitos funcionais

### Usuário

- **RF01:** O sistema deve permitir que o usuário realize login na plataforma informando o email e senha cadastrados
- **RF02:** O sistema deve permitir que o usuário se cadastre informando nome, email e senha
- **RF03:** O sistema deve permitir que o usuário faça logout
- **RF04:** O sistema deve permitir que o usuário altere seu nome email e sua senha, informando a senha atual quando estiver logado 
- **RF05:** O sistema deve permitir que o usuário redefina sua senha estando deslogado ao informar o email cadastrado

### Contas

- **RF06:** O sistema deve permitir que o usuário cadastre uma conta informando nome, tipo (corrente, poupança ou carteira) e saldo inicial
- **RF07:** O sistema deve permitir que o usuário edite todos os dados de uma conta
- **RF08:** O sistema deve permitir que o usuário arquive uma conta que não usa mais 
- **RF09:** O sistema deve listar as contas do usuário com o saldo atual de cada uma e o saldo total

### Categorias

- **RF10:** O sistema deve criar um conjunto de categorias padrão para cada usuário novo (salário, alimentação, moradia)
- **RF10:** O sistema deve permitir que o usuário crie uma categoria informando nome e tipo (entrada ou saída)
- **RF11:** O sistema deve permitir que o usuário renomeie uma categoria 
- **RF12:** O sistema deve permitir que o usuário altere o tipo de uma categoria 
- **RF13:** O sistema deve permitir que o usuário arquive uma categoria 

### Lançamentos

- **RF14:** O sistema deve permitir que o usuário registre um lançamento informando tipo (entrada ou saída), data, valor, descrição, categoria e conta
- **RF15:** O sistema deve permitir que o usuário edite um lançamento
- **RF16:** O sistema deve permitir que o usuário exclua um lançamento
- **RF17:** O sistema deve listar os lançamentos de um mês, permitindo filtrar por tipo, categoria, conta e data
- **RF18:** O sistema deve permitir que o usuário busque lançamentos pela descrição 

### Visão geral

- **RF19:** O sistema deve mostrar, para o mês escolhido, o total de entradas, o total de saídas e o resultado (entradas menos saídas)
- **RF20:** O sistema deve mostrar os gastos do mês por categoria, em valor e em percentual
- **RF21:** O sistema deve mostrar o saldo de cada conta e o saldo total
- **RF22:** O sistema deve mostrar a evolução de entradas, saídas e resultado dos últimos meses 
- **RF23:** O sistema deve permitir que o usuário consulte o saldo de uma conta em uma data, para conferir com o extrato do banco 
- **RF24:** O sistema deve permitir que o usuário possa ocultar/exibir valores na tela, semelhante ao olho no campo de senha para garantir a privacidade.

## Regras de negócio

- **RN01:** Cada usuário só pode ver e alterar os próprios dados (contas, categorias e lançamentos)
- **RN02:** Não podem existir dois usuários com o mesmo email
- **RN03:** A senha deve ter no mínimo 8 caracteres
- **RN04:** O valor do lançamento deve ser maior que zero; o tipo (entrada ou saída) é que define se ele soma ou subtrai do saldo
- **RN05:** A categoria do lançamento deve ser do mesmo tipo do lançamento (categoria de saída só em saída)
- **RN06:** A data do lançamento é a data em que o dinheiro entrou ou saiu da conta (regime de caixa)
- **RN07:** O saldo de uma conta é o saldo inicial mais as entradas menos as saídas daquela conta
- **RN08:** Uma conta ou categoria que já tem lançamentos não pode ser excluída, só arquivada
- **RN09:** Contas e categorias arquivadas não aparecem para novos lançamentos, mas continuam no histórico e nos totais dos meses anteriores
- **RN10:** O usuário não pode ter duas contas com o mesmo nome, nem duas categorias do mesmo tipo com o mesmo nome
- **RN11:** Os valores são em reais, com duas casas decimais

## Requisitos não funcionais

### Segurança

- **RNF01:** As senhas devem ser armazenadas com hash (BCrypt), nunca em texto
- **RNF02:** Todas as funcionalidades, exceto cadastro, login e redefinição de senha devem exigir autenticação
- **RNF03:** Deve haver testes automatizados garantindo que um usuário não acessa dados de outro
- **RNF04:** Senhas, tokens e dados sensíveis não podem aparecer nos logs
- **RNF05:** Senhas do banco e chaves devem vir de variáveis de ambiente, nunca do código

### Confiabilidade

- **RNF06:** Valores monetários devem usar um tipo int no banco de dados armazenando a quantia em centavos exato, nunca `float` ou `double`
- **RNF07:** Operações que alteram dados devem ser transacionais: ou salvam tudo, ou não salvam nada
- **RNF08:** O banco deve ter backup automático diário, com a restauração testada pelo menos uma vez por mês

### Manutenibilidade

- **RNF09:** As mudanças no banco de dados devem ser versionadas com migrações (Flyway)
- **RNF10:** O sistema deve ter testes automatizados de unidade e de integração com banco real, rodando no pipeline a cada push
- **RNF11:** A API deve ser documentada com OpenAPI (Swagger)

### Portabilidade

- **RNF12:** O sistema deve subir com um único comando, usando Docker Compose
- **RNF13:** O pipeline deve gerar uma imagem Docker versionada a cada nova versão

### Observabilidade

- **RNF14:** Os logs devem ser estruturados e ter um identificador por requisição
- **RNF15:** O sistema deve ter um endpoint de health check

### Desempenho

- **RNF16:** A visão geral e a listagem do mês devem responder em menos de 500 ms com até 10 mil lançamentos

### Privacidade

- **RNF17:** O sistema deve guardar apenas os dados do usuário que são necessários (nome, email e senha)

## Pontos a decidir

- **Transferências entre contas:** mover dinheiro entre as minhas contas (da corrente para a poupança, por exemplo) continua acontecendo. Sem transferência, isso teria que ser lançado como uma saída e uma entrada e apareceria como gasto. Se entrarem na v1, viram novos requisitos funcionais e uma regra: transferência não conta como entrada nem como saída na visão geral.
- **Lançamento com data futura:** se for permitido registrar um pagamento que ainda vai acontecer, o saldo atual precisa ignorar o que ainda não aconteceu. A ideia é não permitir na v1, o que viraria uma nova regra de negócio.
- **Excluir a própria conta e os dados:** é um direito pela LGPD. Pode entrar já na v1 ou só quando outras pessoas forem usar o sistema.