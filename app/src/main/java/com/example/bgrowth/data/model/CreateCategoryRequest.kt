package com.example.bgrowth.data.model

data class CreateCategoryRequest(
    val name: String,
    val description: String = ""
)