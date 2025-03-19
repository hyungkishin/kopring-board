package com.board.comment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class CommentApplication

fun main(args: Array<String>) {
    runApplication<CommentApplication>(*args)
}