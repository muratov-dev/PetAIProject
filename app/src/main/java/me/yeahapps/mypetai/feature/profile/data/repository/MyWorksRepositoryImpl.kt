package me.yeahapps.mypetai.feature.profile.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.yeahapps.mypetai.feature.profile.data.local.MyWorksDao
import me.yeahapps.mypetai.feature.profile.data.model.toDomain
import me.yeahapps.mypetai.feature.profile.domain.model.MyWorkModel
import me.yeahapps.mypetai.feature.profile.domain.model.toEntity
import me.yeahapps.mypetai.feature.profile.domain.repository.MyWorksRepository
import javax.inject.Inject

class MyWorksRepositoryImpl @Inject constructor(
    private val dao: MyWorksDao
) : MyWorksRepository {

    override suspend fun getMyWorkInfo(id: Long): MyWorkModel {
        return dao.getMyWork(id)?.toDomain() ?: throw NoSuchElementException("MyWork with id $id not found")
    }

    override suspend fun getMyWorks(): Flow<List<MyWorkModel>> {
        return dao.getMyWorks().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun createMyWork(model: MyWorkModel) {
        dao.addMyWork(model.toEntity())
    }

    override suspend fun deleteMyWork(id: Long) {
        dao.deleteMyWork(id)
    }

    override suspend fun getMyWorksCount(): Int {
        return dao.getMyWorksCount()
    }
}