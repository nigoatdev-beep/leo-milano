package com.leomilano.diamond.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.leomilano.diamond.data.entity.DiamondEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiamondDao {

    @Query("SELECT * FROM diamonds ORDER BY timestamp DESC")
    fun getAllDiamonds(): Flow<List<DiamondEntity>>

    @Query("SELECT * FROM diamonds WHERE id = :id")
    suspend fun getDiamondById(id: Long): DiamondEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiamond(diamond: DiamondEntity): Long

    @Update
    suspend fun updateDiamond(diamond: DiamondEntity)

    @Delete
    suspend fun deleteDiamond(diamond: DiamondEntity)

    @Query("DELETE FROM diamonds WHERE id = :id")
    suspend fun deleteDiamondById(id: Long)
}
