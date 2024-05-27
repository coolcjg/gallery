package com.cjg.gallery.service

import com.cjg.gallery.document.Gallery
import com.cjg.gallery.dto.GalleryDto
import com.cjg.gallery.dto.SearchParamDto
import com.cjg.gallery.repository.GalleryRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
@Transactional(readOnly=true)
class GalleryService(
    private val galleryRepository:GalleryRepository,
    val mongoTemplate: MongoTemplate
) {

    @Value("\${server.port}")
    lateinit var serverPort : String

    @Value("\${serverDomain}")
    lateinit var serverDomain : String

    @Value("\${uploadPathPrefix}")
    lateinit var uploadPathPrefix : String

    fun list(searchParamDto : SearchParamDto):Map<String, Any>{

        val result = HashMap<String, Any>()

        //파라미터 검증
        checkListParam(searchParamDto)

        //페이징 처리
        val pageable = PageRequest.of(searchParamDto.pageNumber-1, searchParamDto.pageSize, Sort.Direction.DESC, "regDate")

        //MongoTemplate
        val query = Query()

        if(searchParamDto.type != "all"){
            query.addCriteria(Criteria.where("type").`is`(searchParamDto.type));
        }

        if(searchParamDto.day != "all"){

            if(searchParamDto.day == "day"){
                query.addCriteria(
                    Criteria.where("regDate").gte(LocalDateTime.now().minusDays(1)).lte(LocalDateTime.now())
                )
            }else if(searchParamDto.day == "week"){
                query.addCriteria(
                    Criteria.where("regDate").gte(LocalDateTime.now().minusWeeks(1)).lte(LocalDateTime.now())
                )
            }else if(searchParamDto.day == "month"){
                query.addCriteria(
                    Criteria.where("regDate").gte(LocalDateTime.now().minusMonths(1)).lte(LocalDateTime.now())
                )
            }else if(searchParamDto.day == "year") {
                query.addCriteria(
                    Criteria.where("regDate").gte(LocalDateTime.now().minusYears(1)).lte(LocalDateTime.now())
                )
            }else{
                val cal = Calendar.getInstance();
                val sdf = SimpleDateFormat("yyyy-MM-dd");
                val sdf2 = SimpleDateFormat("yyyy-MM-dd 00:00:00");

                val start = searchParamDto.dateRange.split("~")[0] + " 00:00:00"

                var end = searchParamDto.dateRange.split("~")[1]
                val date = sdf.parse(end);
                cal.setTime(date)
                cal.add(Calendar.DATE, 1)
                end = sdf2.format(cal.getTime())

                val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val startTime : LocalDateTime  = LocalDateTime.parse(start, dtf)
                val endTime : LocalDateTime  = LocalDateTime.parse(end, dtf)

                query.addCriteria(
                    Criteria.where("regDate").gte(startTime).lt(endTime)
                )

            }

        }

        val count = mongoTemplate.count(query, Gallery::class.java)
        val list:List<Gallery> = mongoTemplate.find(query.with(pageable), Gallery::class.java)
        val page = PageImpl(list, pageable, count)

        //날짜 형식 변환
        val boardDtoList = mutableListOf<GalleryDto>()

        for(element:Gallery in page.content){

            val galleryDto = GalleryDto()
            galleryDto.galleryId = element.galleryId
            galleryDto.mediaId = element.mediaId
            galleryDto.type = element.type

            galleryDto.thumbnailFileName = element.thumbnailFileName
            galleryDto.thumbnailFileUrl = serverDomain + ":" + serverPort + uploadPathPrefix + element.thumbnailFilePath + element.thumbnailFileName

            galleryDto.encodingFileName = element.encodingFileName
            galleryDto.encodingFileUrl = serverDomain + ":" + serverPort + uploadPathPrefix  + element.encodingFilePath + element.encodingFileName

            if(element.regDate != null){
                galleryDto.regDate =  element.regDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }
            boardDtoList.add(galleryDto)
        }

        result.put("data", boardDtoList)
        result.put("prevPage", getPrevPageInfo(searchParamDto, getListUrl(serverDomain, serverPort)))
        result.put("nextPage", getNextPageInfo(searchParamDto, page.totalPages, getListUrl(serverDomain, serverPort)))
        result.put("lastPage", getLastPageInfo(searchParamDto, page.totalPages, getListUrl(serverDomain, serverPort)))

        return result
    }

    fun getPrevPageInfo(searchParamDto: SearchParamDto, listUrl : String) : String{
        var prevPageUrl = ""
        if(searchParamDto.pageNumber >= 2){
            prevPageUrl = listUrl + "pageNumber=${searchParamDto.pageNumber-1}&pageSize=${searchParamDto.pageSize}"
        }
        return prevPageUrl
    }

    fun getNextPageInfo(searchParamDto: SearchParamDto, totalPages:Int, listUrl : String) : String{
        var nextPageUrl = ""
        if(searchParamDto.pageNumber < totalPages){
            nextPageUrl = listUrl + "pageNumber=${searchParamDto.pageNumber+1}&pageSize=${searchParamDto.pageSize}"
        }
        return nextPageUrl
    }

    fun getLastPageInfo(searchParamDto: SearchParamDto, totalPages:Int, listUrl : String) : String{
        return listUrl + "pageNumber=${totalPages}&pageSize=${searchParamDto.pageSize}"

    }

    fun getListUrl(serverDomain : String, serverPort:String):String{
        return "$serverDomain:$serverPort/gallery/list?"
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