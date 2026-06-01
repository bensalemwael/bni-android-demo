package com.bni.sifipdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bni.sifipdemo.ui.dashboard.DashboardViewModel
import com.bni.sifipdemo.ui.login.LoginViewModel
import com.bni.sifipdemo.ui.transfer.TransferViewModel

class LoginViewModelFactory(private val app: BniApplication) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(
            sifipApi = app.sifipMock,
            mockService = app.sifipMock,
        ) as T
    }
}

class DashboardViewModelFactory(private val app: BniApplication) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(app.bankRepository) as T
    }
}

class TransferViewModelFactory(
    private val app: BniApplication,
    private val msisdn: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransferViewModel(
            sifipApi = app.sifipMock,
            userMsisdn = msisdn,
        ) as T
    }
}
