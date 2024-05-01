package com.cjg.scene.service

import com.cjg.scene.data.Board
import com.cjg.scene.dto.BoardDto
import com.cjg.scene.repository.BoardRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BoardServiceTest : BehaviorSpec({

    val boardRepository = mockk<BoardRepository>()

    Given("create"){

        val boardDto = BoardDto()
        boardDto.title = "테스트제목"
        boardDto.userId = "coolcjg"
        boardDto.content = "내용"
        boardDto.category = "서울"

        val boardService = withContext(Dispatchers.IO){
            BoardService(boardRepository)
        }

        When("올바른 데이터를 입력하면"){

            val board = Board()
            board.id = "aabb"
            board.title = boardDto.title
            board.userId = boardDto.userId
            board.content = boardDto.content
            board.category = boardDto.category

            every{boardRepository.save(any())} returns board

            Then("저장 완료") {
                val result = boardService.create(boardDto)

                result shouldBe "created"
            }
        }

    }
})