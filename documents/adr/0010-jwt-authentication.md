# ADR 0010: Autenticação com JWT

**Status:** Aceita
**Data:** 26/10/2026

## Contexto

A v1 precisa de cadastro, login e logout (RF01 a RF03), de alteração dos dados informando a senha atual (RF04) e de redefinição de senha por email para quem não está logado (RF05). Todo o resto exige autenticação (RNF02).

Já implementei autenticação com JWT e refresh token em outros projetos. Conheço bem esse modelo.

## Opções consideradas

- **JWT com refresh token.**
- **Sessão do Spring Security com cookie.**
- **Um provedor de identidade**, como Keycloak ou Auth0.

## Decisão

- Usar **JWT**: um access token de curta duração (15 minutos) e um refresh token guardado no banco, que permite pegar um novo access token sem fazer login de novo.
- Corrigir as limitações que encontrei no springauth:
  - o refresh token é trocado a cada uso: cada renovação gera um refresh token novo e invalida o anterior;
  - os refresh tokens e os tokens de redefinição de senha são guardados no banco como hash, e não em texto;
  - a cada requisição, o usuário do token é buscado no banco e precisa existir.
- O logout revoga o refresh token daquela sessão. Trocar ou redefinir a senha revoga todos os refresh tokens do usuário.
- Senhas guardadas com BCrypt (RNF01), com no mínimo 8 caracteres (RN03).
- **Redefinição de senha (RF05):** o usuário recebe por email um link com um token de uso único, que expira em 30 minutos. A resposta é sempre a mesma, exista o email ou não. Na v1, os emails são enviados para o Mailpit, que roda no Docker Compose e mostra os emails numa página local.
- Tudo isso fica no módulo `identity` (ADR 0004), reaproveitando o que fiz no springauth e adaptando para o Spring Boot 4 (ADR 0006).

## Justificativa

- Já domino o JWT: consigo implementar, manter cada parte. A maturidade do time é um dos critérios para escolher uma solução.
- A API valida o token sem precisar guardar sessão, o que facilita testar pelo Swagger ou pelo curl e permite que outro cliente, como um app, use a mesma API no futuro.
- As melhorias corrigem exatamente as limitações que eu mesmo encontrei no springauth.
- A sessão com cookie tem vantagens para um frontend no mesmo endereço da API, como o logout na hora e o cookie que o JavaScript não consegue ler. Mas é um modelo que eu ainda não usei, com proteção contra CSRF e armazenamento de sessão. Fica como alternativa para estudar no futuro.
- Um provedor de identidade seria mais um serviço para rodar e configurar, o que é over-engineering para um sistema que roda localmente com um usuário.

## Consequências

- O access token não pode ser cancelado antes de expirar. Por isso a validade é curta: depois de um logout, ele continua valendo por no máximo 15 minutos.
- Como o token vai no cabeçalho `Authorization` e não num cookie, a proteção contra CSRF não é necessária.
- Ainda preciso decidir, quando o frontend começar, onde ele guarda os tokens:
  - no `localStorage`, que é mais simples, mas deixa os tokens expostos se alguém conseguir injetar um script na página (XSS);
  - ou o access token só na memória e o refresh token num cookie `HttpOnly`, que o JavaScript não consegue ler. É mais seguro e dá um pouco mais de trabalho.
- A v1 já precisa de envio de email por causa do RF05. Por isso o Mailpit entra no Docker Compose.
- Limitar tentativas de login contra força bruta fica para quando o sistema for exposto na internet.
