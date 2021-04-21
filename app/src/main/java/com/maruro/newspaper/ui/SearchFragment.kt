package com.maruro.newspaper.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maruro.newspaper.R
import com.maruro.newspaper.enums.QueryEnums
import com.maruro.newspaper.models.Article
import com.maruro.newspaper.recycleView.ArticleAdapter
import com.maruro.newspaper.utils.PreferencesWrapper
import com.maruro.newspaper.viewModels.NewspaperViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var preferencesWrapper: PreferencesWrapper

    private lateinit var newspaperViewModel: NewspaperViewModel
    private var isLoading = false
    private var totalResult = -1
    private val recycleView by lazy{requireView().findViewById<RecyclerView>(R.id.searchRecycleView)}
    private val keywordEditText by lazy {requireView().findViewById<EditText>(R.id.keywordEditText)}
    private val searchButton by lazy {requireView().findViewById<Button>(R.id.searchButton)}
    private val spinner by lazy {requireView().findViewById<Spinner>(R.id.spinner)}
    private val adapter: ArticleAdapter by lazy{ ArticleAdapter(object: ArticleAdapter.ArticleClickListener{
        override fun onArticleClick(article: Article) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.url)))
        }
    }) }
    private var keyword: String = ""
    private var sortedBy: String = "publishedAt"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        newspaperViewModel = ViewModelProvider(this, factory).get(NewspaperViewModel::class.java)

        //get sources
        val country = QueryEnums.Country.valueOf(preferencesWrapper
                                        .getValue(PreferencesWrapper.COUNTRY_SETTING, QueryEnums.Country.USA.toString()))

        ArrayAdapter.createFromResource(requireContext(), R.array.sortBy_arrays, android.R.layout.simple_spinner_item).also {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        //recycleView
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(context)

        //infinity scroll
        recycleView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if(!isLoading){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1){
                        loadMore()
                    }
                }
            }
        })

        //set observe
        newspaperViewModel.searchArticlesResponse.observe(viewLifecycleOwner, {
                response ->
            totalResult = response.totalResults
            adapter.addArticles(response.articles)
            isLoading = false
        })
        newspaperViewModel.error.observe(viewLifecycleOwner, {
            adapter.hideLoading()
            isLoading = false
        })

        searchButton.setOnClickListener {
            val text = keywordEditText.text.toString()
            adapter.clearArticles()
            keyword = text
            sortedBy = spinner.selectedItem.toString().decapitalize()
            if(text.isNotEmpty()){
                isLoading = true
                adapter.showLoading()
                newspaperViewModel.getArticles(
                        keyword,
                        keyword,
                        pageSize = PAGE_SIZE,
                        sortBy = QueryEnums.SortBy.valueOf(sortedBy)
                )
            }
            else{
                Toast.makeText(context, "Keyword not empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadMore(){
        if(totalResult == -1 || adapter.itemCount == totalResult) return
        val page = adapter.itemCount / TopHeadlinesFragment.PAGE_SIZE
        newspaperViewModel.getArticles(keyword, keyword, page = page + 1, sortBy = QueryEnums.SortBy.valueOf(sortedBy))
    }

    companion object {
        const val TAG = "SearchFragment"
        const val PAGE_SIZE = 15
    }
}