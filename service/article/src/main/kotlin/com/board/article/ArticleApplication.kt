package com.board.article

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class ArticleApplication

fun main(args: Array<String>) {
    runApplication<ArticleApplication>(*args)
}