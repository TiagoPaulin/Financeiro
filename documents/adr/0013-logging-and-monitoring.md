# ADR 0013: Logs e monitoramento com Grafana e OpenTelemetry

**Status:** Proposta
**Data:** 27/10/2026

## Contexto

Os logs precisam ser estruturados e ter um identificador por requisição (RNF14), o sistema precisa de um health check (RNF15) e precisa disponibilizar métricas de tempo de resposta, requisições, erros e uso de recursos, permitindo consultar todos os logs de uma requisição pelo identificador dela (RNF18).

Quero aplicar a observabilidade que estudei na pós (logs, métricas e traces, e os golden signals), na medida certa para um sistema que roda localmente.

## Opções consideradas

- **Só logs em JSON**, com um filtro próprio gerando o identificador da requisição, e as métricas consultadas direto no Actuator.
- **Prometheus, Loki e Grafana**, cada um no seu container.
- **Grafana com a imagem `grafana/otel-lgtm`**, que roda tudo num container só e recebe os dados por OpenTelemetry.

## Decisão

- Os logs saem em JSON na saída padrão, usando o suporte do próprio Spring Boot.
- O **Spring Boot Actuator** fornece o health check e as métricas (com o Micrometer). Só os endpoints `health` e `info` ficam expostos.
- O **Micrometer Tracing** gera um `traceId` para cada requisição. Ele aparece em todas as linhas de log daquela requisição e volta nas respostas de erro.
- A API envia métricas, logs e traces por **OpenTelemetry** (OTLP) para o `grafana/otel-lgtm`.
- O Grafana fica num **profile** do Docker Compose chamado `observability`:
  - `docker compose up` sobe só a API, o banco e o Mailpit;
  - `docker compose --profile observability up` sobe também o Grafana, na porta 3000.
- Os painéis mostram os golden signals: latência, tráfego, erros e saturação.
- Os logs nunca levam senhas, tokens, o corpo das requisições nem os valores e descrições dos lançamentos (RNF04).

## Justificativa

- O filtro próprio atende o RNF14, mas não entrega métricas em painéis nem liga os logs aos traces, que é o que o RNF18 pede.
- Prometheus, Loki e Grafana separados ficam mais parecidos com um ambiente de produção, mas são mais containers e configurações para um sistema local com um usuário.
- O `otel-lgtm` entrega tudo num container só, e o Spring Boot já tem suporte a ele.
- O OpenTelemetry é o padrão de mercado. Se um dia eu trocar o Grafana por outra ferramenta, a aplicação continua enviando os dados do mesmo jeito.
- Com o `traceId`, consigo ir de uma linha de log para a requisição inteira e ver onde ela gastou tempo, inclusive em cada consulta ao banco.
- Deixar o Grafana num profile mantém o dia a dia leve, e o sistema continua subindo com um único comando (RNF12).

## Consequências

- O `otel-lgtm` é feito para desenvolvimento e demonstração, não para produção. Quando houver hospedagem, o monitoramento precisa ser revisto num novo ADR.
- OpenTelemetry e Grafana são novos para mim. Eles entram logo depois do esqueleto do projeto, antes das funcionalidades.
- As dependências e propriedades exatas vão ser conferidas na documentação do Spring Boot 4.1 na hora de implementar.
- Expor outros endpoints do Actuator além do `health` e do `info` vazaria configurações e dados, então eles ficam fechados.
- Alertas e rastreamento de erros, como o Sentry, ficam para quando o sistema for hospedado.
