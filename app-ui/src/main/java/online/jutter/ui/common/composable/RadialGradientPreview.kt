package online.jutter.ui.common.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.RadialGradient
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun RadialGradientPreview(
    gradient: Gradient,
    iterations: Int = 200,
    modifier: Modifier,
) {
    Canvas(
        modifier = modifier,
        onDraw = {
            val angleDiff = 360F / iterations
            val strokeWidth = size.width/10
            for (i in 0 until iterations) {
//                drawRect(
//                    topLeft = Offset(0F, 0F),
//                    size = Size(size.width, size.height),
//                    color = Color.Cyan
//                )
                drawArc(
                    topLeft = Offset(strokeWidth, strokeWidth),
                    size = Size(
                        size.width - strokeWidth*2,
                        size.height - strokeWidth*2,
                    ),
                    color = Color(gradient.getColor(i/iterations.toFloat())),
                    startAngle = angleDiff * i,
                    sweepAngle = angleDiff + 1,
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth.dp.toPx(),
                    )
                )
            }
        }
    )
}

@Preview
@Composable
fun RadialGradientDebugPreview() {
    FractalsTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            RadialGradientPreview(
                GradientBlue,
                4,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
            )
            RadialGradientPreview(
                GradientBlueGreen,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
            )
            RadialGradientPreview(
                GradientFractalysis,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
            )
            RadialGradientPreview(
                GradientDeepSpace,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
            )
            RadialGradientPreview(
                GradientRainbow,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
            )
        }
    }
}


