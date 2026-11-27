package pt.ipca.cuidas.ui.medicamentos

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ipca.cuidas.ui.components.Icones
import pt.ipca.cuidas.ui.theme.Cores
import pt.ipca.cuidas.utils.Fotos

// Fotografia da embalagem ou, sem fotografia, o ícone do medicamento.
@Composable
fun MiniaturaMedicamento(foto: String?, nome: String, tamanho: Dp = 64.dp) {
    val imagem by produceState<Bitmap?>(null, foto) {
        value = withContext(Dispatchers.IO) { Fotos.miniatura(foto) }
    }
    val forma = RoundedCornerShape(10.dp)
    val atual = imagem
    if (atual != null) {
        Image(
            bitmap = atual.asImageBitmap(),
            contentDescription = "Fotografia da embalagem de $nome",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(tamanho).clip(forma),
        )
    } else {
        Box(Modifier.size(tamanho).background(Cores.Campo, forma), contentAlignment = Alignment.Center) {
            Icon(Icones.Medicamento, contentDescription = null, tint = Cores.DestaqueEscuro, modifier = Modifier.size(tamanho / 2))
        }
    }
}
