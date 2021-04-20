package com.maruro.newspaper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.maruro.newspaper.R
import com.maruro.newspaper.enums.QueryEnums
import com.maruro.newspaper.recycleView.ArticleAdapter
import com.maruro.newspaper.utils.PreferencesWrapper
import com.maruro.newspaper.viewModels.NewspaperViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [TopHeadlinesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopHeadlinesFragment : DaggerFragment() {

    private val categoryGroup: ChipGroup by lazy{
        requireView().findViewById(R.id.categoryGroup)
    }
    private val recycleView: RecyclerView by lazy { requireView().findViewById(R.id.searchRecycleView) }
    private val adapter: ArticleAdapter by lazy{ ArticleAdapter() }

    @Inject
    lateinit var preferencesWrapper: PreferencesWrapper
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var newspaperViewModel: NewspaperViewModel
    private var category: QueryEnums.Category? = null
    private var totalResult = -1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_headlines, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        newspaperViewModel = ViewModelProvider(this, factory).get(NewspaperViewModel::class.java)
        //create category chips
        for (category in QueryEnums.Category.values()){
            val chip = layoutInflater.inflate(R.layout.chip_single_layout, categoryGroup, false) as Chip
            chip.text = category.name.capitalize()
            categoryGroup.addView(chip)
        }
        for(chip in categoryGroup.children){
            chip.setOnClickListener{
                val text = (chip as Chip).text.toString().decapitalize()
                if(text == "all") {
                    category = null
                } else {
                    category = QueryEnums.Category.valueOf(text)
                }
                adapter.clearArticles()
                loadMore()
            }
        }

        //init recycleView
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
        newspaperViewModel.articlesResponse.observe(viewLifecycleOwner, {
            response ->
            totalResult = response.totalResults
            adapter.addArticles(response.articles)
            isLoading = false
        })
        newspaperViewModel.error.observe(viewLifecycleOwner, {
            adapter.hideLoading()
            isLoading = false
        })

        //init data
        initGetArticles()
    }

    private fun initGetArticles() {
        getArticles(null)
    }

    private fun loadMore(){
        if(totalResult == -1 || adapter.itemCount == totalResult) return
        val page = adapter.itemCount / PAGE_SIZE
        getArticles(category = category, page = page + 1, pageSize = PAGE_SIZE)
    }

    private fun getArticles(category: QueryEnums.Category?, page: Int = 1, pageSize: Int = PAGE_SIZE){
        isLoading = true
        adapter.showLoading()
        val country = preferencesWrapper.getValue(PreferencesWrapper.COUNTRY_SETTING, QueryEnums.Country.USA.name)
        newspaperViewModel.getTopHeadlinesArticles("", pageSize, page, QueryEnums.Country.valueOf(country))
    }

    companion object {
        const val TAG = "TopHeadlinesFragment"
        const val PAGE_SIZE = 15
    }
}