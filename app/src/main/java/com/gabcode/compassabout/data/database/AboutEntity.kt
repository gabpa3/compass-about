package com.gabcode.compassabout.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "about")
data class AboutEntity(
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "modified_at") val modifiedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "content") val content: String?
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
