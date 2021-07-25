package com.hunihun.usersearch.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hunihun.usersearch.BaseViewModel
import com.hunihun.usersearch.main.model.UserList
import com.hunihun.usersearch.main.repository.UserSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSearchRepository: UserSearchRepository
): BaseViewModel() {
    val searchWord = MutableLiveData("")
    private val page = 1

    private val _userList = MutableLiveData<ArrayList<UserList>>()
    val userList: LiveData<ArrayList<UserList>> = _userList

    fun searchUser() {
        addDisposable(userSearchRepository.searchUser(searchWord.value, page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                _userList.value?.addAll(it.userLists)
            }, {
                Log.d("jsh", "fail >>> " + it.message)
            }))
    }
}