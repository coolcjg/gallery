package com.cjg.scene.dto

data class BoardDto(
    var category: String ?=null,
    var title: String ?=null,
    var content : String ?= null,
    var userId: String ?=null,
    var createDate : String ?= null,


    var pageNumber:Int = 1,
    var pageSize:Int = 10

)
