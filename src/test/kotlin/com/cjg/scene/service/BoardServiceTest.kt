package com.cjg.scene.service

import com.cjg.scene.data.Board
import com.cjg.scene.dto.BoardDto
import com.cjg.scene.repository.BoardRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BoardServiceTest : BehaviorSpec({

    val boardRepository = mockk<BoardRepository>()

    Given("/board/list : 게시판 리스트를 요청시"){

        val boardService = withContext(Dispatchers.IO){
            BoardService(boardRepository)
        }

        When("올바른 데이터 입력 시"){
            val boardDto = BoardDto()

            val newBoardDto = boardService.checkListParam(boardDto)

            Then("원래 값 출력"){
                newBoardDto.pageNumber shouldBe boardDto.pageNumber
                newBoardDto.pageSize shouldBe boardDto.pageSize
            }
        }

        When("음수 값 입력 시"){
            val boardDto = BoardDto()
            boardDto.pageNumber = -1
            boardDto.pageSize = -100

            val newBoardDto = boardService.checkListParam(boardDto)

            Then("기본 값 출력") {
                newBoardDto.pageNumber shouldBe BoardDto().pageNumber
                newBoardDto.pageSize shouldBe BoardDto().pageSize
            }

        }
    }

    Given("등록 요청을 할 때"){

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