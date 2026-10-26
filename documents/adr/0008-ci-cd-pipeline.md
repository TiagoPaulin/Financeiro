# ADR 0008: Pipeline de CI/CD com GitHub Actions

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

Quero que toda alteração seja testada automaticamente (RNF10) e que cada versão gere uma imagem Docker (RNF13). O backend e o frontend ficam no mesmo repositório (ADR 0001), e ainda não existe um lugar para implantar o sistema (ADR 0003).

Também quero aplicar práticas de DevSecOps que estudei na pós, como análise de segurança do código e das dependências, na medida certa para um projeto pessoal.

## Opções consideradas

Ferramenta de pipeline:

- **GitHub Actions**
- **GitLab CI**
- **Jenkins**
- **Azure DevOps**

Análise de segurança:

- **CodeQL:** análise estática do código (SAST), do próprio GitHub.
- **Semgrep:** análise estática do código.
- **Dependabot:** verifica vulnerabilidades e versões novas das dependências, do próprio GitHub.
- **OWASP Dependency-Check:** verifica vulnerabilidades das dependências.

## Decisão

- Usar o **GitHub Actions**.
- O workflow do backend roda **só quando algo em `backend/` muda**. Em todo push e pull request, ele compila o projeto e roda todos os testes.
- **CodeQL** analisa o código em busca de falhas de segurança.
- **Dependabot** avisa quando uma dependência tem vulnerabilidade ou versão nova, e abre um pull request com a atualização.
- Quando eu criar uma tag de versão (como `v1.0.0`), o pipeline gera a imagem Docker e publica no **GitHub Container Registry** (`ghcr.io`) com essa mesma versão.
- Trabalho em branches curtas e abro pull request para a `main`. O pull request só pode entrar com o pipeline verde.

## Justificativa

- O código já está no GitHub. O GitHub Actions, o CodeQL e o Dependabot funcionam sem nenhuma infraestrutura extra e são gratuitos para repositório público.
- Jenkins exigiria manter um servidor. GitLab CI e Azure DevOps exigiriam levar o código ou o pipeline para outra plataforma.
- O Semgrep está nas minhas anotações da pós e também atenderia, mas o CodeQL já vem integrado ao GitHub. O Dependency-Check é mais lento e faz o mesmo que o Dependabot.
- Publicar a imagem a cada versão é o Continuous Delivery do ADR 0003: a versão fica pronta para implantar, mesmo sem um ambiente de destino ainda.
- Exigir o pipeline verde no pull request mantém a `main` sempre funcionando.

## Consequências

- Quando a hospedagem for escolhida, o pipeline ganha a etapa de deploy.
- O frontend vai ter um workflow próprio, que roda só quando algo em `frontend/` muda.
- Quando o sistema for exposto na internet, vale adicionar um teste de segurança com o sistema rodando (DAST, como o OWASP ZAP) e uma verificação da imagem Docker.
- Preciso criar tags de versão seguindo o versionamento semântico (`MAJOR.MINOR.PATCH`).
