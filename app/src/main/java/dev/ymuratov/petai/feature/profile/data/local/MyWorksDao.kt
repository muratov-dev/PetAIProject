package dev.ymuratov.petai.feature.profile.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.ymuratov.petai.feature.profile.data.model.MyWorkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MyWorksDao {

    @Query("select * from my_works")
    fun getMyWorks(): Flow<List<MyWorkEntity>>

    @Query("select count(*) from my_works")
    suspend fun getMyWorksCount(): Int

    @Query("select * from my_works where id = :id")
    suspend fun getMyWork(id: Long): MyWorkEntity?

    @Query("delete from my_works where id = :id")
    suspend fun deleteMyWork(id: Long)

    @Upsert
    suspend fun addMyWork(model: MyWorkEntity)
}