package com.codingpizza.euterpe.ui.common

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable

@Composable
fun CenterLoading() {
    CenterBox {
        CircularProgressIndicator()
    }
}
