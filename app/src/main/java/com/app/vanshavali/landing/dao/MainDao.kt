package com.app.vanshavali.landing.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.app.vanshavali.landing.entity.MainEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MainDao {
    @Query("SELECT * FROM main_list")
    fun getAllCategories(): Flow<List<MainEntity>>

    //    @Query("SELECT * FROM main_list WHERE member_id = :memberIID OR parent_id = :parentId OR parent_id = :memberIID")
    @Query("SELECT * FROM main_list WHERE  member_id = :parentId OR member_id = :memberIID OR parent_id = :memberIID")
    fun getDetailsByMemberId(memberIID: String, parentId: String): Flow<MutableList<MainEntity>>

    //    @Query("SELECT * FROM main_list WHERE member_id = :memberIID OR parent_id = :parentId OR parent_id = :memberIID")
    @Query("SELECT * FROM main_list WHERE  member_id = :parentId ")
    fun getDetailsById(parentId: String): MainEntity

    //    @Query("SELECT * FROM main_list WHERE member_id = :memberIID OR parent_id = :parentId OR parent_id = :memberIID")
    @Query("SELECT COUNT(*) FROM main_list WHERE parent_id = :memberId")
    fun getChildCount(memberId: String): Int

    @Insert(onConflict = IGNORE)
    suspend fun insert(word: MainEntity)

    @Query("DELETE FROM main_list")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM main_list)")
    suspend fun hasItem(): Boolean

}