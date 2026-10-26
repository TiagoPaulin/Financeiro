# ADR 0003: Rodar localmente e deixar a hospedagem para depois

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

Ainda não tenho onde hospedar o sistema e não quero gastar com isso agora. Mesmo assim, quero ter um pipeline de CI/CD desde o início e deixar o sistema pronto para ser hospedado quando eu decidir onde.

## Opções consideradas

- **Escolher agora um serviço de hospedagem gratuito.**
- **Rodar o sistema na minha máquina com Docker** e deixar a escolha da hospedagem para quando ela for necessária.

## Decisão

A v1 roda na minha máquina com Docker Compose. O pipeline roda os testes e gera uma imagem Docker versionada a cada nova versão, pronta para ser implantada quando houver onde.

A escolha da hospedagem vai ser registrada num ADR futuro.

## Justificativa

- Custo zero.
- Serviços gratuitos costumam ter limites, como desligar a aplicação por inatividade ou apagar o banco depois de um tempo. Para dados financeiros, isso é um risco.
- Escolher agora seria decidir sem saber ainda o tamanho real do sistema e do que ele vai precisar.
- Se o sistema subir com um comando (RNF12), gerar uma imagem versionada (RNF13) e receber as configurações por variáveis de ambiente (RNF05), hospedar depois vai ser só rodar a mesma imagem em outro lugar.

## Consequências

- O pipeline faz **Continuous Delivery** (deixa a versão pronta para implantar), e não **Continuous Deployment** (implantar automaticamente), porque ainda não existe um ambiente de destino.
- Só consigo usar o sistema no computador onde ele está rodando.
- O backup do banco é minha responsabilidade desde já, porque os dados ficam na minha máquina (RNF08).
- Quando a hospedagem for escolhida, um novo ADR registra a decisão e o pipeline ganha a etapa de deploy.
