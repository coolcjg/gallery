package com.cjg.scene.service

import com.cjg.scene.data.Board
import com.cjg.scene.dto.BoardDto
import com.cjg.scene.repository.BoardRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
@Transactional(readOnly=true)
class BoardService(
    private val boardRepository:BoardRepository,
) {
    private val log = LoggerFactory.getLogger(BoardService::class.java)

    fun list(boardDto : BoardDto):Map<String, Any>{
        val result = HashMap<String, Any>()

        //파라미터 검증
        val checkedBoardDto = checkListParam(boardDto)

        //페이징 처리
        val pageable = PageRequest.of(checkedBoardDto.pageNumber-1, checkedBoardDto.pageSize)
        val page = boardRepository.findAll(pageable)

        //날짜 형식 변환
        val boardDtoList = mutableListOf<BoardDto>()

        for(element:Board in page.content){

            val tempBoard = BoardDto()
            tempBoard.title = element.title
            tempBoard.content = element.content
            tempBoard.category = element.category

            if(element.createDate != null){
                tempBoard.createDate =  element.createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }
            boardDtoList.add(tempBoard)
        }

        result.put("message", "success")
        result.put("data", boardDtoList)

        return result
    }



    fun checkListParam(boardDto:BoardDto):BoardDto{

        //값 범위 확인
        if(boardDto.pageNumber <=0 || boardDto.pageSize <= 0){
            return BoardDto()
        }

        return boardDto
    }
    @Transactional
    fun create(boardDto: BoardDto):String?{
        val message:String

        val board = Board()
        board.title = boardDto.title
        board.content = boardDto.content
        board.userId = boardDto.userId

        val savedBoard = boardRepository.save(board)

        if(savedBoard.id != null){
            message = "created"
        }else{
            message = "creation failed"
        }

        return message
    }
}