package com.maruro.newspaper.enums

open class QueryEnums {
    enum class SortBy{
        relevancy, popularity, publishedAt;
        override fun toString(): String {
            return name;
        }
    }

    enum class Category{
        business, entertainment, general, health, science, sports, technology;
        override fun toString(): String {
            return name;
        }
    }

    enum class Country{
        Australia{
            override fun toString(): String {
                return "au"
            }
        },
        USA{
            override fun toString(): String {
                return "us"
            }
        },
        China{
            override fun toString(): String {
                return "cn"
            }
        },
        Japan{
            override fun toString(): String {
                return "jp"
            }
        },
        England{
            override fun toString(): String {
                return "gb"
            }
        }
    }

    enum class Error {
        E400, E401, E426, E429, E500;

        override fun toString(): String =
            when (name) {
                "E400" -> "Bad Request"
                "E401" -> "Unauthorized"
                "E426" -> "Error"
                "E429" -> "Too Many Requests"
                "E500" -> "Server Error"
                else -> ""
            }
    }
}