package com.leomilano.diamond.data.repository

import com.leomilano.diamond.data.dao.DiamondDao
import com.leomilano.diamond.data.entity.DiamondEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class DiamondRepository(private val diamondDao: DiamondDao) {

    val allDiamonds: Flow<List<DiamondEntity>> = diamondDao.getAllDiamonds()

    suspend fun getDiamondById(id: Long): DiamondEntity? = withContext(Dispatchers.IO) {
        diamondDao.getDiamondById(id)
    }

    suspend fun insert(diamond: DiamondEntity): Long = withContext(Dispatchers.IO) {
        diamondDao.insertDiamond(diamond)
    }

    suspend fun update(diamond: DiamondEntity) = withContext(Dispatchers.IO) {
        diamondDao.updateDiamond(diamond)
    }

    suspend fun delete(diamond: DiamondEntity) = withContext(Dispatchers.IO) {
        diamondDao.deleteDiamond(diamond)
    }

    suspend fun deleteById(id: Long) = withContext(Dispatchers.IO) {
        diamondDao.deleteDiamondById(id)
    }
}
