// Ficheiro de build do projeto: só declara os plugins; cada módulo aplica os que usa.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
