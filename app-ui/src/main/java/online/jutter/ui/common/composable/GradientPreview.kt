package online.jutter.ui.common.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import online.jutter.ui.common.composable.fractal.Gradient
import online.jutter.ui.common.composable.fractal.GradientBlue
import online.jutter.ui.common.composable.fractal.GradientBlueGreen
import online.jutter.ui.common.composable.fractal.GradientDeepSpace
import online.jutter.ui.common.composable.fractal.GradientFractalysis
import online.jutter.ui.common.composable.fractal.GradientRainbow
import online.jutter.ui.common.composable.fractal.RgbColor
import online.jutter.ui.theme.FractalsTheme

@Composable
fun GradientPreview(
    gradient: Gradient,
    iterations: Int,
) {
    Card {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            onDraw = {

                for (i in 0 until iterations) {
                    drawRect(
                        topLeft = Offset(size.width / iterations * i, 0F),
                        size = Size(size.width/iterations + 1, size.height),
                        color = Color(gradient.getColor(i/iterations.toFloat()))
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun DefaultPreview() {
    FractalsTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            GradientPreview(
                GradientBlue,
                200,
            )
            GradientPreview(
                GradientBlueGreen,
                200,
            )
            GradientPreview(
                GradientFractalysis,
                200,
            )
            GradientPreview(
                GradientDeepSpace,
                200,
            )
            GradientPreview(
                GradientRainbow,
                200,
            )
        }
    }
}


