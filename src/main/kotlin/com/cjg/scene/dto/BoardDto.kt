package com.cjg.scene.dto

data class BoardDto(
    val category: String,
    val title: String,
    val content : String ?= null,
    val userId: String,
)
