package com.cjg.gallery.repository

import com.cjg.gallery.document.Gallery
import org.springframework.data.mongodb.repository.MongoRepository

interface GalleryRepository: MongoRepository<Gallery, String> {
    fun deleteByGalleryId(galleryId:Int)
}