package com.maruro.newspaper.models

data class ArticlesResponse(val status: String,
                            val totalResults: Int,
                            val articles: List<Article>)
