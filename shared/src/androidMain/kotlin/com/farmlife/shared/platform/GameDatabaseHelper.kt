package com.farmlife.shared.platform

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.farmlife.shared.model.*

/**
 * Android SQLite 数据库助手
 */
class GameDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    
    companion object {
        private const val DATABASE_NAME = "farmlife.db"
        private const val DATABASE_VERSION = 1
    }
    
    override fun onCreate(db: SQLiteDatabase) {
        // 玩家表
        db.execSQL("""
            CREATE TABLE player (
                id INTEGER PRIMARY KEY,
                gold INTEGER DEFAULT 500,
                level INTEGER DEFAULT 1,
                exp INTEGER DEFAULT 0,
                collection_score INTEGER DEFAULT 0
            )
        """)
        
        // 土地表
        db.execSQL("""
            CREATE TABLE lands (
                land_id INTEGER PRIMARY KEY,
                region TEXT,
                land_index INTEGER,
                unlocked INTEGER DEFAULT 0
            )
        """)
        
        // 作物表
        db.execSQL("""
            CREATE TABLE crops (
                instance_id INTEGER PRIMARY KEY,
                land_id INTEGER,
                crop_id INTEGER,
                plant_time INTEGER,
                finish_time INTEGER,
                quality INTEGER DEFAULT 0
            )
        """)
        
        // 动物表
        db.execSQL("""
            CREATE TABLE animals (
                instance_id INTEGER PRIMARY KEY,
                animal_id INTEGER,
                level INTEGER DEFAULT 1,
                exp INTEGER DEFAULT 0,
                last_produce_time INTEGER,
                quality INTEGER DEFAULT 0
            )
        """)
        
        // 宠物表
        db.execSQL("""
            CREATE TABLE pets (
                instance_id INTEGER PRIMARY KEY,
                pet_id INTEGER,
                level INTEGER DEFAULT 1,
                exp INTEGER DEFAULT 0,
                friendliness INTEGER DEFAULT 50,
                is_active INTEGER DEFAULT 1,
                work_slots INTEGER DEFAULT 1
            )
        """)
        
        // 库存表
        db.execSQL("""
            CREATE TABLE inventory (
                item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                item_type TEXT,
                config_id INTEGER,
                quantity INTEGER DEFAULT 1,
                quality INTEGER DEFAULT 0
            )
        """)
        
        // 果树表
        db.execSQL("""
            CREATE TABLE trees (
                tree_id INTEGER PRIMARY KEY,
                config_id INTEGER,
                plant_time INTEGER,
                harvest_time INTEGER,
                quality INTEGER DEFAULT 0,
                region TEXT DEFAULT 'ORCHARD'
            )
        """)
        
        // 鱼塘表
        db.execSQL("""
            CREATE TABLE ponds (
                pond_id INTEGER PRIMARY KEY,
                unlocked INTEGER DEFAULT 0,
                region TEXT DEFAULT 'FISHPOND'
            )
        """)
        
        // 鱼类表
        db.execSQL("""
            CREATE TABLE fish (
                fish_id INTEGER PRIMARY KEY,
                pond_id INTEGER,
                config_id INTEGER,
                finish_time INTEGER,
                quality INTEGER DEFAULT 0
            )
        """)
        
        // 工厂表
        db.execSQL("""
            CREATE TABLE factories (
                factory_id INTEGER PRIMARY KEY,
                factory_type TEXT,
                level INTEGER DEFAULT 1,
                unlocked INTEGER DEFAULT 0
            )
        """)
        
        // 图鉴表
        db.execSQL("""
            CREATE TABLE collections (
                record_id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT,
                config_id INTEGER,
                name TEXT,
                highest_quality INTEGER DEFAULT 0,
                count INTEGER DEFAULT 0,
                UNIQUE(type, config_id)
            )
        """)
        
        // 统计表
        db.execSQL("""
            CREATE TABLE stats (
                id INTEGER PRIMARY KEY,
                total_plant INTEGER DEFAULT 0,
                total_harvest INTEGER DEFAULT 0,
                total_earned INTEGER DEFAULT 0,
                total_spent INTEGER DEFAULT 0,
                play_time INTEGER DEFAULT 0
            )
        """)
        
        // 设置表
        db.execSQL("""
            CREATE TABLE settings (
                id INTEGER PRIMARY KEY,
                sound_enabled INTEGER DEFAULT 1,
                music_enabled INTEGER DEFAULT 1,
                notifications_enabled INTEGER DEFAULT 1,
                auto_save_interval INTEGER DEFAULT 60
            )
        """)
        
        // 教程进度表
        db.execSQL("""
            CREATE TABLE tutorial_progress (
                id INTEGER PRIMARY KEY,
                completed INTEGER DEFAULT 0,
                current_step INTEGER DEFAULT 0
            )
        """)
        
        // 日志表
        db.execSQL("""
            CREATE TABLE logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT,
                message TEXT,
                timestamp INTEGER
            )
        """)
    }
    
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 升级逻辑
    }
    
    // ===== 玩家操作 =====
    fun getPlayer(): Player? {
        val db = readableDatabase
        val cursor = db.query("player", null, null, null, null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                Player(
                    gold = it.getLong(it.getColumnIndexOrThrow("gold")),
                    level = it.getInt(it.getColumnIndexOrThrow("level")),
                    exp = it.getInt(it.getColumnIndexOrThrow("exp")),
                    collectionScore = it.getInt(it.getColumnIndexOrThrow("collection_score"))
                )
            } else null
        }
    }
    
    fun updatePlayer(player: Player) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("gold", player.gold)
            put("level", player.level)
            put("exp", player.exp)
            put("collection_score", player.collectionScore)
        }
        if (getPlayer() == null) {
            db.insert("player", null, values)
        } else {
            db.update("player", values, "id = 1", null)
        }
    }
    
    fun insertPlayer(player: Player) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", 1)
            put("gold", player.gold)
            put("level", player.level)
            put("exp", player.exp)
            put("collection_score", player.collectionScore)
        }
        db.insert("player", null, values)
    }
    
    // ===== 土地操作 =====
    fun getAllLands(): List<LandInstance> {
        val db = readableDatabase
        val cursor = db.query("lands", null, null, null, null, null, "land_index ASC")
        val list = mutableListOf<LandInstance>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(LandInstance(
                    landId = it.getLong(it.getColumnIndexOrThrow("land_id")),
                    region = it.getString(it.getColumnIndexOrThrow("region")),
                    index = it.getInt(it.getColumnIndexOrThrow("land_index")),
                    unlocked = it.getInt(it.getColumnIndexOrThrow("unlocked")) == 1
                ))
            }
        }
        return list
    }
    
    fun getLandById(landId: Long): LandInstance? {
        val db = readableDatabase
        val cursor = db.query("lands", null, "land_id = ?", arrayOf(landId.toString()), null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                LandInstance(
                    landId = it.getLong(it.getColumnIndexOrThrow("land_id")),
                    region = it.getString(it.getColumnIndexOrThrow("region")),
                    index = it.getInt(it.getColumnIndexOrThrow("land_index")),
                    unlocked = it.getInt(it.getColumnIndexOrThrow("unlocked")) == 1
                )
            } else null
        }
    }
    
    fun insertLand(land: LandInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("land_id", land.landId)
            put("region", land.region)
            put("land_index", land.index)
            put("unlocked", if (land.unlocked) 1 else 0)
        }
        db.insert("lands", null, values)
    }
    
    fun updateLand(land: LandInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("unlocked", if (land.unlocked) 1 else 0)
        }
        db.update("lands", values, "land_id = ?", arrayOf(land.landId.toString()))
    }
    
    // ===== 作物操作 =====
    fun getAllCrops(): List<CropInstance> {
        val db = readableDatabase
        val cursor = db.query("crops", null, null, null, null, null, null)
        val list = mutableListOf<CropInstance>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(CropInstance(
                    instanceId = it.getLong(it.getColumnIndexOrThrow("instance_id")),
                    landId = it.getLong(it.getColumnIndexOrThrow("land_id")),
                    cropId = it.getInt(it.getColumnIndexOrThrow("crop_id")),
                    plantTime = it.getLong(it.getColumnIndexOrThrow("plant_time")),
                    finishTime = it.getLong(it.getColumnIndexOrThrow("finish_time")),
                    quality = it.getInt(it.getColumnIndexOrThrow("quality"))
                ))
            }
        }
        return list
    }
    
    fun getCropByLand(landId: Long): CropInstance? {
        val db = readableDatabase
        val cursor = db.query("crops", null, "land_id = ?", arrayOf(landId.toString()), null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                CropInstance(
                    instanceId = it.getLong(it.getColumnIndexOrThrow("instance_id")),
                    landId = it.getLong(it.getColumnIndexOrThrow("land_id")),
                    cropId = it.getInt(it.getColumnIndexOrThrow("crop_id")),
                    plantTime = it.getLong(it.getColumnIndexOrThrow("plant_time")),
                    finishTime = it.getLong(it.getColumnIndexOrThrow("finish_time")),
                    quality = it.getInt(it.getColumnIndexOrThrow("quality"))
                )
            } else null
        }
    }
    
    fun insertCrop(crop: CropInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("instance_id", crop.instanceId)
            put("land_id", crop.landId)
            put("crop_id", crop.cropId)
            put("plant_time", crop.plantTime)
            put("finish_time", crop.finishTime)
            put("quality", crop.quality)
        }
        db.insert("crops", null, values)
    }
    
    fun updateCrop(crop: CropInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("finish_time", crop.finishTime)
        }
        db.update("crops", values, "instance_id = ?", arrayOf(crop.instanceId.toString()))
    }
    
    fun deleteCrop(instanceId: Long) {
        val db = writableDatabase
        db.delete("crops", "instance_id = ?", arrayOf(instanceId.toString()))
    }
    
    // ===== 动物操作 =====
    fun getAllAnimals(): List<AnimalInstance> {
        val db = readableDatabase
        val cursor = db.query("animals", null, null, null, null, null, null)
        val list = mutableListOf<AnimalInstance>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(AnimalInstance(
                    instanceId = it.getLong(it.getColumnIndexOrThrow("instance_id")),
                    animalId = it.getInt(it.getColumnIndexOrThrow("animal_id")),
                    level = it.getInt(it.getColumnIndexOrThrow("level")),
                    exp = it.getInt(it.getColumnIndexOrThrow("exp")),
                    lastProduceTime = it.getLong(it.getColumnIndexOrThrow("last_produce_time")),
                    quality = it.getInt(it.getColumnIndexOrThrow("quality"))
                ))
            }
        }
        return list
    }
    
    fun insertAnimal(animal: AnimalInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("instance_id", animal.instanceId)
            put("animal_id", animal.animalId)
            put("level", animal.level)
            put("exp", animal.exp)
            put("last_produce_time", animal.lastProduceTime)
            put("quality", animal.quality)
        }
        db.insert("animals", null, values)
    }
    
    fun updateAnimal(animal: AnimalInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("level", animal.level)
            put("exp", animal.exp)
            put("last_produce_time", animal.lastProduceTime)
            put("quality", animal.quality)
        }
        db.update("animals", values, "instance_id = ?", arrayOf(animal.instanceId.toString()))
    }
    
    fun deleteAnimal(instanceId: Long) {
        val db = writableDatabase
        db.delete("animals", "instance_id = ?", arrayOf(instanceId.toString()))
    }
    
    // ===== 宠物操作 =====
    fun getAllPets(): List<PetInstance> {
        val db = readableDatabase
        val cursor = db.query("pets", null, null, null, null, null, null)
        val list = mutableListOf<PetInstance>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(PetInstance(
                    instanceId = it.getLong(it.getColumnIndexOrThrow("instance_id")),
                    petId = it.getInt(it.getColumnIndexOrThrow("pet_id")),
                    level = it.getInt(it.getColumnIndexOrThrow("level")),
                    exp = it.getInt(it.getColumnIndexOrThrow("exp")),
                    friendliness = it.getInt(it.getColumnIndexOrThrow("friendliness")),
                    isActive = it.getInt(it.getColumnIndexOrThrow("is_active")) == 1,
                    workSlots = it.getInt(it.getColumnIndexOrThrow("work_slots"))
                ))
            }
        }
        return list
    }
    
    fun insertPet(pet: PetInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("instance_id", pet.instanceId)
            put("pet_id", pet.petId)
            put("level", pet.level)
            put("exp", pet.exp)
            put("friendliness", pet.friendliness)
            put("is_active", if (pet.isActive) 1 else 0)
            put("work_slots", pet.workSlots)
        }
        db.insert("pets", null, values)
    }
    
    fun updatePet(pet: PetInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("level", pet.level)
            put("exp", pet.exp)
            put("friendliness", pet.friendliness)
            put("is_active", if (pet.isActive) 1 else 0)
            put("work_slots", pet.workSlots)
        }
        db.update("pets", values, "instance_id = ?", arrayOf(pet.instanceId.toString()))
    }
    
    fun deletePet(instanceId: Long) {
        val db = writableDatabase
        db.delete("pets", "instance_id = ?", arrayOf(instanceId.toString()))
    }
    
    // ===== 库存操作 =====
    fun getInventory(): List<InventoryItem> {
        val db = readableDatabase
        val cursor = db.query("inventory", null, null, null, null, null, null)
        val list = mutableListOf<InventoryItem>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(InventoryItem(
                    itemId = it.getLong(it.getColumnIndexOrThrow("item_id")),
                    itemType = it.getString(it.getColumnIndexOrThrow("item_type")),
                    configId = it.getInt(it.getColumnIndexOrThrow("config_id")),
                    quantity = it.getInt(it.getColumnIndexOrThrow("quantity")),
                    quality = it.getInt(it.getColumnIndexOrThrow("quality"))
                ))
            }
        }
        return list
    }
    
    fun addInventoryItem(item: InventoryItem) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("item_type", item.itemType)
            put("config_id", item.configId)
            put("quantity", item.quantity)
            put("quality", item.quality)
        }
        db.insert("inventory", null, values)
    }
    
    fun updateInventoryItem(item: InventoryItem) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("quantity", item.quantity)
        }
        db.update("inventory", values, "item_id = ?", arrayOf(item.itemId.toString()))
    }
    
    fun deleteInventoryItem(itemId: Long) {
        val db = writableDatabase
        db.delete("inventory", "item_id = ?", arrayOf(itemId.toString()))
    }
    
    fun findInventoryItem(type: String, configId: Int, quality: Int): InventoryItem? {
        val db = readableDatabase
        val cursor = db.query("inventory", null, 
            "item_type = ? AND config_id = ? AND quality = ?", 
            arrayOf(type, configId.toString(), quality.toString()), 
            null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                InventoryItem(
                    itemId = it.getLong(it.getColumnIndexOrThrow("item_id")),
                    itemType = it.getString(it.getColumnIndexOrThrow("item_type")),
                    configId = it.getInt(it.getColumnIndexOrThrow("config_id")),
                    quantity = it.getInt(it.getColumnIndexOrThrow("quantity")),
                    quality = it.getInt(it.getColumnIndexOrThrow("quality"))
                )
            } else null
        }
    }
    
    // ===== 果树操作 =====
    fun getAllTrees(): List<TreeInstance> {
        val db = readableDatabase
        val cursor = db.query("trees", null, null, null, null, null, null)
        val list = mutableListOf<TreeInstance>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(TreeInstance(
                    treeId = it.getLong(it.getColumnIndexOrThrow("tree_id")),
                    configId = it.getInt(it.getColumnIndexOrThrow("config_id")),
                    plantTime = it.getLong(it.getColumnIndexOrThrow("plant_time")),
                    harvestTime = it.getLong(it.getColumnIndexOrThrow("harvest_time")),
                    quality = it.getInt(it.getColumnIndexOrThrow("quality")),
                    region = it.getString(it.getColumnIndexOrThrow("region"))
                ))
            }
        }
        return list
    }
    
    fun insertTree(tree: TreeInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("tree_id", tree.treeId)
            put("config_id", tree.configId)
            put("plant_time", tree.plantTime)
            put("harvest_time", tree.harvestTime)
            put("quality", tree.quality)
            put("region", tree.region)
        }
        db.insert("trees", null, values)
    }
    
    fun updateTree(tree: TreeInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("harvest_time", tree.harvestTime)
        }
        db.update("trees", values, "tree_id = ?", arrayOf(tree.treeId.toString()))
    }
    
    // ===== 鱼塘操作 =====
    fun getAllPonds(): List<PondInstance> {
        val db = readableDatabase
        val cursor = db.query("ponds", null, null, null, null, null, null)
        val list = mutableListOf<PondInstance>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(PondInstance(
                    pondId = it.getLong(it.getColumnIndexOrThrow("pond_id")),
                    unlocked = it.getInt(it.getColumnIndexOrThrow("unlocked")) == 1,
                    region = it.getString(it.getColumnIndexOrThrow("region"))
                ))
            }
        }
        return list
    }
    
    fun getPondById(pondId: Long): PondInstance? {
        val db = readableDatabase
        val cursor = db.query("ponds", null, "pond_id = ?", arrayOf(pondId.toString()), null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                PondInstance(
                    pondId = it.getLong(it.getColumnIndexOrThrow("pond_id")),
                    unlocked = it.getInt(it.getColumnIndexOrThrow("unlocked")) == 1,
                    region = it.getString(it.getColumnIndexOrThrow("region"))
                )
            } else null
        }
    }
    
    fun insertPond(pond: PondInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("pond_id", pond.pondId)
            put("unlocked", if (pond.unlocked) 1 else 0)
            put("region", pond.region)
        }
        db.insert("ponds", null, values)
    }
    
    fun updatePond(pond: PondInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("unlocked", if (pond.unlocked) 1 else 0)
        }
        db.update("ponds", values, "pond_id = ?", arrayOf(pond.pondId.toString()))
    }
    
    // ===== 鱼类操作 =====
    fun getAllFish(): List<FishInstance> {
        val db = readableDatabase
        val cursor = db.query("fish", null, null, null, null, null, null)
        val list = mutableListOf<FishInstance>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(FishInstance(
                    fishId = it.getLong(it.getColumnIndexOrThrow("fish_id")),
                    pondId = it.getLong(it.getColumnIndexOrThrow("pond_id")),
                    configId = it.getInt(it.getColumnIndexOrThrow("config_id")),
                    finishTime = it.getLong(it.getColumnIndexOrThrow("finish_time")),
                    quality = it.getInt(it.getColumnIndexOrThrow("quality"))
                ))
            }
        }
        return list
    }
    
    fun insertFish(fish: FishInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("fish_id", fish.fishId)
            put("pond_id", fish.pondId)
            put("config_id", fish.configId)
            put("finish_time", fish.finishTime)
            put("quality", fish.quality)
        }
        db.insert("fish", null, values)
    }
    
    fun updateFish(fish: FishInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("finish_time", fish.finishTime)
        }
        db.update("fish", values, "fish_id = ?", arrayOf(fish.fishId.toString()))
    }
    
    fun deleteFish(fishId: Long) {
        val db = writableDatabase
        db.delete("fish", "fish_id = ?", arrayOf(fishId.toString()))
    }
    
    // ===== 工厂操作 =====
    fun getAllFactories(): List<FactoryInstance> {
        val db = readableDatabase
        val cursor = db.query("factories", null, null, null, null, null, null)
        val list = mutableListOf<FactoryInstance>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(FactoryInstance(
                    factoryId = it.getLong(it.getColumnIndexOrThrow("factory_id")),
                    factoryType = it.getString(it.getColumnIndexOrThrow("factory_type")),
                    level = it.getInt(it.getColumnIndexOrThrow("level")),
                    unlocked = it.getInt(it.getColumnIndexOrThrow("unlocked")) == 1
                ))
            }
        }
        return list
    }
    
    fun insertFactory(factory: FactoryInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("factory_id", factory.factoryId)
            put("factory_type", factory.factoryType)
            put("level", factory.level)
            put("unlocked", if (factory.unlocked) 1 else 0)
        }
        db.insert("factories", null, values)
    }
    
    fun updateFactory(factory: FactoryInstance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("level", factory.level)
            put("unlocked", if (factory.unlocked) 1 else 0)
        }
        db.update("factories", values, "factory_id = ?", arrayOf(factory.factoryId.toString()))
    }
    
    // ===== 图鉴操作 =====
    fun getAllCollections(): List<CollectionRecord> {
        val db = readableDatabase
        val cursor = db.query("collections", null, null, null, null, null, null)
        val list = mutableListOf<CollectionRecord>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(CollectionRecord(
                    recordId = it.getLong(it.getColumnIndexOrThrow("record_id")),
                    type = it.getString(it.getColumnIndexOrThrow("type")),
                    configId = it.getInt(it.getColumnIndexOrThrow("config_id")),
                    name = it.getString(it.getColumnIndexOrThrow("name")),
                    highestQuality = it.getInt(it.getColumnIndexOrThrow("highest_quality")),
                    count = it.getInt(it.getColumnIndexOrThrow("count"))
                ))
            }
        }
        return list
    }
    
    fun findCollection(type: String, configId: Int): CollectionRecord? {
        val db = readableDatabase
        val cursor = db.query("collections", null, 
            "type = ? AND config_id = ?", 
            arrayOf(type, configId.toString()), 
            null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                CollectionRecord(
                    recordId = it.getLong(it.getColumnIndexOrThrow("record_id")),
                    type = it.getString(it.getColumnIndexOrThrow("type")),
                    configId = it.getInt(it.getColumnIndexOrThrow("config_id")),
                    name = it.getString(it.getColumnIndexOrThrow("name")),
                    highestQuality = it.getInt(it.getColumnIndexOrThrow("highest_quality")),
                    count = it.getInt(it.getColumnIndexOrThrow("count"))
                )
            } else null
        }
    }
    
    fun insertCollection(record: CollectionRecord) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("type", record.type)
            put("config_id", record.configId)
            put("name", record.name)
            put("highest_quality", record.highestQuality)
            put("count", record.count)
        }
        db.insert("collections", null, values)
    }
    
    fun updateCollection(record: CollectionRecord) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("highest_quality", record.highestQuality)
            put("count", record.count)
        }
        db.update("collections", values, "record_id = ?", arrayOf(record.recordId.toString()))
    }
    
    // ===== 统计操作 =====
    fun getStats(): PlayerStatistics? {
        val db = readableDatabase
        val cursor = db.query("stats", null, null, null, null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                PlayerStatistics(
                    totalPlant = it.getInt(it.getColumnIndexOrThrow("total_plant")),
                    totalHarvest = it.getInt(it.getColumnIndexOrThrow("total_harvest")),
                    totalEarned = it.getLong(it.getColumnIndexOrThrow("total_earned")),
                    totalSpent = it.getLong(it.getColumnIndexOrThrow("total_spent")),
                    playTime = it.getLong(it.getColumnIndexOrThrow("play_time"))
                )
            } else null
        }
    }
    
    fun updateStats(stats: PlayerStatistics) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("total_plant", stats.totalPlant)
            put("total_harvest", stats.totalHarvest)
            put("total_earned", stats.totalEarned)
            put("total_spent", stats.totalSpent)
            put("play_time", stats.playTime)
        }
        if (getStats() == null) {
            values.put("id", 1)
            db.insert("stats", null, values)
        } else {
            db.update("stats", values, "id = 1", null)
        }
    }
    
    // ===== 设置操作 =====
    fun getSettings(): GameSettings? {
        val db = readableDatabase
        val cursor = db.query("settings", null, null, null, null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                GameSettings(
                    soundEnabled = it.getInt(it.getColumnIndexOrThrow("sound_enabled")) == 1,
                    musicEnabled = it.getInt(it.getColumnIndexOrThrow("music_enabled")) == 1,
                    notificationsEnabled = it.getInt(it.getColumnIndexOrThrow("notifications_enabled")) == 1,
                    autoSaveInterval = it.getInt(it.getColumnIndexOrThrow("auto_save_interval"))
                )
            } else null
        }
    }
    
    fun updateSettings(settings: GameSettings) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("sound_enabled", if (settings.soundEnabled) 1 else 0)
            put("music_enabled", if (settings.musicEnabled) 1 else 0)
            put("notifications_enabled", if (settings.notificationsEnabled) 1 else 0)
            put("auto_save_interval", settings.autoSaveInterval)
        }
        if (getSettings() == null) {
            values.put("id", 1)
            db.insert("settings", null, values)
        } else {
            db.update("settings", values, "id = 1", null)
        }
    }
    
    // ===== 教程操作 =====
    fun getTutorialProgress(): List<Int> {
        val db = readableDatabase
        val cursor = db.query("tutorial_progress", null, "completed = 1", null, null, null, null)
        val list = mutableListOf<Int>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(it.getInt(it.getColumnIndexOrThrow("id")))
            }
        }
        return list
    }
    
    fun markTutorialComplete(stepId: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", stepId)
            put("completed", 1)
        }
        db.insertWithOnConflict("tutorial_progress", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }
    
    fun updateTutorialCurrentStep(stepId: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE tutorial_progress SET current_step = $stepId WHERE id = $stepId")
    }
    
    // ===== 日志操作 =====
    fun addLog(type: String, message: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("type", type)
            put("message", message)
            put("timestamp", System.currentTimeMillis())
        }
        db.insert("logs", null, values)
    }
}
