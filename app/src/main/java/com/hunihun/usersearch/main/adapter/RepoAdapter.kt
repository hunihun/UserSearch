package com.hunihun.usersearch.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.hunihun.usersearch.R
import com.hunihun.usersearch.databinding.RepoItemBinding
import com.hunihun.usersearch.databinding.UserProfileItemBinding
import com.hunihun.usersearch.main.model.user.UserDetailData

class RepoAdapter(private val itemClickListener: (link: String) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val itemList: ArrayList<UserDetailData> = ArrayList()
    private lateinit var binding: ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            HEADER -> {
                binding = DataBindingUtil.inflate<RepoItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.user_profile_item,
                    parent,
                    false
                )
                HeaderViewHolder()
            }

            else -> {
                binding = DataBindingUtil.inflate<RepoItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.repo_item,
                    parent,
                    false
                )
                RepoViewHolder(itemClickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            HEADER -> {
                (holder as HeaderViewHolder).bind(itemList[position])
            }

            BODY -> {
                (holder as RepoViewHolder).bind(itemList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) HEADER else BODY
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addList(list: List<UserDetailData>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    inner class RepoViewHolder(
            private val itemClickListener: (link: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var repoBinding: RepoItemBinding = binding as RepoItemBinding
        private lateinit var link: String

        init {
            itemView.setOnClickListener {
                itemClickListener(link)
            }
        }

        fun bind(item: UserDetailData) {
            repoBinding.run {
                data = item
                executePendingBindings()
            }
            link = item.repoUrl
        }
    }

    inner class HeaderViewHolder : RecyclerView.ViewHolder(binding.root) {
        var userProfileItemBinding: UserProfileItemBinding = binding as UserProfileItemBinding

        fun bind(item: UserDetailData) {
            userProfileItemBinding.run {
                data = item
                executePendingBindings()
            }
        }
    }

    companion object {
        const val HEADER = 1
        const val BODY = 2
    }
}