package com.cjg.gallery.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document
data class Gallery (

    @Id
    var id:String ?= null,

    @Field("galleryId")
    var galleryId:Long ?= null,

    @Field("mediaId")
    var mediaId:Long ?= null,

    @Field("type")
    var type:String ?= null,

    @Field("regDate")
    var regDate:LocalDateTime ?= null,

    @Field("thumbnailFilePath")
    var thumbnailFilePath:String ?= null,

    @Field("thumbnailFileName")
    var thumbnailFileName:String ?= null,

    @Field("encodingFilePath")
    var encodingFilePath:String ?= null,

    @Field("encodingFileName")
    var encodingFileName:String ?= null
)