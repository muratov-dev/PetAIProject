package me.yeahapps.mypetai.feature.profile.data.repository

import me.yeahapps.mypetai.feature.profile.data.local.MyWorksDao
import me.yeahapps.mypetai.feature.profile.data.model.toDomain
import me.yeahapps.mypetai.feature.profile.domain.model.MyWorkModel
import me.yeahapps.mypetai.feature.profile.domain.repository.MyWorksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyWorksRepositoryImpl @Inject constructor(
    private val dao: MyWorksDao
) : MyWorksRepository {

    override suspend fun getMyWorks(): Flow<List<MyWorkModel>> {
        return dao.getMyWorks().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun createMyWork(model: MyWorkModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMyWork(id: Long) {
        dao.deleteMyWork(id)
    }

    override suspend fun getMyWorksCount(): Int {
        return dao.getMyWorksCount()
    }
}