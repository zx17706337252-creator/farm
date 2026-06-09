package com.farmlife.app.data.dao

import androidx.room.*
import com.farmlife.app.data.entity.*

/**
 * 果树 DAO
 */
@Dao
interface TreeDao {
    @Query("SELECT * FROM tree_instance")
    suspend fun getAll(): List<TreeInstanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tree: TreeInstanceEntity): Long

    @Update
    suspend fun update(tree: TreeInstanceEntity)

    @Query("DELETE FROM tree_instance WHERE treeId = :treeId")
    suspend fun delete(treeId: Long)
}

/**
 * 鱼塘 DAO
 */
@Dao
interface FishDao {
    @Query("SELECT * FROM fish_instance")
    suspend fun getAll(): List<FishInstanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fish: FishInstanceEntity): Long

    @Update
    suspend fun update(fish: FishInstanceEntity)

    @Query("DELETE FROM fish_instance WHERE fishId = :fishId")
    suspend fun delete(fishId: Long)
}

/**
 * 池塘 DAO
 */
@Dao
interface PondDao {
    @Query("SELECT * FROM pond_instance")
    suspend fun getAll(): List<PondInstanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pond: PondInstanceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ponds: List<PondInstanceEntity>)

    @Update
    suspend fun update(pond: PondInstanceEntity)
}

/**
 * 宠物设施 DAO
 */
@Dao
interface PetFacilityDao {
    @Query("SELECT * FROM pet_facility")
    suspend fun getAll(): List<PetFacilityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(facility: PetFacilityEntity): Long

    @Update
    suspend fun update(facility: PetFacilityEntity)
}
