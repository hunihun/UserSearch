package com.hunihun.usersearch.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hunihun.usersearch.BaseActivity
import com.hunihun.usersearch.R
import com.hunihun.usersearch.databinding.ActivityMainBinding
import com.hunihun.usersearch.main.adapter.RepoAdapter
import com.hunihun.usersearch.main.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSearchActivity: BaseActivity<ActivityMainBinding, UserSearchViewModel>(R.layout.activity_main) {
    override val vm : UserSearchViewModel by viewModels()
    private val userAdapter by lazy {
        UserAdapter {
            binding.flSearchList.visibility = View.GONE
            imm.hideSoftInputFromWindow(binding.etSearchWord.windowToken, 0)

            clearDataList()
            vm.getUserData(it)
        }
    }

    private val repoAdapter by lazy {
        RepoAdapter {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }
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
            addOnScrollListener(userScrollListener)
        }

        binding.rvRepoList.run {
            adapter = repoAdapter
            setHasFixedSize(true)
            addOnScrollListener(repoScrollListener)
        }
    }

    private fun initObserve() {
        vm.searchWord.observe(this) {
            if (vm.page.loadingData) return@observe
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
            binding.flSearchList.visibility = View.VISIBLE
        }

        vm.error.observe(this) {
            if (it.contains("403")) {
                Toast.makeText(this, R.string.error_403, Toast.LENGTH_SHORT).show()
            }
        }

        vm.userDetailData.observe(this) {
            if (it == null) return@observe
            repoAdapter.addList(it)
        }
    }

    private fun clearDataList() {
        vm.page.initialize()
        vm.tempUserList.clear()
        vm.tempUserDetailDataList.clear()
        userAdapter.clearList()
        repoAdapter.clearList()
    }

    private var userScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
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

    private var repoScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val visibleItemCount = recyclerView.childCount
            val totalItemCount = recyclerView.layoutManager!!.itemCount
            val firstVisibleItemCount = (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()


            if (firstVisibleItemCount + visibleItemCount == totalItemCount) {
                if (vm.page.isMorePage && !vm.page.loadingData) {
                    vm.page.addPageNo()
                    vm.searchRepo()
                }
            }
        }
    }
}