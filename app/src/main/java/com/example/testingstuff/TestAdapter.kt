package com.example.testingstuff

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TestAdapter : RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    var gitHubRepos : List<GitHubRepo> = ArrayList()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val repoName : TextView = itemView.findViewById(R.id.repo_name)

        fun bind(pos : Int) {
            Log.d("repoName", "name: ${gitHubRepos[pos].name}")
            repoName.text = gitHubRepos[pos].name
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_laoyut,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("onBindViewHolder", "called")
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return gitHubRepos.size
    }
}