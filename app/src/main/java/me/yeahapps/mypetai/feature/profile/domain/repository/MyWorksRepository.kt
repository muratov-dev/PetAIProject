package me.yeahapps.mypetai.feature.profile.domain.repository

import kotlinx.coroutines.flow.Flow
import me.yeahapps.mypetai.feature.profile.domain.model.MyWorkModel

interface MyWorksRepository {

    suspend fun getMyWorkInfo(id: Long): MyWorkModel
    suspend fun getMyWorks(): Flow<List<MyWorkModel>>

    suspend fun createMyWork(model: MyWorkModel)
    suspend fun deleteMyWork(id: Long)

    suspend fun getMyWorksCount(): Int
}