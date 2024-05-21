package com.cjg.gallery.service

import com.cjg.gallery.document.Gallery
import com.cjg.gallery.dto.GalleryDto
import com.cjg.gallery.dto.SearchParamDto
import com.cjg.gallery.repository.GalleryRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BoardServiceTest : BehaviorSpec({

    val galleryRepository = mockk<GalleryRepository>()

    @InjectMockKs
    val galleryService = GalleryService(galleryRepository)

    galleryService.serverDomain = "http://localhost"
    galleryService.serverPort = "7001"
    galleryService.uploadPathPrefix = "/upload"


    Given("list"){

        When("올바른 데이터 입력 시"){
            val searchParamDto = SearchParamDto(2, 2)
            val pageable = PageRequest.of(searchParamDto.pageNumber-1, searchParamDto.pageSize, Sort.Direction.DESC, "regDate")

            val gallery1 = Gallery()
            gallery1.id = "4"
            gallery1.galleryId=7
            gallery1.mediaId=106
            gallery1.type="video"
            gallery1.regDate = LocalDateTime.of(2024, 5, 1, 15, 0, 0)
            gallery1.thumbnailFilePath = "/encoding/2024/05/19/"
            gallery1.thumbnailFileName = "106.jpg"
            gallery1.encodingFilePath = "/encoding/2024/05/19/"
            gallery1.encodingFileName = "106.mp4"

            val gallery2 = Gallery()
            gallery2.id = "3"
            gallery2.galleryId=6
            gallery2.mediaId=105
            gallery2.type="image"
            gallery2.regDate = LocalDateTime.of(2024, 5, 1, 14, 0, 0)
            gallery2.thumbnailFilePath = "/encoding/2024/05/19/"
            gallery2.thumbnailFileName = "105.jpg"
            gallery2.encodingFilePath = "/encoding/2024/05/19/"
            gallery2.encodingFileName = "105.jpg"

            val gallery3 = Gallery()
            gallery3.id = "2"
            gallery3.galleryId=5
            gallery3.mediaId=104
            gallery3.type="image"
            gallery3.regDate = LocalDateTime.of(2024, 5, 1, 13, 0, 0)
            gallery3.thumbnailFilePath = "/encoding/2024/05/19/"
            gallery3.thumbnailFileName = "104.jpg"
            gallery3.encodingFilePath = "/encoding/2024/05/19/"
            gallery3.encodingFileName = "104.jpg"

            val gallery4 = Gallery()
            gallery4.id = "1"
            gallery4.galleryId=4
            gallery4.mediaId=103
            gallery4.type="image"
            gallery4.regDate = LocalDateTime.of(2024, 5, 1, 12, 0, 0)
            gallery4.thumbnailFilePath = "/encoding/2024/05/19/"
            gallery4.thumbnailFileName = "103.jpg"
            gallery4.encodingFilePath = "/encoding/2024/05/19/"
            gallery4.encodingFileName = "103.jpg"

            val list : MutableList<Gallery> = mutableListOf(gallery1, gallery2)
            val page: Page<Gallery> = PageImpl(list, pageable, 6)

            every{galleryRepository.findAll(pageable)} returns page
            val result = galleryService.list(searchParamDto)

            Then("원래 값 출력"){
                val data = result["data"] as List<GalleryDto>

                data.get(0).galleryId shouldBe gallery1.galleryId
                data.get(0).mediaId shouldBe gallery1.mediaId
                data.get(0).type shouldBe gallery1.type
                data.get(0).regDate shouldBe gallery1.regDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                data.get(0).thumbnailFileName shouldBe gallery1.thumbnailFileName
                data.get(0).encodingFileName shouldBe gallery1.encodingFileName

                data.get(0).thumbnailFileUrl shouldBe (galleryService.serverDomain + ":" + galleryService.serverPort + galleryService.uploadPathPrefix
                                                        + gallery1.thumbnailFilePath + gallery1.thumbnailFileName)

                data.get(0).encodingFileUrl shouldBe (galleryService.serverDomain + ":" + galleryService.serverPort + galleryService.uploadPathPrefix
                                                        + gallery1.encodingFilePath + gallery1.encodingFileName)

                result["prevPage"] shouldBe "http://localhost:7001/gallery/list?pageNumber=1&pageSize=2"
                result["nextPage"] shouldBe "http://localhost:7001/gallery/list?pageNumber=3&pageSize=2"
                result["lastPage"] shouldBe "http://localhost:7001/gallery/list?pageNumber=3&pageSize=2"

            }
        }
    }

    Given("getPrevPageInfo"){

        When("현재 페이지가 2 이상일 때"){
            val searchParamDto =  SearchParamDto()
            searchParamDto.pageNumber = 2

            val result = galleryService.getPrevPageInfo(searchParamDto, "http://localhost:8080/gallery/list?")

            Then("이전 페이지가 출력"){
                result shouldBe "http://localhost:8080/gallery/list?pageNumber=${searchParamDto.pageNumber-1}&pageSize=${searchParamDto.pageSize}"
            }
        }

        When("현재 페이지가 1일 때"){
            val searchParamDto =  SearchParamDto()

            val result = galleryService.getPrevPageInfo(searchParamDto, "http://localhost:8080/gallery/list?")

            Then("출력 없음"){
                result shouldBe ""
            }
        }

    }

    Given("getNextPageInfo"){

        When("현재 페이지가 마지막페이지가 아닐 때"){

            val searchParamDto = SearchParamDto(1, 10)
            val totalPages = 2
            val listUrl = "http://localhost:8080/gallery/list?"

            val result = galleryService.getNextPageInfo(searchParamDto, totalPages, listUrl)

            Then("마지막 페이지 출력"){
                result shouldBe "http://localhost:8080/gallery/list?pageNumber=${totalPages}&pageSize=${searchParamDto.pageSize}"
            }
        }

        When("현재 페이지가 마지막 페이지일 때"){

            val searchParamDto = SearchParamDto(1, 10)
            val totalPages = 1
            val listUrl = "http://localhost:8080/gallery/list?"

            val result = galleryService.getNextPageInfo(searchParamDto, totalPages, listUrl)

            Then("빈 값 출력"){
                result shouldBe ""
            }
        }
    }

    Given("getLastPageInfo"){

        When("기본 기능 확인"){

            val searchParamDto = SearchParamDto(2, 10)
            val totalPages = 10
            val listUrl = "http://localhost:8080/gallery/list?"

            val result = galleryService.getLastPageInfo(searchParamDto, totalPages, listUrl)

            Then("마지막 페이지 출력"){
                result shouldBe "http://localhost:8080/gallery/list?pageNumber=${totalPages}&pageSize=${searchParamDto.pageSize}"
            }
        }
    }

    Given("getListUrl"){

        When("기본 기능 테스트"){

            val result = galleryService.getListUrl("http://localhost", "8080")

            Then("url 출력"){
                result shouldBe "http://localhost:8080/gallery/list?"
            }
        }
    }

    Given("checkListParam"){

        val searchParamDto = SearchParamDto()

        When("파라미터가 정상 범위일 때"){

            searchParamDto.pageNumber=1
            searchParamDto.pageSize=10

            galleryService.checkListParam(searchParamDto)

            Then("값이 변하지 않음"){
                searchParamDto.pageNumber shouldBe 1
                searchParamDto.pageSize shouldBe 10
            }
        }

        When("파라미터가 정상 범위가 아닐 때"){

            searchParamDto.pageNumber=-1
            searchParamDto.pageSize=-10

            galleryService.checkListParam(searchParamDto)

            Then("값이 기본값을 변함"){
                searchParamDto.pageNumber shouldBe 1
                searchParamDto.pageSize shouldBe 10
            }
        }
    }


})