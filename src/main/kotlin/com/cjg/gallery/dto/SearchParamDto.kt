package com.cjg.gallery.dto


data class SearchParamDto(
    var pageNumber:Int = 1,
    var pageSize:Int = 10,
    var type:String = "all",
    var day:String = "all",
    var dateRange:String = ""
)
