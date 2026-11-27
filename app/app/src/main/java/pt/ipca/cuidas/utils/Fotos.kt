// Fotografias das embalagens, guardadas na área privada da aplicação.
package pt.ipca.cuidas.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object Fotos {

    // Cria o ficheiro onde a câmara guarda a fotografia.
    fun novoFicheiro(context: Context): File {
        val pasta = File(context.filesDir, "fotos").apply { mkdirs() }
        return File(pasta, "embalagem_${System.currentTimeMillis()}.jpg")
    }

    // Endereço do ficheiro que a câmara pode escrever (FileProvider).
    fun uriPara(context: Context, ficheiro: File): Uri =
        FileProvider.getUriForFile(context, context.packageName + ".ficheiros", ficheiro)

    // Reduz a imagem ao ler, para não esgotar a memória dos telemóveis mais fracos.
    fun miniatura(caminho: String?, ladoMaximo: Int = 400): Bitmap? {
        if (caminho == null) return null
        val f = File(caminho)
        if (!f.exists() || f.length() == 0L) return null
        val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(caminho, limites)
        var amostra = 1
        while (limites.outWidth / amostra > ladoMaximo * 2 || limites.outHeight / amostra > ladoMaximo * 2) {
            amostra *= 2
        }
        return BitmapFactory.decodeFile(caminho, BitmapFactory.Options().apply { inSampleSize = amostra })
    }

    // Apaga uma fotografia.
    fun apagar(caminho: String?) {
        if (caminho != null) File(caminho).delete()
    }

    // Apaga todas as fotografias (ao apagar os dados).
    fun apagarTodas(context: Context) {
        File(context.filesDir, "fotos").listFiles()?.forEach { it.delete() }
    }
}
