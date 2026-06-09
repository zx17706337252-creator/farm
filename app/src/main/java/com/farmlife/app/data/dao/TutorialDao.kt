package com.farmlife.app.data.dao

import androidx.room.*
import com.farmlife.app.data.entity.TutorialProgressEntity

@Dao
interface TutorialDao {
    @Query("SELECT * FROM tutorial_progress WHERE id = 1")
    suspend fun get(): TutorialProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: TutorialProgressEntity)

    @Update
    suspend fun update(progress: TutorialProgressEntity)
}
