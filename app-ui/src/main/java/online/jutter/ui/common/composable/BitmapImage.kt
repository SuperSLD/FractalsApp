package online.jutter.ui.common.composable

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
fun BitmapImage(
    bitmap: Bitmap?,
    modifier: Modifier = Modifier.fillMaxSize(),
    contentScale: ContentScale = ContentScale.Fit,
) {
    if (bitmap == null) {
        Text(text = "Image is null")
    } else {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "some useful description",
            modifier = modifier,
            contentScale = contentScale,
        )
    }
}