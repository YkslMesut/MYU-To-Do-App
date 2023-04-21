package com.myu.myuto_do.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myu.myuto_do.util.Constants.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class ToDoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority,
)