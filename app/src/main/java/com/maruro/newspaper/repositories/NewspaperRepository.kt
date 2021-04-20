package com.maruro.newspaper.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maruro.newspaper.api.ApiService
import com.maruro.newspaper.enums.QueryEnums
import com.maruro.newspaper.models.ArticlesResponse
import com.maruro.newspaper.models.SourcesResponse

class NewspaperRepository(val apiService: ApiService) {
    private val articlesResponseLiveData = MutableLiveData<ArticlesResponse>()
    private val sourcesResponseLiveData = MutableLiveData<SourcesResponse>()
    private val errorLiveData = MutableLiveData<String>()

    val articlesResponse: LiveData<ArticlesResponse>
    get() = articlesResponseLiveData

    val sourcesResponse: LiveData<SourcesResponse>
    get() = sourcesResponseLiveData

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
            articlesResponseLiveData.postValue(articlesResponse)
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

    suspend fun getSources(
        category: QueryEnums.Category? = null,
        country: QueryEnums.Country? = null
    ){
        try{
            val sourcesResponse = apiService.getSources(category, country)
            sourcesResponseLiveData.postValue(sourcesResponse)
        }
        catch (e: Exception){
            errorLiveData.postValue(e.message?.replace("HTTP ", ""))
        }
    }
}