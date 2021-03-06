package com.ittyo.githubtrendingrepo.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ittyo.githubtrendingrepo.R
import com.ittyo.githubtrendingrepo.repository.Repo

class TrendingRepoAdapter : RecyclerView.Adapter<TrendingRepoAdapter.ViewHolder>() {

    var expandPosition = -1
    private var repos = emptyList<Repo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_repo, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = repos[position]
        holder.itemView.setOnClickListener {
            val prevExpandPosition = expandPosition
            val newExpandPosition = if (expandPosition == position) -1 else position
            notifyItemChanged(prevExpandPosition)
            notifyItemChanged(newExpandPosition)
            expandPosition = newExpandPosition
        }

        holder.authorName.text = repo.author
        holder.repoName.text = repo.name
        holder.language.text = repo.language
        holder.description.text = "${repo.description}(${repo.repositoryUrl})"
        holder.stars.text = repo.stars.toString()
        holder.forks.text = repo.forks.toString()

        setLanguageDrawableColor(holder, repo.languageColor)
        Glide.with(holder.itemView.context)
            .load(repo.avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.avatarImage)

        holder.repoDetail.visibility = if (expandPosition == position) View.VISIBLE else View.GONE
        ViewCompat.setTranslationZ(holder.itemView, if (expandPosition == position) 5f else 0f)
    }

    private fun setLanguageDrawableColor(holder: ViewHolder, colorString: String) {
        val languageDrawable = holder.language.compoundDrawables[0]
        val languageColor = Color.parseColor(colorString)
        DrawableCompat.setTint(languageDrawable, languageColor)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    fun setTrendingRepo(repos: List<Repo>) {
        this.repos = repos
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var authorName: TextView = view.findViewById(R.id.text_author)
        var repoName: TextView = view.findViewById(R.id.text_repo_name)
        var avatarImage: ImageView = view.findViewById(R.id.image_user_avatar)
        var description: TextView = view.findViewById(R.id.text_description)
        var language: TextView = view.findViewById(R.id.text_language)
        var stars: TextView = view.findViewById(R.id.text_stars)
        var forks: TextView = view.findViewById(R.id.text_forks)
        var repoDetail: Group = view.findViewById(R.id.repo_detail)
    }
}