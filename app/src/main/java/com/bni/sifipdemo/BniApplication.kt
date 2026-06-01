package com.bni.sifipdemo

import android.app.Application
import com.bni.sifipdemo.data.mock.MockScenario
import com.bni.sifipdemo.data.mock.SifipMockService
import com.bni.sifipdemo.data.repository.BankRepository

/**
 * Composition root for the demo app.
 *
 * The SIFIP mock is the only "production-like" component held here so that
 * a future swap to the real Spring Boot SIFIP backend is one line:
 *     `val sifipApi: SifipApi = RetrofitSifipClient(BuildConfig.SIFIP_BASE_URL)`
 */
class BniApplication : Application() {

    lateinit var sifipMock: SifipMockService
        private set

    lateinit var bankRepository: BankRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val initial = runCatching {
            MockScenario.valueOf(BuildConfig.DEFAULT_SIFIP_SCENARIO)
        }.getOrDefault(MockScenario.ALL_OK)

        sifipMock = SifipMockService(initial)
        bankRepository = BankRepository()
    }
}
