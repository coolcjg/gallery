package com.cjg.gallery.service

import com.cjg.gallery.dto.SearchParamDto
import com.cjg.gallery.repository.GalleryRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BoardServiceTest : BehaviorSpec({

    val galleryRepository = mockk<GalleryRepository>()

    Given("/board/list : 게시판 리스트를 요청시"){

        val boardService = withContext(Dispatchers.IO){
            GalleryService(galleryRepository)
        }

        When("올바른 데이터 입력 시"){
            val searchParamDto = SearchParamDto(1, 10)

            boardService.checkListParam(searchParamDto)

            Then("원래 값 출력"){
                searchParamDto.pageNumber shouldBe searchParamDto.pageNumber
                searchParamDto.pageSize shouldBe searchParamDto.pageSize
            }
        }

        When("음수 값 입력 시"){
            val searchParamDto = SearchParamDto(-1, -10)

            boardService.checkListParam(searchParamDto)

            Then("기본 값 출력") {
                searchParamDto.pageNumber shouldBe 1
                searchParamDto.pageSize shouldBe 10
            }

        }
    }

    Given("getPrevPageInfo"){

        val boardService = withContext(Dispatchers.IO){
            GalleryService(galleryRepository)
        }

        When("현재 페이지가 2 이상일 때"){
            val searchParamDto =  SearchParamDto()
            searchParamDto.pageNumber = 2

            val result = boardService.getPrevPageInfo(searchParamDto, "http://localhost:8080/gallery/list?")
            println(result)

            Then("이전 페이지가 출력"){
                result shouldBe "http://localhost:8080/gallery/list?pageNumber=${searchParamDto.pageNumber-1}&pageSize=${searchParamDto.pageSize}"
            }
        }

        When("현재 페이지가 1일 때"){
            val searchParamDto =  SearchParamDto()

            val result = boardService.getPrevPageInfo(searchParamDto, "http://localhost:8080/gallery/list?")
            println(result)

            Then("출력 없음"){
                result shouldBe ""
            }
        }

    }

    Given("getNextPageInfo"){

        val boardService = withContext(Dispatchers.IO){
            GalleryService(galleryRepository)
        }

        When("현재 페이지가 마지막페이지가 아닐 때"){

            val searchParamDto = SearchParamDto(1, 10)
            val totalPages = 2
            val listUrl = "http://localhost:8080/gallery/list?"

            val result = boardService.getNextPageInfo(searchParamDto, totalPages, listUrl)

            Then("마지막 페이지 출력"){
                result shouldBe "http://localhost:8080/gallery/list?pageNumber=${totalPages}&pageSize=${searchParamDto.pageSize}"
            }
        }

        When("현재 페이지가 마지막 페이지일 때"){

            val searchParamDto = SearchParamDto(1, 10)
            val totalPages = 1
            val listUrl = "http://localhost:8080/gallery/list?"

            val result = boardService.getNextPageInfo(searchParamDto, totalPages, listUrl)

            Then("빈 값 출력"){
                result shouldBe ""
            }
        }
    }

    Given("getLastPageInfo"){

        val boardService = withContext(Dispatchers.IO){
            GalleryService(galleryRepository)
        }

        When("기본 기능 확인"){

            val searchParamDto = SearchParamDto(2, 10)
            val totalPages = 10
            val listUrl = "http://localhost:8080/gallery/list?"

            val result = boardService.getLastPageInfo(searchParamDto, totalPages, listUrl)

            Then("마지막 페이지 출력"){
                result shouldBe "http://localhost:8080/gallery/list?pageNumber=${totalPages}&pageSize=${searchParamDto.pageSize}"
            }
        }
    }

    Given("getListUrl"){

        val boardService = withContext(Dispatchers.IO){
            GalleryService(galleryRepository)
        }

        When("기본 기능 테스트"){

            val result = boardService.getListUrl("http://localhost", "8080")

            Then("url 출력"){
                result shouldBe "http://localhost:8080/gallery/list?"
            }
        }
    }


})