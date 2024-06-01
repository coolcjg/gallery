package com.cjg.gallery.controller

import com.cjg.gallery.dto.GalleryDto
import com.cjg.gallery.dto.SearchParamDto
import com.cjg.gallery.service.GalleryService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GalleryController(
    private val galleryService: GalleryService,
) {
    @GetMapping("/gallery/list")
    fun list(searchParamDto : SearchParamDto): Map<String,Any>{
        return galleryService.list(searchParamDto)
    }

    @DeleteMapping("/gallery")
    fun delete(@RequestBody galleryDto : GalleryDto): Map<String,Any>{
        return galleryService.delete(galleryDto)
    }
}