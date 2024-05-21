package com.cjg.gallery.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값인 필드 제외
data class GalleryDto(
    var galleryId: Long ?=null,
    var mediaId: Long ?=null,
    var type : String ?= null,
    var regDate: String?=null,
    var thumbnailFilePath : String ?= null,
    var thumbnailFileName : String ?= null,
    var encodingFilePath : String ?= null,
    var encodingFileName : String ?= null,

    var thumbnailFileUrl : String ? = null,
    var encodingFileUrl : String ?= null
)
