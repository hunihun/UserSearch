package com.hunihun.usersearch.main

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hunihun.usersearch.BaseViewModel
import com.hunihun.usersearch.main.model.UserListData
import com.hunihun.usersearch.main.repository.UserSearchRepository
import com.hunihun.usersearch.util.Pagination
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val userSearchRepository: UserSearchRepository
): BaseViewModel() {
    val searchWord = MutableLiveData("")
    val tempList = mutableListOf<UserListData>()
    var page = Pagination()

    private val _userList = MutableLiveData<List<UserListData>>()
    val userList: LiveData<List<UserListData>> = _userList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun searchUser() {
        startLoading()
        addDisposable(userSearchRepository.searchUser(searchWord.value, page.pageNo)
            .map {
                if (it.items.isEmpty()) {
                    page.isMorePage = false
                }
                tempList.addAll(it.items)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                _userList.value = tempList
                finishLoading()
            }, {
                Log.d("jsh","error >>> " + it.message)
                _error.value = it.message
                finishLoading()
            }))
    }

    private fun startLoading() {
        page.loadingData = true
        progressVisible.value = View.VISIBLE
    }

    private fun finishLoading() {
        page.loadingData = false
        progressVisible.value = View.GONE
    }
}