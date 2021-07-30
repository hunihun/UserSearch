package com.hunihun.usersearch.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
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
import java.util.*

@AndroidEntryPoint
class UserSearchActivity: BaseActivity<ActivityMainBinding, UserSearchViewModel>(R.layout.activity_main) {
    override val vm : UserSearchViewModel by viewModels()
    private var searchDelay: TimerTask? = null
    private var isUserClick = false
    private val userAdapter by lazy {
        UserAdapter {
            isUserClick = true
            binding.etSearchWord.setText(it)
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

        binding.etSearchWord.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    vm.getUserData(vm.searchWord.value!!)
                    return true
                }
                return false
            }
        })
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

            // 사용자 리스트에서 선택했을 경우에는 조회 안되게 처리
            if (isUserClick) {
                isUserClick = false
                return@observe
            }
            searchDelay?.cancel()
            vm.clearDataList()
            if (it.isEmpty()) {
                userAdapter.notifyDataSetChanged()
                return@observe
            }
            // 0.2초 후에 검색한다.
            searchDelay = object : TimerTask() {
                override fun run() {
                    try {
                        runOnUiThread { vm.searchUser() }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            val timer = Timer()
            timer.schedule(searchDelay, 200)
        }

        vm.userList.observe(this) {
            if (it.isEmpty()) {
                vm.clearDataList()
                userAdapter.notifyDataSetChanged()
                return@observe
            }
            binding.flSearchList.visibility = View.VISIBLE
            userAdapter.addList(it)
        }

        vm.error.observe(this) {
            when {
                it.contains("403") -> {
                    Toast.makeText(this, R.string.error_403, Toast.LENGTH_SHORT).show()
                }
                it.contains("404") -> {
                    Toast.makeText(this, R.string.error_404, Toast.LENGTH_SHORT).show()
                }
                it.contains("401") -> {
                    Toast.makeText(this, R.string.error_404, Toast.LENGTH_SHORT).show()
                }
            }
        }

        vm.userDetailData.observe(this) {
            if (it == null) return@observe
            repoAdapter.addList(it)
        }

        vm.selectEvent.observe(this) {
            vm.clearDataList()
            binding.flSearchList.visibility = View.GONE
            imm.hideSoftInputFromWindow(binding.etSearchWord.windowToken, 0)
        }
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