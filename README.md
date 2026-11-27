# CUIDAS

**Cuidar da Utilização da Informação para Decidir e Agir em Saúde**

Aplicação móvel de literacia em saúde para utentes e cuidadores da ULS Barcelos/Esposende (ULSBE), com foco em pessoas idosas e doentes crónicos.

> Projeto Aplicado · 3.º Ano ESI · 2026/2027
> <br>Escola Superior de Tecnologia do IPCA
> <br>Entidade parceira: ULS Barcelos/Esposende, E.P.E. (ULSBE)

## Equipa

| Nome | N.º |
|---|---|
| Diogo Silva | 31504 |
| Gonçalo Rodrigues | 31507 |
| Rui Barbosa | 31509 |

## O que faz

A CUIDAS ajuda o utilizador a perceber a sua medicação e **confirma essa compreensão** com perguntas simples. Quando o utilizador erra, a aplicação explica de outra forma; se a dúvida persistir, assinala o tema como "ainda não percebi" (só no telemóvel do utilizador) para ser esclarecido na consulta.

### Princípios

1. **Conteúdo clínico fechado e pré-aprovado.** Só mostra informação de fontes oficiais (DGS, SNS 24, INFARMED, Ordem dos Farmacêuticos), revista por profissionais de saúde. Não gera respostas livres nem diagnósticos.
2. **Privacidade por desenho.** Sem conta e sem recolha de dados pessoais ou de saúde. Os dados do utilizador ficam só no telemóvel. Os indicadores são sempre anónimos e agregados.

### Funcionalidades

* **App do utilizador:** a minha medicação (dose, horário e para que serve, com texto simples, pictogramas e áudio), inserção por QR da receita ou à mão, lembretes e registo de tomas, perguntas de confirmação, perguntas do dia a dia (terminam sempre em ficar em casa, ligar SNS 24 ou ligar 112), dicionário, sinais de alerta, modo acessível e cuidador, e apagar todos os dados numa só ação.
* **Área web e backoffice na app (profissionais da ULSBE):** edição de conteúdos sem programação, perfis de editor e aprovador, histórico de versões e painel de indicadores exportável para Excel.
* **Integrações:** a MyULSBE abre a CUIDAS por ligação direta, sem partilha de dados pessoais (a confirmar com a Informática da ULSBE). O SNS 24 e o 112 são só destinos de encaminhamento; a app não comunica com eles.

## Stack e estrutura do repositório

| Pasta | O que é | Tecnologia |
|---|---|---|
| `app/` | Aplicação Android do utente e do cuidador | Kotlin, Jetpack Compose, SQLite |

* O utente não tem conta. Medicação, fotografias, tomas e contactos ficam só no SQLite do telemóvel.

## Identidade visual

* Fundo e superfícies `#EDEDED`, destaque e ações `#00A0EA`.
* Texto escuro sobre o azul.
* O vermelho só aparece no 112 e nos botões de apagar.

## Arrancar a app Android

1. Abrir a pasta `app/` no Android Studio e esperar pela sincronização do Gradle.
2. Correr num emulador (Android 8.0 ou superior).

### Ecrãs da app

| Ecrã | Requisitos |
|---|---|
| Boas vindas: privacidade, pergunta se quer leitura em voz alta, tamanho da letra | RF02, RF18 |
| Início: as secções em blocos quadrados iguais | RNF02 |
| Início: próxima toma com "Já tomei" e botão "Mostrar todos os medicamentos" | RF11 |
| A minha medicação: só as tomas de hoje ("Por tomar" e "Já tomei") e os medicamentos sem horário fixo | RF05, RF11 |
| Todos os medicamentos: em curso e terminados, com o botão "Adicionar medicamento" | RF05 |
| Adicionar medicamento: leitura do QR da receita ou escrita à mão, para que serve, horas, data de término, fotografia | RF06, RF19 |
| Lembretes: notificação com som, vibração e luz, botão "Já tomei", param depois da data de término | RF10, RF11 |
| Início: registo dos últimos 7 dias (completo, incompleto ou sem tomas) | RF11 |
| Histórico: "X de Y dias completos" nos últimos 30 dias; cada dia abre a lista dos medicamentos tomados e a hora | RF11 |
| Contactos importantes: médico, farmácia, familiares e serviços de saúde, com chamada direta; o número leva a bandeira e o indicativo do país (🇵🇹 +351 por omissão) | RF15 |
| Definições: letra, leitura automática, notificações dos lembretes e apagar todos os dados | RF03, RF18 |

A leitura automática vem desligada. A app pergunta na primeira utilização e a escolha pode ser mudada nas Definições. O botão "Ouvir" está sempre no topo de cada ecrã.

### Códigos QR e ligações diretas (RF04, RF06)

| Texto do QR | O que faz |
|---|---|
| `cuidas://receita?n=Ramipril&d=1%20comprimido&h=08:00,20:00&p=tensao&f=2026-12-31` | Abre "Adicionar medicamento" já preenchido (`f` é a data de término, opcional) |
| Receita eletrónica do SNS (Base64 + gzip) | Lê cada linha `LRC`: código CNPEM, quantidade, horas e duração a partir da posologia |
| `{"tipo":"receita","medicamentos":[...]}` | Formato JSON, vários medicamentos no mesmo código |
| `cuidas://abrir` | Abre a app (botão na MyULSBE) |

`p` aceita `tensao`, `diabetes`, `coracao`, `colesterol`, `dor`, `estomago`, `sono` e `outro`. Se o formato das receitas mudar, só `utils/LeituraQr.kt` precisa de mudar.

## Organização do código

```
app/app/src/main/java/pt/ipca/cuidas/
  MainActivity.kt   atividade única: NavHost e a sealed class Screen (rotas)
  CuidasApp.kt      Application: inicia o Contentor uma vez ao arrancar
  Contentor.kt      object com os repositórios partilhados; os ViewModels usam Contentor.xxx
  Helpers.kt        funções de extensão (datas em pt-PT, leitura de JSON)
  models/           data classes (Medicamento, Contacto, RegistoDia, ...)
  data/local/       SQLite (CuidasBd, Esquema, Migracoes) e preferências
  data/repository/  repositórios usados pelos ViewModels (medicamentos, tomas, registo dos dias, contactos)
  lembretes/        alarmes e notificações das tomas
  utils/            fotos, chamadas, voz e leitura de QR
  ui/theme/         Color.kt, Type.kt, Theme.kt (CuidasTheme)
  ui/components/    componentes reutilizáveis (MyTopBar, Ecra, Botoes, Cartao, Aviso, Texto, ...)
  ui/<ecrã>/        cada ecrã com XxxView.kt (Compose) e XxxViewModel.kt (XxxViewState + StateFlow):
                    boasvindas, home, medicamentos (+ form), historico, contactos, definicoes
```

## Base de dados local (SQLite, versão 1)

| Tabela | Conteúdo |
|---|---|
| `medicamento` | nome, quantidade por toma, para que serve, fotografia, data de início, data de término |
| `horario` | horas de cada medicamento |
| `toma` | tomas confirmadas (uma por horário e por dia) |
| `contacto` | contactos importantes; o SNS 24 e o 112 têm chave fixa e não se podem apagar |

## Pontos a confirmar com a ULSBE

* O formato do QR das receitas (SPMS).
* A ligação a partir da MyULSBE.

## Documentação

* `Proposta_de_Sistema_Grupo13_CUIDAS.pdf`: proposta de sistema completa, com o problema, os requisitos (RF01 a RF26, RNF01 a RNF14), a validação e as referências.

---

*Esta aplicação não substitui um profissional de saúde.*
