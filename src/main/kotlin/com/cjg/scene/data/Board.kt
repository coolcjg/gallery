package com.cjg.scene.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document
data class Board (

    @Id
    var id:String ?= null,

    @Field("title")
    var title:String ?= null,

    @Field("content")
    var content:String ?= null,

    @Field("userId")
    var userId:String ?= null

)