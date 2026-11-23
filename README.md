# CUIDAS

**Cuidar da Utilização da Informação para Decidir e Agir em Saúde**

Aplicação móvel de literacia em saúde para utentes e cuidadores da ULS Barcelos/Esposende (ULSBE), com foco em pessoas idosas e doentes crónicos.

> Projeto Aplicado · 3.º Ano ESI · 2026/2027
> <br> Escola Superior de Tecnologia do IPCA
> <br>Entidade parceira: ULS Barcelos/Esposende, E.P.E. (ULSBE)

---

## Equipa

| Nome | N.º |
|---|---|
| Diogo Silva | 31504 |
| Gonçalo Rodrigues | 31507 |
| Rui Barbosa | 31509 |

---

## Problema

Em Portugal, 55,8% da população tem um nível de literacia problemático ou inadequado na dimensão dos cuidados de saúde (Pedro et al., 2016). As aplicações existentes (MyHSMM, MyTherapy, Medisafe) facilitam o acesso à informação e o cumprimento de horários, mas **não ajudam o utilizador a perceber o que lhe foi prescrito** nem a usar a medicação em segurança.

## Solução

A CUIDAS ajuda o utilizador a perceber a sua medicação e **confirma essa compreensão** com perguntas simples. Quando o utilizador erra, a aplicação explica de outra forma; se a dúvida persistir, assinala o tema como "ainda não percebi" (apenas no telemóvel do utilizador) para ser esclarecido na consulta.

### Princípios do sistema

1. **Conteúdo clínico fechado e pré-aprovado.** Só mostra informação de fontes oficiais (DGS, SNS 24, INFARMED, Ordem dos Farmacêuticos), revista por profissionais de saúde. Não gera respostas livres nem diagnósticos.
2. **Privacidade por desenho.** Sem conta e sem recolha de dados pessoais ou de saúde. Os dados do utilizador ficam apenas no telemóvel. Os indicadores são sempre anónimos e agregados.

---


## Funcionalidades

### App do utilizador
- **Minha medicação:** dose, horário e para que serve, com texto simples, pictogramas e áudio em português; inserção por QR code da prescrição ou manual.
- **Perguntas de confirmação:** perguntas gerais de segurança, com explicação alternativa em caso de erro.
- **Lembretes e registo de tomas.**
- **Perguntas do dia a dia:** árvore de perguntas que termina sempre em *ficar em casa*, *ligar SNS 24* ou *ligar 112*.
- **Dicionário simples** e **sinais de alerta.**
- **Modo acessível e cuidador:** letra grande, áudio, botões grandes e poucos ecrãs.
- **Apagar todos os dados** do telemóvel numa só ação.

### Área web (profissionais da ULSBE)
- **Backoffice de conteúdos:** edição de textos, imagens, áudios e questionários sem programação, com perfis de editor/aprovador e histórico de versões.
- **Painel de indicadores:** instalações, módulos consultados, resultados agregados e satisfação, exportável para Excel.

### Fora do âmbito (fases seguintes)

 Identificação do utente, infeções, cirurgia segura, prevenção de quedas, segurança das crianças internadas e vacinação. A IA na app do utilizador também não está prevista na versão 1.

## Arquitetura

O sistema divide-se em três camadas:

- **Interfaces:** app do utilizador (também usada pelo cuidador), painel de indicadores e backoffice de conteúdos.
- **Lógica:** orienta o utilizador.
- **Dados:** base de dados de conteúdos validados, gestão de acessos (só profissionais) e recolha de indicadores anónimos.

Integrações:
- **MyULSBE:** abre a CUIDAS por ligação direta, sem partilha de dados pessoais (a confirmar com a Informática da ULSBE).
- **SNS 24 / 112:** apenas destinos de encaminhamento; a app não comunica diretamente com eles.



## Requisitos

Resumo (detalhe na proposta, secções 5 e 6):

- **26 requisitos funcionais** (RF01–RF26), com prioridade *Essencial* ou *Desejável*.
- **14 requisitos não funcionais** (RNF01–RNF14), entre os quais:
  - Contraste WCAG 2.1 AA e alvos de toque de pelo menos 48 × 48 dp
  - Tarefas-chave a no máximo 3 toques do ecrã inicial
  - Funcionamento offline das funções essenciais
  - Resposta abaixo de 2 s
  - Android e iOS, incluindo gama média e baixa
  - Conformidade com o RGPD


## Validação do sucesso

1. **Teste de usabilidade** com 5 a 8 idosos e cuidadores.
2. **Validação mais longa** proposta à ULSBE: 8 a 12 semanas, cerca de 30 a 50 utentes (valores a ajustar).

| Indicador | Meta |
|---|---|
| Literacia em saúde (HLS-EU, 16 itens, escala 0–50) | Média do grupo ≥ 33 |
| Perguntas gerais | ≥ 80% dos utilizadores acertam ≥ 80% ao fim de 2 semanas |
| Adesão à terapêutica | ≥ 85% de tomas confirmadas |
| Perguntas do dia a dia | 100% nos casos de alerta; ≥ 95% nos restantes |
| Uso autónomo | ≥ 70% |
| Segurança | Zero respostas incorretas ou inseguras |

---

## Pontos em aberto

- Validar com a ULSBE os percursos das "Perguntas do dia a dia" e o conjunto de perguntas gerais.
- Confirmar com a Informática da ULSBE a ligação a partir da MyULSBE (RF04, RNF13).
- Analisar a viabilidade da IA como extensão futura (sugestão de artigos, análise de resultados, apoio aos questionários).
- Ajustar, no teste de usabilidade, os valores propostos (metas, número de tentativas, duração).

---

## Stack tecnológica

*A definir.*

## Estrutura do repositório

*A definir.*

## Documentação

- `Proposta_de_Sistema_Grupo13_CUIDAS.pdf`: proposta de sistema completa (novembro de 2026).

## Referências principais

- HLS-EU Consortium (2012). *Comparative report on health literacy in eight EU member states.*
- Pedro, A. R., Amaral, O., & Escoval, A. (2016). Literacia em saúde, dos dados à ação. *Revista Portuguesa de Saúde Pública, 34*(3), 259–275.
- Organização Mundial da Saúde (2002). *Active ageing: A policy framework.*

---

*Esta aplicação não substitui um profissional de saúde.*