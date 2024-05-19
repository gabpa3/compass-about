package com.gabcode.compassabout.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AboutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(content: AboutEntity)

    @Query("SELECT * FROM about")
    suspend fun getData(): AboutEntity?
}
