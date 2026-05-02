package com.leomilano.diamond.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diamonds")
data class DiamondEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nom: String,
    val carats: String,
    val prix: String,
    val couleur: String,
    val clarte: String,
    val notes: String,
    val stock: Int = 1,
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
