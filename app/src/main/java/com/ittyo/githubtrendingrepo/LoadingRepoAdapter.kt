package com.ittyo.githubtrendingrepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class LoadingRepoAdapter: RecyclerView.Adapter<LoadingRepoAdapter.ViewHolder>() {

    private val placeholderCount = 8

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_placeholder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // nothing
    }

    override fun getItemCount(): Int {
        return placeholderCount
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}