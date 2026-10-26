# Diagrama de contexto (C4 nível 1)

Mostra o sistema como uma caixa só: quem usa e com quais sistemas externos ele conversa.

```mermaid
flowchart LR
    usuario["<b>Usuário</b><br/>[Pessoa]<br/><br/>Pessoa que controla<br/>as próprias finanças"]
    financeiro["<b>Financeiro</b><br/>[Sistema]<br/><br/>Registra entradas e saídas<br/>e mostra gastos, saldos<br/>e a visão geral do mês"]
    email["<b>Serviço de email</b><br/>[Sistema externo]<br/><br/>Envia o email de<br/>redefinição de senha"]

    usuario -- "Registra lançamentos e<br/>consulta a visão geral" --> financeiro
    financeiro -- "Envia o link de redefinição<br/>de senha [SMTP]" --> email
    email -. "Entrega o email" .-> usuario

    classDef person fill:#08427b,stroke:#052e56,color:#fff
    classDef system fill:#1168bd,stroke:#0b4884,color:#fff
    classDef external fill:#999999,stroke:#6b6b6b,color:#fff
    class usuario person
    class financeiro system
    class email external
```

**Legenda:** azul-escuro é pessoa, azul é o meu sistema e cinza é sistema externo.

## O que o diagrama mostra

- **Usuário:** quem usa o sistema para registrar lançamentos e acompanhar gastos e saldos. Na v1 sou só eu, mas o sistema já é multiusuário (ADR 0002).
- **Financeiro:** o sistema inteiro, visto de fora. As partes de dentro (frontend, API e banco de dados) aparecem no [diagrama de containers](c4-containers.md).
- **Serviço de email:** usado para enviar o link de redefinição de senha (RF05). Na v1 é o Mailpit, que roda localmente e só mostra os emails numa página, sem enviar de verdade (ADR 0010).

## O que fica de fora

- **Bancos:** não existe integração com banco. Os lançamentos são registrados à mão, conforme o escopo.
- **Serviço de cotações:** só entra com os investimentos, na v2. Quando entrar, vira um novo sistema externo neste diagrama.
