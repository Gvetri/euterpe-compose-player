package com.codingpizza.cachemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemCached(
    @PrimaryKey val id: Int,
    val guid: String,
    val title: String,
    val link: String,
    val description: String,
    val publicationDate: String,
    val uriDownload: String? = null
)
