package com.hunihun.usersearch.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hunihun.usersearch.BaseActivity
import com.hunihun.usersearch.R
import com.hunihun.usersearch.databinding.ActivityMainBinding
import com.hunihun.usersearch.main.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSearchActivity: BaseActivity<ActivityMainBinding, UserSearchViewModel>(R.layout.activity_main) {
    override val vm : UserSearchViewModel by viewModels()
    private val userAdapter = UserAdapter {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()
        initObserve()
    }

    private fun initAdapter() {
        binding.rvSearchList.run {
            adapter = userAdapter
            setHasFixedSize(true)
            addOnScrollListener(scrollListener)
        }
    }

    private fun initObserve() {
        vm.searchWord.observe(this) {
            clearDataList()
            if (it.isEmpty()) {
                userAdapter.notifyDataSetChanged()
                return@observe
            }
            vm.searchUser()
        }

        vm.userList.observe(this) {
            if (it.isEmpty()) return@observe
            userAdapter.addList(it)
        }

        vm.error.observe(this) {
            if (it.contains("403")) {
                Toast.makeText(this, R.string.error_403, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearDataList() {
        vm.page.initialize()
        vm.tempList.clear()
        userAdapter.clearList()
    }

    private var scrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val visibleItemCount = recyclerView.childCount
            val totalItemCount = recyclerView.layoutManager!!.itemCount
            val firstVisibleItemCount = (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()


            if (firstVisibleItemCount + visibleItemCount == totalItemCount) {
                if (vm.page.isMorePage && !vm.page.loadingData) {
                    vm.page.addPageNo()
                    vm.searchUser()
                }
            }
        }
    }
}