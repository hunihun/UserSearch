package com.hunihun.usersearch.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hunihun.usersearch.R
import com.hunihun.usersearch.databinding.UserItemBinding
import com.hunihun.usersearch.main.model.user.UserListData

class UserAdapter(private val itemClickListener: (userName: String) -> Unit): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val itemList: ArrayList<UserListData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = DataBindingUtil.inflate<UserItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.user_item,
            parent,
            false
        )
        return UserViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addList(list: List<UserListData>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        itemList.clear()
    }

    inner class UserViewHolder(
        private val binding: UserItemBinding,
        private val itemClickListener: (userName: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var userId = ""

        init {
            itemView.setOnClickListener {
                itemClickListener(userId)
            }
        }

        fun bind(userList: UserListData) {
            binding.run {
                data = userList.login
                executePendingBindings()
            }

            userId = userList.login
        }
    }
}