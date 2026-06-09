package com.farmlife.app

import android.app.Application
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.repository.FarmLifeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * FarmLifeApplication - 全局应用入口
 */
class FarmLifeApplication : Application() {
    lateinit var repository: FarmLifeRepository
    lateinit var engine: FarmEngine

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        instance = this
        repository = FarmLifeRepository(this)
        engine = FarmEngine(repository)

        // 异步初始化引擎
        scope.launch {
            engine.initialize()
        }
    }

    companion object {
        @Volatile
        private var instance: FarmLifeApplication? = null

        fun getInstance(): FarmLifeApplication = instance
            ?: throw IllegalStateException("Application not initialized")
    }
}
