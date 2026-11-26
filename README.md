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
| `backoffice/` | Área web dos profissionais da ULSBE e API de conteúdos | Java 17, Spring Boot 3, Thymeleaf, MySQL |
| `docker-compose.yml` | Base de dados MySQL pronta a usar | Docker |

## Como o sistema funciona

```
 App Android (SQLite)  <── conteúdos aprovados (só o que mudou) ──  Backoffice Spring Boot (MySQL)  <── editores e aprovadores ULSBE
                       ── contagens anónimas ──>
```

* O utente não tem conta. Medicação, fotografias, tomas, respostas, dúvidas e contactos ficam só no SQLite do telemóvel.
* A app traz um pacote inicial de conteúdos (`app/app/src/main/assets/conteudo_inicial.json`). Na primeira utilização copia esse pacote para o SQLite, incluindo as perguntas do dia a dia, e funciona sem internet.
* Cada conteúdo fica guardado com a sua versão. Sempre que a app abre, pergunta a versão geral ao servidor; se mudou, pede o índice (id e versão de cada item) e descarrega só os itens novos ou alterados. Os itens retirados são apagados.
* Para o servidor vão só eventos anónimos (tipo, conteúdo e dia). O identificador aleatório da instalação (RF02) só serve para contar instalações, e o servidor guarda apenas o seu resumo (SHA 256).
* Só os profissionais iniciam sessão, na área web.

## Identidade visual

* Fundo e superfícies `#EDEDED`, destaque e ações `#00A0EA`.
* Texto escuro sobre o azul (o branco não chega ao contraste mínimo de 4,5:1).
* Sem gradientes e sem emojis. Ícones simples do Material, todos do mesmo estilo.
* O vermelho só aparece no 112 e nos botões de apagar.
* O backoffice usa as mesmas cores e funciona no telemóvel (menu que abre e fecha, tabelas com scroll horizontal, botões com 44 px de altura).

## 1. Arrancar o backoffice

### Sem MySQL (modo de demonstração)

No IntelliJ, em Run > Edit Configurations, escrever `demo` em Active profiles e correr `CuidasBackofficeApplication`. Usa uma base de dados H2 guardada na pasta `backoffice/dados-demo/`.

### Com MySQL

```bash
docker compose up -d
```

Cria a base de dados `cuidas` com o utilizador `cuidas` e a palavra passe `cuidas`. Sem Docker, basta um MySQL 8 local com esses dados, ou mudar as variáveis `CUIDAS_DB_URL`, `CUIDAS_DB_USER` e `CUIDAS_DB_PASSWORD`.

Abre em http://localhost:8080. No primeiro arranque são criados:

* três profissionais de demonstração, todos com a palavra passe `cuidas123`: `editor@cuidas.pt` (editor), `aprovador@cuidas.pt` (aprovador) e `admin@cuidas.pt` (os dois perfis);
* os conteúdos de exemplo de `backoffice/src/main/resources/conteudo-inicial.json`, já publicados.

**Os conteúdos de exemplo têm de ser revistos e aprovados pela ULSBE antes de qualquer uso real.**

Os testes usam H2 em memória e não precisam de MySQL.

### O que se faz no backoffice

* **Conteúdos:** artigos dos módulos, perguntas de compreensão, termos do dicionário, percursos do dia a dia e sinais de alerta (RF21).
* **Ligações oficiais:** cada artigo pode ter até 3 ligações. Só são aceites endereços HTTPS de `gov.pt`, `min-saude.pt`, `dgs.pt` e `infarmed.pt` (por exemplo SNS 24, Portal do SNS, DGS e INFARMED).
* **Fluxo de aprovação (RF22):** o editor envia para aprovação e outro profissional aprova ou rejeita. Ninguém aprova uma versão escrita por si.
* **Histórico de versões (RF23)**, **painel de indicadores (RF24, RF25)** com exportação para Excel, e **códigos QR** de artigos e de receitas de teste.

### API usada pela app

| Pedido | Para quê |
|---|---|
| `GET /api/v1/conteudos/versao` | Versão geral dos conteúdos publicados |
| `GET /api/v1/conteudos/indice` | Id, tipo e versão de cada conteúdo publicado |
| `GET /api/v1/conteudos/itens?ids=1,2,3` | Dados completos só dos itens pedidos (até 500) |
| `GET /api/v1/conteudos` | Pacote completo (mantido para versões antigas da app) |
| `POST /api/v1/instalacoes` | Regista uma instalação: `{"identificador": "uuid"}` |
| `POST /api/v1/eventos` | Eventos anónimos: `{"eventos": [{"tipo": "RESPOSTA", "conteudoId": 9, "valor": "CERTA", "dia": "2026-11-09"}]}` |

### Perguntas ligadas a artigos

