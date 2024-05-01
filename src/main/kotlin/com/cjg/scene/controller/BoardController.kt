package com.cjg.scene.controller

import com.cjg.scene.dto.BoardDto
import com.cjg.scene.service.BoardService
import org.springframework.web.bind.annotation.*

@RestController
class BoardController(
    private val boardService: BoardService,
) {

    @GetMapping("/board/list")
    fun list(): String{
        return "abc"
    }

    @PostMapping("/board")
    fun create(boardDto: BoardDto):String?{
        return boardService.create(boardDto)
    }
}