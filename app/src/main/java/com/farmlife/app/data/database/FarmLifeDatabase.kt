package com.farmlife.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.farmlife.app.data.dao.*
import com.farmlife.app.data.entity.*

/**
 * FarmLife主数据库
 */
@Database(
    entities = [
        PlayerEntity::class,
        PlayerStatisticsEntity::class,
        LandInstanceEntity::class,
        CropInstanceEntity::class,
        AnimalInstanceEntity::class,
        PetInstanceEntity::class,
        PetMissionEntity::class,
        InventoryItemEntity::class,
        FactoryInstanceEntity::class,
        ProductionQueueEntity::class,
        CollectionRecordEntity::class,
        AchievementEntity::class,
        FarmLogEntity::class,
        GameSettingsEntity::class,
        TreeInstanceEntity::class,
        FishInstanceEntity::class,
        PondInstanceEntity::class,
        PetFacilityEntity::class,
        TutorialProgressEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class FarmLifeDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun playerStatisticsDao(): PlayerStatisticsDao
    abstract fun landDao(): LandDao
    abstract fun cropDao(): CropDao
    abstract fun animalDao(): AnimalDao
    abstract fun petDao(): PetDao
    abstract fun petMissionDao(): PetMissionDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun factoryDao(): FactoryDao
    abstract fun productionQueueDao(): ProductionQueueDao
    abstract fun collectionDao(): CollectionDao
    abstract fun achievementDao(): AchievementDao
    abstract fun farmLogDao(): FarmLogDao
    abstract fun gameSettingsDao(): GameSettingsDao
    abstract fun treeDao(): TreeDao
    abstract fun fishDao(): FishDao
    abstract fun pondDao(): PondDao
    abstract fun petFacilityDao(): PetFacilityDao
    abstract fun tutorialDao(): TutorialDao

    companion object {
        @Volatile
        private var INSTANCE: FarmLifeDatabase? = null

        fun getInstance(context: Context): FarmLifeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FarmLifeDatabase::class.java,
                    "farmlife.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
