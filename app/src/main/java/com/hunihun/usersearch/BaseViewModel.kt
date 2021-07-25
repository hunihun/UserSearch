package com.hunihun.usersearch

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel: ViewModel() {
    private val composeDisposable = CompositeDisposable()

    override fun onCleared() {
        composeDisposable.dispose()
        super.onCleared()
    }

    fun addDisposable(disposable: Disposable) {
        composeDisposable.add(disposable)
    }
}