package com.cjg.gallery.service

import com.cjg.gallery.document.Gallery
import com.cjg.gallery.dto.GalleryDto
import com.cjg.gallery.dto.SearchParamDto
import com.cjg.gallery.repository.GalleryRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import org.springframework.data.mongodb.core.MongoTemplate
import java.time.LocalDateTime

class GalleryServiceTest : BehaviorSpec ({

    val mongoTemplate = mockk<MongoTemplate>(relaxed = true)
    val galleryRepository = mockk<GalleryRepository>(relaxed = true)

    @InjectMockKs
    val galleryService = GalleryService(galleryRepository, mongoTemplate)

    Given("리스트 : 기간1일"){

        galleryService.serverDomain = "https://cjg.com"
        galleryService.serverPort = "8080"
        galleryService.uploadPathPrefix = "/upload"

        val searchParamDto = SearchParamDto();
        searchParamDto.type = "video"
        searchParamDto.day = "day"

        val list  = mutableListOf<Gallery>();

        for(i in 1 .. 10){
            val gallery = Gallery();
            gallery.galleryId = i.toLong();
            gallery.regDate = LocalDateTime.now()
            list.add(gallery)
        }

        When("리스트 수행"){

            every{ mongoTemplate.count(any(), Gallery::class.java)} answers {20}
            every{ mongoTemplate.find(any(), Gallery::class.java)} answers {list}

            val result = galleryService.list(searchParamDto);

            Then("결과"){
                result["data"].shouldNotBeNull()
            }
        }
    }

    Given("리스트 : 기간1주"){

        galleryService.serverDomain = "https://cjg.com"
        galleryService.serverPort = "8080"
        galleryService.uploadPathPrefix = "/upload"

        val searchParamDto = SearchParamDto();
        searchParamDto.type = "video"
        searchParamDto.day = "week"

        val list  = mutableListOf<Gallery>();

        for(i in 1 .. 10){
            val gallery = Gallery();
            gallery.galleryId = i.toLong();
            gallery.regDate = LocalDateTime.now()
            list.add(gallery)
        }

        When("리스트 수행"){

            every{ mongoTemplate.count(any(), Gallery::class.java)} answers {20}
            every{ mongoTemplate.find(any(), Gallery::class.java)} answers {list}

            val result = galleryService.list(searchParamDto);

            Then("결과"){
                result["data"].shouldNotBeNull()
            }

        }
    }

    Given("리스트 : 기간1달"){

        galleryService.serverDomain = "https://cjg.com"
        galleryService.serverPort = "8080"
        galleryService.uploadPathPrefix = "/upload"

        val searchParamDto = SearchParamDto();
        searchParamDto.type = "video"
        searchParamDto.day = "month"

        val list  = mutableListOf<Gallery>();

        for(i in 1 .. 10){
            val gallery = Gallery();
            gallery.galleryId = i.toLong();
            gallery.regDate = LocalDateTime.now()
            list.add(gallery)
        }

        When("리스트 수행"){

            every{ mongoTemplate.count(any(), Gallery::class.java)} answers {20}
            every{ mongoTemplate.find(any(), Gallery::class.java)} answers {list}

            val result = galleryService.list(searchParamDto);

            Then("결과"){
                result["data"].shouldNotBeNull()
            }
        }
    }

    Given("리스트 : 기간1년"){

        galleryService.serverDomain = "https://cjg.com"
        galleryService.serverPort = "8080"
        galleryService.uploadPathPrefix = "/upload"

        val searchParamDto = SearchParamDto();
        searchParamDto.type = "video"
        searchParamDto.day = "year"

        val list  = mutableListOf<Gallery>();

        for(i in 1 .. 10){
            val gallery = Gallery();
            gallery.galleryId = i.toLong();
            gallery.regDate = LocalDateTime.now()
            list.add(gallery)
        }

        When("리스트 수행"){

            every{ mongoTemplate.count(any(), Gallery::class.java)} answers {20}
            every{ mongoTemplate.find(any(), Gallery::class.java)} answers {list}

            val result = galleryService.list(searchParamDto);

            Then("결과"){
                result["data"].shouldNotBeNull()
            }

        }
    }

    Given("리스트 : 기간 직접입력"){

        galleryService.serverDomain = "https://cjg.com"
        galleryService.serverPort = "8080"
        galleryService.uploadPathPrefix = "/upload"

        val searchParamDto = SearchParamDto();
        searchParamDto.type = "video"
        searchParamDto.day = "manual"
        searchParamDto.dateRange = "2024-05-01~2024-06-01"
        searchParamDto.pageNumber = 2

        val list  = mutableListOf<Gallery>();

        for(i in 1 .. 10){
            val gallery = Gallery();
            gallery.galleryId = i.toLong();
            gallery.regDate = LocalDateTime.now()
            list.add(gallery)
        }

        When("리스트 수행"){

            every{ mongoTemplate.count(any(), Gallery::class.java)} answers {20}
            every{ mongoTemplate.find(any(), Gallery::class.java)} answers {list}

            val result = galleryService.list(searchParamDto);

            Then("결과"){
                result["data"].shouldNotBeNull()
            }
        }
    }

    Given("checkListParam : 파라미터가 음수로 들어올 때"){
        val searchParamDto = SearchParamDto();
        searchParamDto.pageNumber = -1
        searchParamDto.pageSize = -1

        When("함수 실행"){
            galleryService.checkListParam(searchParamDto);

            Then("pageNumber, pageSize가 1로 변환"){
                searchParamDto.pageNumber shouldBe 1;
                searchParamDto.pageSize shouldBe 10;
            }
        }
    }

    Given("delete"){

        val galleryDto  = GalleryDto()
        galleryDto.galleryIds = arrayOf("1", "2")

        When("함수 실행"){
            val result = galleryService.delete(galleryDto);

            Then("검증"){
                result.get("message") shouldBe "success"

            }
        }
    }





})