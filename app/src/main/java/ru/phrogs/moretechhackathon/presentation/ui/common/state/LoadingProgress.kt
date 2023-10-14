package ru.phrogs.moretechhackathon.presentation.ui.common.state

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.phrogs.moretechhackathon.presentation.ui.theme.ProgressIndicatorSize

@Composable
fun LoadingProgress() {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(modifier = Modifier.size(ProgressIndicatorSize))
    }
}