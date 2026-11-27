package pt.ipca.cuidas.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import pt.ipca.cuidas.ui.theme.Cores

// Campo de texto com fundo branco e letra grande; `inicio` mostra algo antes do texto (ex.: o indicativo do país).
@Composable
fun CampoTexto(
    valor: String,
    aoMudar: (String) -> Unit,
    rotulo: String,
    teclado: KeyboardType = KeyboardType.Text,
    erro: String? = null,
    oculto: Boolean = false,
    linhas: Int = 1,
    inicio: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        label = { Text(rotulo) },
        leadingIcon = inicio,
        textStyle = MaterialTheme.typography.bodyLarge,
        singleLine = linhas == 1,
        minLines = linhas,
        visualTransformation = if (oculto) PasswordVisualTransformation() else VisualTransformation.None,
        isError = erro != null,
        supportingText = erro?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Cores.Campo,
            unfocusedContainerColor = Cores.Campo,
            errorContainerColor = Cores.Campo,
            focusedBorderColor = Cores.Destaque,
        ),
    )
}
