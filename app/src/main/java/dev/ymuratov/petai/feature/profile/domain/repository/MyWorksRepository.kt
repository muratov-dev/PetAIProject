package dev.ymuratov.petai.feature.profile.domain.repository

import dev.ymuratov.petai.feature.profile.domain.model.MyWorkModel
import kotlinx.coroutines.flow.Flow

interface MyWorksRepository {

    suspend fun getMyWorks(): Flow<List<MyWorkModel>>

    suspend fun createMyWork(model: MyWorkModel)
    suspend fun deleteMyWork(id: Long)

    suspend fun getMyWorksCount(): Int
}