package com.hunihun.usersearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<B: ViewDataBinding, VM: BaseViewModel>(
    private val layoutId: Int
): AppCompatActivity() {

    abstract val vm: VM
    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.run {
            setVariable(BR.vm, vm)
            lifecycleOwner = this@BaseActivity
        }
    }
}