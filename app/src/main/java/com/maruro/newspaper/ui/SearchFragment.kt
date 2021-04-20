package com.maruro.newspaper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var preferencesWrapper: PreferencesWrapper

    private lateinit var newspaperViewModel: NewspaperViewModel
    private val sourcesChipGroup by lazy { requireView().findViewById<ChipGroup>(R.id.sourceChipGroup) }
    private var isLoading = false
    private var totalResult = -1
    private val recycleView by lazy{requireView().findViewById<RecyclerView>(R.id.searchRecycleView)}
    private val keywordEditText by lazy {requireView().findViewById<EditText>(R.id.keywordEditText)}
    private val searchButton by lazy {requireView().findViewById<Button>(R.id.searchButton)}
    private val adapter by lazy{ArticleAdapter()}
    private var keyword: String = ""
    private var sources: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        newspaperViewModel = ViewModelProvider(this, factory).get(NewspaperViewModel::class.java)
        newspaperViewModel.sourcesResponse.observe(viewLifecycleOwner, {
            for(source in it.sources){
                val chip = Chip(requireView().context).apply {
                    text = source.name
                }
                chip.setOnCheckedChangeListener { compoundButton, b ->
                    if(b){
                        if(sourcesChipGroup.checkedChipIds.size > MAX_CHIP){
                            chip.isChecked = false
                        }
                    }
                }
                sourcesChipGroup.addView(chip)
            }
        })

        //get sources
        val country = QueryEnums.Country.valueOf(preferencesWrapper
                                        .getValue(PreferencesWrapper.COUNTRY_SETTING, QueryEnums.Country.USA.toString()))
        newspaperViewModel.getSources(country = country)

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

        searchButton.setOnClickListener {
            val text = keywordEditText.text.toString()
            adapter.clearArticles()
            keyword = text
            sources = getSourcesFromChipGroup()
            isLoading = true
            adapter.showLoading()
            newspaperViewModel.getArticles(
                keyword,
                keyword,
                pageSize = PAGE_SIZE,
                sources = sources
            )
        }
    }

    private fun getSourcesFromChipGroup(): String{
        var sources = ""
        for(index in sourcesChipGroup.checkedChipIds){
            val text = (sourcesChipGroup.children.toList()[index] as Chip).text
            sources += ",$text"
        }
        return sources
    }

    private fun loadMore(){
        if(totalResult == -1 || adapter.itemCount == totalResult) return
        val page = adapter.itemCount / TopHeadlinesFragment.PAGE_SIZE
        newspaperViewModel.getArticles(keyword, keyword, page = page + 1, sources = sources)
    }

    companion object {
        const val TAG = "SearchFragment"
        const val MAX_CHIP = 20
        const val PAGE_SIZE = 15
    }
}