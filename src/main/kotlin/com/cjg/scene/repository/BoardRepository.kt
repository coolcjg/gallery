package com.cjg.scene.repository

import com.cjg.scene.data.Board
import org.springframework.data.mongodb.repository.MongoRepository

interface BoardRepository: MongoRepository<Board, String> {
}