package com.maruro.newspaper.api

import com.maruro.newspaper.BuildConfig
import com.maruro.newspaper.enums.QueryEnums
import com.maruro.newspaper.models.ArticlesResponse
import com.maruro.newspaper.models.SourcesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("X-Api-Key: ${BuildConfig.NEWS_API}")
    @GET("everything")
    suspend fun getArticles(
                            @Query("q") keyword: String,
                            @Query("qInTitle") keywordInTitle: String? = null,
                            @Query("sortBy") sortBy: QueryEnums.SortBy = QueryEnums.SortBy.publishedAt,
                            @Query("pageSize") pageSize: Int = 50,
                            @Query("page") page: Int = 1,
                            @Query("sources") sources: String? = null
    ): ArticlesResponse

    @Headers("X-Api-Key: ${BuildConfig.NEWS_API}")
    @GET("top-headlines")
    suspend fun getTopHeadlinesArticles(
                            @Query("q") keyword: String? = "",
                            @Query("pageSize") pageSize: Int = 50,
                            @Query("page") page: Int = 1,
                            @Query("category") category: QueryEnums.Category? = null,
                            @Query("country") country: QueryEnums.Country? = null,

    ): ArticlesResponse

    @Headers("X-Api-Key: ${BuildConfig.NEWS_API}")
    @GET("sources")
    suspend fun getSources(
        @Query("category") category: QueryEnums.Category? = null,
        @Query("country") country: QueryEnums.Country? = null
    ): SourcesResponse
}