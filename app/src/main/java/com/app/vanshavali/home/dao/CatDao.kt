package com.app.vanshavali.home.dao

import androidx.room.*
import com.app.vanshavali.home.entity.CatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {
    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<CatEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: CatEntity)

    @Query("DELETE FROM category")
    suspend fun deleteAll()
}