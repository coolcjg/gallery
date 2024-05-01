package com.cjg.scene.service

import com.cjg.scene.data.Board
import com.cjg.scene.dto.BoardDto
import com.cjg.scene.repository.BoardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly=true)
class BoardService(

    private val boardRepository:BoardRepository,

) {

    @Transactional
    fun create(boardDto: BoardDto):String?{

        val board = Board()
        board.title = boardDto.title
        board.content = boardDto.content
        board.userId = boardDto.userId

        val saveBoard = boardRepository.save(board)
        return saveBoard.id
    }

}