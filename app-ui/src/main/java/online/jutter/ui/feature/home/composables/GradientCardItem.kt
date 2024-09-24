package online.jutter.ui.feature.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.jutter.ui.common.composable.RadialGradientPreview
import online.jutter.ui.common.composable.fractal.Gradient
import online.jutter.ui.common.composable.fractal.GradientDeepSpace
import online.jutter.ui.common.composable.fractal.GradientFractalysis
import online.jutter.ui.theme.BackgroundSecondary
import online.jutter.ui.theme.FractalsTheme
import online.jutter.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradientCardItem(
    gradient: Gradient,
    onGradientSelected: (Gradient)->Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundSecondary,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        onClick = {
            onGradientSelected(gradient)
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            RadialGradientPreview(
                gradient = gradient,
                iterations = 40,
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = gradient.name,
                    color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Color count: ${gradient.colors.size}",
                    color = TextGray,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(
    widthDp = 400,
    heightDp = 400,
)
@Composable
fun GradientCardItemPreview() {
    FractalsTheme {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            GradientCardItem(
                gradient = GradientDeepSpace,
                onGradientSelected = {}
            )
            Spacer(modifier = Modifier.height(4.dp))
            GradientCardItem(
                gradient = GradientFractalysis,
                onGradientSelected = {}
            )
        }
    }
}