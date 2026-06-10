package com.farmlife.app.data.dao

import androidx.room.*
import com.farmlife.app.data.entity.*

/**
 * Tutorial DAO
 */
@Dao
interface TutorialDao {
    @Query("SELECT * FROM tutorial_progress WHERE id = 1")
    suspend fun get(): TutorialProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: TutorialProgressEntity)

    @Update
    suspend fun update(progress: TutorialProgressEntity)
}

/**
 * Player DAO
 */
@Dao
interface PlayerDao {
    @Query("SELECT * FROM player WHERE id = 1")
    suspend fun getPlayer(): PlayerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(player: PlayerEntity)

    @Update
    suspend fun update(player: PlayerEntity)
}

/**
 * PlayerStatistics DAO
 */
@Dao
interface PlayerStatisticsDao {
    @Query("SELECT * FROM player_statistics WHERE id = 1")
    suspend fun get(): PlayerStatisticsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(stats: PlayerStatisticsEntity)

    @Update
    suspend fun update(stats: PlayerStatisticsEntity)
}

/**
 * LandInstance DAO
 */
@Dao
interface LandDao {
    @Query("SELECT * FROM land_instance")
    suspend fun getAll(): List<LandInstanceEntity>

    @Query("SELECT * FROM land_instance WHERE region = :region")
    suspend fun getByRegion(region: String): List<LandInstanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(land: LandInstanceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lands: List<LandInstanceEntity>)

    @Update
    suspend fun update(land: LandInstanceEntity)

    @Query("DELETE FROM land_instance WHERE landId = :landId")
    suspend fun delete(landId: Long)
}

/**
 * CropInstance DAO
 */
@Dao
interface CropDao {
    @Query("SELECT * FROM crop_instance")
    suspend fun getAll(): List<CropInstanceEntity>

    @Query("SELECT * FROM crop_instance WHERE landId = :landId")
    suspend fun getByLandId(landId: Long): CropInstanceEntity?

    @Query("SELECT * FROM crop_instance WHERE finishTime <= :currentTime AND harvested = 0")
    suspend fun getReadyToHarvest(currentTime: Long): List<CropInstanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crop: CropInstanceEntity): Long

    @Update
    suspend fun update(crop: CropInstanceEntity)

    @Query("DELETE FROM crop_instance WHERE instanceId = :instanceId")
    suspend fun delete(instanceId: Long)

    @Query("DELETE FROM crop_instance WHERE landId = :landId")
    suspend fun deleteByLandId(landId: Long)
}

/**
 * AnimalInstance DAO
 */
@Dao
interface AnimalDao {
    @Query("SELECT * FROM animal_instance")
    suspend fun getAll(): List<AnimalInstanceEntity>

    @Query("SELECT * FROM animal_instance WHERE region = :region")
    suspend fun getByRegion(region: String): List<AnimalInstanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(animal: AnimalInstanceEntity): Long

    @Update
    suspend fun update(animal: AnimalInstanceEntity)

    @Query("DELETE FROM animal_instance WHERE instanceId = :instanceId")
    suspend fun delete(instanceId: Long)
}

/**
 * PetInstance DAO
 */
@Dao
interface PetDao {
    @Query("SELECT * FROM pet_instance")
    suspend fun getAll(): List<PetInstanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pet: PetInstanceEntity): Long

    @Update
    suspend fun update(pet: PetInstanceEntity)

    @Query("DELETE FROM pet_instance WHERE instanceId = :instanceId")
    suspend fun delete(instanceId: Long)
}

/**
 * PetMission DAO
 */
@Dao
interface PetMissionDao {
    @Query("SELECT * FROM pet_mission")
    suspend fun getAll(): List<PetMissionEntity>

    @Query("SELECT * FROM pet_mission WHERE petInstanceId = :petId")
    suspend fun getByPetId(petId: Long): List<PetMissionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mission: PetMissionEntity): Long

    @Update
    suspend fun update(mission: PetMissionEntity)

    @Query("DELETE FROM pet_mission WHERE missionId = :missionId")
    suspend fun delete(missionId: Long)
}

/**
 * Inventory DAO
 */
@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory_item")
    suspend fun getAll(): List<InventoryItemEntity>

    @Query("SELECT * FROM inventory_item WHERE itemType = :type")
    suspend fun getByType(type: String): List<InventoryItemEntity>

    @Query("SELECT * FROM inventory_item WHERE itemType = :type AND configId = :configId AND quality = :quality")
    suspend fun findItem(type: String, configId: Int, quality: Int): InventoryItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InventoryItemEntity): Long

    @Update
    suspend fun update(item: InventoryItemEntity)

    @Query("DELETE FROM inventory_item WHERE itemId = :itemId")
    suspend fun delete(itemId: Long)

    @Query("DELETE FROM inventory_item")
    suspend fun clear()
}

/**
 * Factory DAO
 */
@Dao
interface FactoryDao {
    @Query("SELECT * FROM factory_instance")
    suspend fun getAll(): List<FactoryInstanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(factory: FactoryInstanceEntity): Long

    @Update
    suspend fun update(factory: FactoryInstanceEntity)

    @Query("DELETE FROM factory_instance WHERE factoryId = :factoryId")
    suspend fun delete(factoryId: Long)
}

/**
 * ProductionQueue DAO
 */
@Dao
interface ProductionQueueDao {
    @Query("SELECT * FROM production_queue")
    suspend fun getAll(): List<ProductionQueueEntity>

    @Query("SELECT * FROM production_queue WHERE factoryId = :factoryId")
    suspend fun getByFactoryId(factoryId: Long): List<ProductionQueueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(queue: ProductionQueueEntity): Long

    @Update
    suspend fun update(queue: ProductionQueueEntity)

    @Query("DELETE FROM production_queue WHERE queueId = :queueId")
    suspend fun delete(queueId: Long)
}

/**
 * Collection DAO
 */
@Dao
interface CollectionDao {
    @Query("SELECT * FROM collection_record")
    suspend fun getAll(): List<CollectionRecordEntity>

    @Query("SELECT * FROM collection_record WHERE collectionType = :type")
    suspend fun getByType(type: String): List<CollectionRecordEntity>

    @Query("SELECT * FROM collection_record WHERE collectionType = :type AND configId = :configId")
    suspend fun findByTypeAndId(type: String, configId: Int): CollectionRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: CollectionRecordEntity): Long

    @Update
    suspend fun update(record: CollectionRecordEntity)
}

/**
 * Achievement DAO
 */
@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievement")
    suspend fun getAll(): List<AchievementEntity>

    @Query("SELECT * FROM achievement WHERE achievementId = :id")
    suspend fun getById(id: String): AchievementEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(achievement: AchievementEntity): Long

    @Update
    suspend fun update(achievement: AchievementEntity)
}

/**
 * FarmLog DAO
 */
@Dao
interface FarmLogDao {
    @Query("SELECT * FROM farm_log ORDER BY timestamp DESC LIMIT 100")
    suspend fun getRecent(): List<FarmLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: FarmLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<FarmLogEntity>)
}

/**
 * GameSettings DAO
 */
@Dao
interface GameSettingsDao {
    @Query("SELECT * FROM game_settings WHERE id = 1")
    suspend fun get(): GameSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: GameSettingsEntity)

    @Update
    suspend fun update(settings: GameSettingsEntity)
}
