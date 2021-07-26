package com.hunihun.usersearch.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hunihun.usersearch.R
import com.hunihun.usersearch.databinding.RepoItemBinding
import com.hunihun.usersearch.main.model.repo.ResponseGitHubRepoDataItem
import com.hunihun.usersearch.main.model.user.UserListData
import java.io.PrintStream

class RepoAdapter(private val itemClickListener: (userName: String) -> Unit): RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {
    private val itemList: ArrayList<ResponseGitHubRepoDataItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = DataBindingUtil.inflate<RepoItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.repo_item,
                parent,
                false
        )
        return RepoViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addList(list: List<ResponseGitHubRepoDataItem>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    inner class RepoViewHolder(
            private val binding: RepoItemBinding,
            private val itemClickListener: (link: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var link: String

        init {
            itemView.setOnClickListener {
                itemClickListener(link)
            }
        }

        fun bind(item: ResponseGitHubRepoDataItem) {
            Log.d("jsh","name >>> " + item.name)
            binding.run {
                data = item
                executePendingBindings()
            }
        }
    }
}