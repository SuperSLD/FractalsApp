package online.jutter.ui.common.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import online.jutter.ui.theme.Background
import online.jutter.ui.theme.FractalsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultCard(
    modifier: Modifier,
    onClick: (()->Unit)? = null,
    content: @Composable BoxScope.()->Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Background,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        onClick = { onClick?.invoke() },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            content()
        }
    }
}

@Preview(
    widthDp = 400,
    heightDp = 400,
)
@Composable
fun DefaultCardPreview() {
    FractalsTheme {
        Box(
            contentAlignment = Alignment.Center
        ) {
            DefaultCard(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            ) {

                Text(
                    text = "Text In Default Card",
                    color = Color.White,
                )

            }
        }
    }
}