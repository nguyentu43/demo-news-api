package com.maruro.newspaper.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maruro.newspaper.api.ApiService
import com.maruro.newspaper.enums.QueryEnums
import com.maruro.newspaper.models.ArticlesResponse

class NewspaperRepository(val apiService: ApiService) {
    private val articlesResponseLiveData = MutableLiveData<ArticlesResponse>()
    private val searchArticlesResponseLiveData = MutableLiveData<ArticlesResponse>()
    private val errorLiveData = MutableLiveData<String>()

    val articlesResponse: LiveData<ArticlesResponse>
    get() = articlesResponseLiveData

    val searchArticlesResponse: LiveData<ArticlesResponse>
        get() = searchArticlesResponseLiveData

    val error: LiveData<String>
    get() = errorLiveData

    suspend fun getArticles(
            keyword: String,
            keywordInTitle: String? = null,
            pageSize: Int = 50,
            page: Int = 1,
            sortBy: QueryEnums.SortBy = QueryEnums.SortBy.relevancy,
            sources: String? = null
    ){
        try {
            val articlesResponse = apiService.getArticles(keyword, keywordInTitle, sortBy, pageSize, page, sources)
            searchArticlesResponseLiveData.postValue(articlesResponse)
        }
        catch (e: Exception){
            errorLiveData.postValue(e.message?.replace("HTTP ", ""))
        }
    }

    suspend fun getTopHeadlinesArticles(
        keyword: String,
        pageSize: Int = 50,
        page: Int = 1,
        country: QueryEnums.Country? = null,
        category: QueryEnums.Category? = null
    ){
        try {
            val articlesResponse = apiService.getTopHeadlinesArticles(keyword, pageSize, page, category, country)
            articlesResponseLiveData.postValue(articlesResponse)
        }
        catch (e: Exception){
            errorLiveData.postValue(e.message?.replace("HTTP ", ""))
        }
    }
}