package com.cjg.gallery.service

import com.cjg.gallery.document.Gallery
import com.cjg.gallery.dto.GalleryDto
import com.cjg.gallery.dto.SearchParamDto
import com.cjg.gallery.repository.GalleryRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
@Transactional(readOnly=true)
class GalleryService(
    private val galleryRepository:GalleryRepository,
) {
    private val log = LoggerFactory.getLogger(GalleryService::class.java)

    fun list(searchParamDto : SearchParamDto):Map<String, Any>{
        val result = HashMap<String, Any>()

        //파라미터 검증
        checkListParam(searchParamDto)

        //페이징 처리
        val pageable = PageRequest.of(searchParamDto.pageNumber-1, searchParamDto.pageSize, Sort.Direction.DESC, "regDate")
        val page = galleryRepository.findAll(pageable)

        //날짜 형식 변환
        val boardDtoList = mutableListOf<GalleryDto>()

        for(element:Gallery in page.content){

            val galleryDto = GalleryDto()
            galleryDto.galleryId = element.galleryId
            galleryDto.mediaId = element.mediaId
            galleryDto.type = element.type
            galleryDto.thumbnailFilePath = element.thumbnailFilePath
            galleryDto.thumbnailFileName = element.thumbnailFileName
            galleryDto.encodingFilePath = element.encodingFilePath
            galleryDto.encodingFileName = element.encodingFileName

            if(element.regDate != null){
                galleryDto.regDate =  element.regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }
            boardDtoList.add(galleryDto)
        }

        result.put("data", boardDtoList)

        return result
    }

    fun checkListParam(searchParamDto:SearchParamDto){

        //값 범위 확인
        if(searchParamDto.pageNumber <=0){
            searchParamDto.pageNumber = 1
        }

        if(searchParamDto.pageSize <= 0){
            searchParamDto.pageSize = 10
        }
    }
}