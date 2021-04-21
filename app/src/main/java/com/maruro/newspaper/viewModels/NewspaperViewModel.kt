package com.maruro.newspaper.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maruro.newspaper.enums.QueryEnums
import com.maruro.newspaper.models.ArticlesResponse
import com.maruro.newspaper.repositories.NewspaperRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewspaperViewModel(private val newspaperRepository: NewspaperRepository): ViewModel() {

    val error: LiveData<String>
    get() = newspaperRepository.error

    val articlesResponse: LiveData<ArticlesResponse>
    get() = newspaperRepository.articlesResponse

    val searchArticlesResponse: LiveData<ArticlesResponse>
        get() = newspaperRepository.searchArticlesResponse

    fun getArticles(
        keyword: String,
        keywordInTitle: String? = null,
        pageSize: Int = 50,
        page: Int = 1,
        sortBy: QueryEnums.SortBy = QueryEnums.SortBy.relevancy,
        sources: String? = null
    ){
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            newspaperRepository.getArticles(keyword, keywordInTitle, pageSize, page, sortBy, sources)
        }
    }

    fun getTopHeadlinesArticles(
        keyword: String,
        pageSize: Int = 50,
        page: Int = 1,
        country: QueryEnums.Country? = null,
        category: QueryEnums.Category? = null
    ){
        viewModelScope.launch(Dispatchers.IO){
            delay(2000)
            newspaperRepository.getTopHeadlinesArticles(keyword, pageSize, page, country, category)
        }
    }
}