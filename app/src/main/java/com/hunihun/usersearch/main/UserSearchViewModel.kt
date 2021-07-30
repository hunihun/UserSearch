package com.hunihun.usersearch.main

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hunihun.usersearch.base.BaseViewModel
import com.hunihun.usersearch.main.model.user.UserDetailData
import com.hunihun.usersearch.main.model.user.UserListData
import com.hunihun.usersearch.main.repository.UserSearchRepository
import com.hunihun.usersearch.util.Pagination
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val userSearchRepository: UserSearchRepository,
    val page: Pagination
): BaseViewModel() {
    private val tempUserList = mutableListOf<UserListData>()
    private val tempUserDetailDataList = mutableListOf<UserDetailData>()
    val searchWord = MutableLiveData("")
    private val _userList = MutableLiveData<List<UserListData>>()
    val userList: LiveData<List<UserListData>> = _userList

    private val _userDetailData = MutableLiveData<List<UserDetailData>>()
    val userDetailData: LiveData<List<UserDetailData>> = _userDetailData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _selectEvent = MutableLiveData<Unit>()
    val selectEvent: LiveData<Unit> = _selectEvent

    fun searchUser() {
        startLoading()
        addDisposable(userSearchRepository.searchUser(searchWord.value, page.pageNo)
            .map {
                if (it.items.isEmpty()) {
                    page.isMorePage = false
                }

                if (page.pageNo == 1) {
                    tempUserList.clear()
                }
                tempUserList.addAll(it.items)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                _userList.value = tempUserList
                finishLoading()
            }, {
                Log.d("jsh", "searchUser error >>> " + it.message)
                _error.value = it.message
                finishLoading()
            })
        )
    }

    fun searchRepo() {
        if (page.loadingData) return
        startLoading()
        addDisposable(userSearchRepository.searchRepo(searchWord.value, page.pageNo)
            .map {
                if (it.isEmpty()) {
                    page.isMorePage = false
                }
                it.map { data ->
                    val userDetailData = UserDetailData(
                        repoName = data.name,
                        starCount = data.stargazers_count,
                        pushedAt = data.pushed_at,
                        repoUrl = data.html_url
                    )
                    tempUserDetailDataList.add(userDetailData)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                _userDetailData.value = tempUserDetailDataList
                finishLoading()
            }, {
                Log.d("jsh", "searchRepo error >>> " + it.message)
                finishLoading()
            })
        )
    }

    fun getUserData(userId: String) {
        if (userId.isEmpty()) return
        _selectEvent.value = Unit
        startLoading()
            addDisposable(Observable.zip(
                // first api = search repository
                userSearchRepository.searchRepo(userId, page.pageNo)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        if (it.isEmpty()) {
                            page.isMorePage = false
                        }
                        it.map { data ->
                            val userDetailData = UserDetailData(
                                repoName = data.name,
                                starCount = data.stargazers_count,
                                pushedAt = data.pushed_at,
                                repoUrl = data.html_url
                            )
                            tempUserDetailDataList.add(userDetailData)
                        }
                    }
                    .toObservable(),
                // second api = get user profile data
                userSearchRepository.getUserProfile(userId)
                    .map {
                        val userDetailData = UserDetailData(
                            bio = it.bio ?: "",
                            login = it.login ?: "",
                            name = it.name ?: "",
                            profilePath = it.avatar_url ?: ""
                        )
                        tempUserDetailDataList.add(0, userDetailData)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable(),
                // result
                { _: List<Boolean>, _: Unit ->
                    _userDetailData.value = tempUserDetailDataList
                    finishLoading()
                }
            ).subscribeOn(Schedulers.newThread())
                .onErrorReturn {
                    Log.d("jsh", "getUserData error >>> " + it.message)
                    _error.value = it.message
                    finishLoading()
                }
                .subscribe())
    }

    private fun startLoading() {
        page.loadingData = true
        progressVisible.value = View.VISIBLE
    }

    private fun finishLoading() {
        page.loadingData = false
        progressVisible.value = View.GONE
    }


    fun clearDataList() {
        page.initialize()
        tempUserList.clear()
        tempUserDetailDataList.clear()
    }
}