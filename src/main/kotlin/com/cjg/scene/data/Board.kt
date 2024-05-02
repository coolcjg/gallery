package com.cjg.scene.data

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document
data class Board (

    @Id
    var id:String ?= null,

    @Field("title")
    var title:String ?= null,

    @Field("content")
    var content:String ?= null,

    @Field("userId")
    var userId:String ?= null,

    @Field("category")
    var category:String ?= null,

    @CreatedDate
    val createDate: LocalDateTime ?= null



)