Cada pergunta de compreensão pertence a um artigo, escolhido no formulário da pergunta (web ou app). Na app, a pergunta aparece em "Ver se percebi" desse artigo. Se a base de dados de demonstração foi criada antes desta versão, apagar a pasta `backoffice/dados-demo` para os conteúdos de exemplo serem carregados de novo já com as ligações.

### Modo debug

Com o perfil `demo` (`cuidas.debug.ativo=true`), o ecrã de entrada do backoffice na app tem o interruptor "Modo debug": entra sem palavra passe como "Modo debug", que pode editar e aprovar (continua a não poder aprovar versões escritas por si). Em produção a propriedade fica desligada e o cabeçalho `X-Cuidas-Debug` é ignorado.

### API profissional (área do backoffice dentro da app)

Usa as mesmas contas da área web, enviadas em cada pedido com HTTP Basic (em produção, sempre sobre HTTPS). A app não guarda a palavra passe: fica só em memória e a sessão acaba quando a app fecha. As regras são as mesmas da área web (só o editor edita, só o aprovador aprova, ninguém aprova uma versão escrita por si).

| Pedido | Para quê |
|---|---|
| `GET /api/v1/pro/eu`, `/listas`, `/resumo` | Profissional com sessão, listas de tipos, módulos e ações, números do painel |
| `GET /api/v1/pro/conteudos?tipo=` | Lista dos conteúdos |
| `POST /api/v1/pro/conteudos` | Criar um conteúdo (rascunho) |
| `GET /api/v1/pro/conteudos/{id}` | Versão publicada, versão em curso e histórico |
| `POST /api/v1/pro/conteudos/{id}/editar`, `/retirar`, `/reativar` | Abrir uma versão para editar, retirar ou voltar a mostrar |
| `GET` e `PUT /api/v1/pro/versoes/{id}`, `POST .../submeter`, `.../repor` | Ler, guardar, submeter e repor uma versão |
| `GET /api/v1/pro/aprovacoes`, `/aprovacoes/{id}`, `POST .../aprovar`, `.../rejeitar` | Aprovações |
| `GET /api/v1/pro/indicadores?inicio=&fim=` | Indicadores anónimos do período |

## 2. Arrancar a app Android

1. Abrir a pasta `app/` no Android Studio e esperar pela sincronização do Gradle.
2. Correr num emulador (Android 8.0 ou superior).
3. No emulador, a app fala com o backoffice em `http://10.0.2.2:8080/`. Num telemóvel real, mudar `API_URL` em `app/app/build.gradle.kts` para o IP do computador na rede e acrescentar esse IP em `res/xml/network_security_config.xml`.

A app funciona mesmo sem o backoffice ligado, com os conteúdos iniciais.

### Ecrãs da app

| Ecrã | Requisitos |
|---|---|
| Boas vindas: privacidade, pergunta se quer leitura em voz alta, tamanho da letra | RF02, RF18 |
| Início: registo dos últimos 7 dias (completo, incompleto ou sem tomas), próxima toma com "Já tomei", botão "Mostrar todos os medicamentos" e as secções em blocos quadrados iguais | RNF02, RF11 |
| A minha medicação: só as tomas de hoje ("Por tomar" e "Já tomei") e os medicamentos sem horário fixo | RF05, RF11 |
| Todos os medicamentos: em curso e terminados, com o botão "Adicionar medicamento" e a quantidade à direita de cada um | RF05 |
| Adicionar medicamento: leitura do QR da receita ou escrita à mão, para que serve, horas, data de término, fotografia | RF06, RF19 |
| Histórico: "X de Y dias completos" nos últimos 30 dias; cada dia abre a lista dos medicamentos tomados e a hora | RF11 |
| Lembretes: notificação com som, vibração e luz, botão "Já tomei", param depois da data de término | RF10, RF11 |
| Ver se percebi (dentro de cada artigo, só com as perguntas desse artigo) e Ainda não percebi | RF07, RF08, RF09 |
| Perguntas do dia a dia: percursos de sim ou não que acabam em casa, SNS 24 ou 112 | RF12 a RF15 |
| Sinais de alerta: cada cartão tem o seu botão de chamada, com o número lido do SQLite | RF17 |
| Contactos importantes: médico, farmácia, familiares e serviços de saúde, com chamada direta | RF15 |
| Aprender: um separador por tema, artigos com ligações para sites oficiais e o botão "Ver se percebi" | RF20 |
| Dicionário em toda a app: qualquer palavra do dicionário aparece sublinhada e, ao tocar, abre o significado | RF16 |
| Acompanhar (cuidador), só se o utente autorizar | RF19 |
| Definições: letra, leitura automática, cuidador, atualizar conteúdos, satisfação, apagar todos os dados, aceder ao backoffice | RF03, RF18 |
| Backoffice no telemóvel: entrar, painel, conteúdos (criar, editar, submeter, retirar, repor), aprovações e indicadores, pela API profissional | RF01, RF21 a RF25 |

