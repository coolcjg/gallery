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

})