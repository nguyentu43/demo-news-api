package com.maruro.newspaper.recycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maruro.newspaper.R
import com.maruro.newspaper.models.Article

class ArticleAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val articles = mutableListOf<Article?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ARTICLE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
            return ArticleViewHolder(view)
        }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
        return LoadingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ArticleViewHolder){
            holder.bind(articles[position]!!)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun getItemViewType(position: Int): Int = if(articles[position] is Article) ARTICLE_ITEM else LOADING_ITEM

    fun addArticles(articleList: List<Article>){
        hideLoading()
        val size = articles.size
        articles.addAll(articleList)
        notifyItemRangeInserted(size, articles.size)
    }

    fun clearArticles(){
        val size = articles.size
        articles.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun showLoading(){
        articles.add(null);
        notifyItemRangeInserted(articles.size - 1, articles.size)
    }

    fun hideLoading(){
        if(articles.isNotEmpty() && articles.last() == null){
            val size = articles.size;
            articles.removeAt(size - 1)
            notifyItemRangeRemoved(size - 1, 1)
        }
    }

    companion object{
        const val ARTICLE_ITEM = 1
        const val LOADING_ITEM = 0
    }

    class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val sourceTextView: TextView by lazy{
            itemView.findViewById(R.id.sourceTextView)
        }
        private val publishedAtTextView: TextView by lazy{
            itemView.findViewById(R.id.publishedAtTextView)
        }
        private val imageView: ImageView by lazy{
            itemView.findViewById(R.id.imageView)
        }
        private val titleTextView: TextView by lazy{
            itemView.findViewById(R.id.titleTextView)
        }
        private val descriptionTextView: TextView by lazy{
            itemView.findViewById(R.id.descriptionTextView)
        }
        private val authorTextView: TextView by lazy{
            itemView.findViewById(R.id.authorTextView)
        }
        private val openButton: Button by lazy{
            itemView.findViewById(R.id.openButton)
        }

        fun bind(article: Article){
            sourceTextView.text = article.source.name
            publishedAtTextView.text = article.publishedAt
            Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .into(imageView)
            titleTextView.text = article.title
            descriptionTextView.text = article.description
            authorTextView.text = article.author
        }
    }

    class LoadingViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    }

    interface ArticleClickListener{
        fun onArticleClick(article: Article)
    }
}