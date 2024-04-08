package com.cjg.scene.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/board")
class BoardController {

    @GetMapping("/list")
    fun list(): String{
        return "abc"
    }
}