A leitura automática vem desligada. A app pergunta na primeira utilização e a escolha pode ser mudada nas Definições. O botão "Ouvir" está sempre no topo de cada ecrã.

### Códigos QR e ligações diretas (RF04, RF06)

| Texto do QR | O que faz |
|---|---|
| `cuidas://artigo/7` | Abre o artigo 7 |
| `cuidas://receita?n=Ramipril&d=1%20comprimido&h=08:00,20:00&p=tensao&f=2026-12-31` | Abre "Adicionar medicamento" já preenchido (`f` é a data de término, opcional) |
| `{"tipo":"receita","medicamentos":[...]}` | Formato da primeira versão, ainda aceite |
| `cuidas://abrir` | Abre a app (botão na MyULSBE) |

O QR pode ser lido no ecrã "Adicionar medicamento" ou com a câmara do telemóvel. `p` aceita `tensao`, `diabetes`, `coracao`, `colesterol`, `dor`, `estomago`, `sono` e `outro`. O formato real das receitas tem de ser confirmado com a SPMS; só `utils/LeituraQr.kt` precisa de mudar.

## Organização do código

```
app/app/src/main/java/pt/ipca/cuidas/
  MainActivity.kt   atividade única: NavHost com todos os ecrãs e a sealed class Screen (rotas)
  CuidasApp.kt      Application e Context.contentor()
  Contentor.kt      repositórios partilhados (injeção de dependências manual)
  Helpers.kt        funções de extensão (datas em pt-PT, leitura de JSON)
  models/           data classes (Medicamento, Contacto, Conteudos, RegistoDia, ...; backoffice/ para a API profissional)
  data/local/       SQLite (CuidasBd, Esquema, Migracoes), preferências e pacote inicial
  data/remote/      HttpJson e as APIs (conteúdos, indicadores, backoffice)
  data/repository/  repositórios usados pelos ViewModels (medicamentos, tomas, conteúdos, sessão do backoffice, ...)
  lembretes/        alarmes e notificações das tomas
  utils/            fotos, chamadas, sites oficiais, voz e leitura de QR
  ui/theme/         Color.kt, Type.kt, Theme.kt (CuidasTheme)
  ui/components/    componentes reutilizáveis (MyTopBar, Ecra, Botoes, Cartao, Aviso, Texto, ...)
  ui/<ecrã>/        cada ecrã com XxxView.kt (Compose) e XxxViewModel.kt (XxxViewState + StateFlow):
                    home, medicamentos (+ form), historico, contactos, sinaisalerta, diaadia, aprender,
                    compreensao, dicionario, cuidador, definicoes, boasvindas, backoffice (+ components)

backoffice/src/main/java/pt/ipca/cuidas/backoffice/
  app/        segurança e dados iniciais
  core/       profissionais e regras de negócio comuns
  features/   conteudos, aprovacoes, sync, indicadores, qrcode
```

## Base de dados local (SQLite, versão 2)

| Tabela | Conteúdo |
|---|---|
| `medicamento` | nome, quantidade por toma, para que serve, fotografia, data de início, data de término |
| `horario` | horas de cada medicamento |
| `toma` | tomas confirmadas (uma por horário e por dia) |
| `contacto` | contactos importantes; o SNS 24 e o 112 têm chave fixa e não se podem apagar |
| `conteudo` | cada conteúdo aprovado com a sua versão |
| `meta` | versão geral dos conteúdos |
| `resposta`, `duvida` | resultados das perguntas e temas "ainda não percebi" |
| `evento_pendente` | eventos anónimos à espera de internet |

Quem já tinha a versão 1 instalada não perde dados: a migração junta as datas e as tabelas novas.

## Base de dados do backoffice (MySQL)

| Tabela | Conteúdo |
|---|---|
| `profissional` | contas dos profissionais (palavra passe cifrada com BCrypt) |
| `conteudo` | cada conteúdo e a versão publicada |
| `versao_conteudo`, `versao_opcao`, `versao_passo`, `versao_ligacao` | versões, respostas, passos e ligações oficiais |
| `instalacao` | resumo do identificador de cada instalação |
| `evento` | eventos anónimos, só com o dia |

## Pontos a confirmar com a ULSBE

* Os conteúdos, as perguntas e os percursos de exemplo.
* O número de tentativas antes de "ainda não percebi" (agora 2, em `ui/compreensao/PerguntasViewModel.kt`).
* O formato do QR das receitas (SPMS).
* A ligação a partir da MyULSBE.
* O alojamento do backoffice (UE ou infraestrutura da ULSBE/SPMS) com HTTPS.

## Documentação

* `Proposta_de_Sistema_Grupo13_CUIDAS.pdf`: proposta de sistema completa, com o problema, os requisitos (RF01 a RF26, RNF01 a RNF14), a validação e as referências.

---

*Esta aplicação não substitui um profissional de saúde.*
