# CUIDAS

Aplicação de literacia em saúde e segurança do doente, feita para a ULS Barcelos/Esposende no Projeto Aplicado (3.º ano ESI, IPCA, 2026/2027).

| Pasta | O que é | Tecnologia |
|---|---|---|
| `app/` | Aplicação Android do utente e do cuidador | Kotlin, Jetpack Compose, SQLite |

* O utente não tem conta. As preferências ficam só no telemóvel.

## Arrancar a app Android

1. Abrir a pasta `app/` no Android Studio e esperar pela sincronização do Gradle.
2. Correr num emulador (Android 8.0 ou superior).

### Ecrãs da app

| Ecrã | Requisitos |
|---|---|
| Boas vindas: privacidade, pergunta se quer leitura em voz alta, tamanho da letra | RF02, RF18 |
| Início: as secções em blocos quadrados iguais | RNF02 |
| Definições: letra e leitura automática | RF03, RF18 |

A leitura automática vem desligada. A app pergunta na primeira utilização e a escolha pode ser mudada nas Definições. O botão "Ouvir" está sempre no topo de cada ecrã.

## Organização do código

```
app/app/src/main/java/pt/ipca/cuidas/
  MainActivity.kt   atividade única: NavHost e a sealed class Screen (rotas)
  CuidasApp.kt      Application e Context.contentor()
  Contentor.kt      objetos partilhados (injeção de dependências manual)
  Helpers.kt        funções de extensão (datas em pt-PT, leitura de JSON)
  data/local/       preferências
  utils/            voz
  ui/theme/         Color.kt, Type.kt, Theme.kt (CuidasTheme)
  ui/components/    componentes reutilizáveis (MyTopBar, Ecra, Botoes, Cartao, Aviso, Texto, ...)
  ui/<ecrã>/        cada ecrã com XxxView.kt
```

## Pontos a confirmar com a ULSBE

* A ligação a partir da MyULSBE.
