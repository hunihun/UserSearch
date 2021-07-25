package com.hunihun.usersearch.main

import android.os.Bundle
import androidx.activity.viewModels
import com.hunihun.usersearch.BaseActivity
import com.hunihun.usersearch.R
import com.hunihun.usersearch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override val vm : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserve()
    }

    private fun initObserve() {
        vm.searchWord.observe(this) {
            if (it.isEmpty()) return@observe
            vm.searchUser()
        }

        vm.userList.observe(this) {
            if (it.isEmpty()) return@observe
        }
    }
}