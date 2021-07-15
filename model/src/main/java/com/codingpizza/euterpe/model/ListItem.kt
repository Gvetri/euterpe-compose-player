package com.codingpizza.euterpe.model

data class ListItem(
    val guid: String,
    val title: String,
    val link: String,
    val description: String,
    val publicationDate: String,
    val uriStatus: ListItemUriStatus = ListItemUriStatus.NotStored,
) {
    val id: Int =
        if (guid.isEmpty()) {
            -1
        } else {
            guid.replaceBeforeLast(delimiter = "/", replacement = "")
                .replace("/", "").toInt()
        }
}
