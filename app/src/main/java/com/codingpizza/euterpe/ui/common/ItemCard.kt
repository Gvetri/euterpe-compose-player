package com.codingpizza.euterpe.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.codingpizza.euterpe.model.ListItem

@Composable
fun ItemCard(item: ListItem, onClick: () -> Unit) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(onClick = onClick)
        ) {
            Text(
                style = MaterialTheme.typography.subtitle1,
                text = item.title,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                style = MaterialTheme.typography.body1,
                text = item.description,
                modifier = Modifier.padding(16.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
