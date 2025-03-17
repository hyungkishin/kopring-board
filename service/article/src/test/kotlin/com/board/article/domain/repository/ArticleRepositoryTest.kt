package com.board.article.domain.repository

import com.board.article.domain.Article
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.system.measureTimeMillis

@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Test
    fun findAllTest() {
        var articles: List<Article>
        val elapsedTimeMillis = measureTimeMillis {
            articles = articleRepository.findAllArticles(1L, 14999970L, 30L)
        }

        println("소요시간: ${elapsedTimeMillis / 1000.0} 초")

        println("게시글 수: ${articles.size}")

        for (article in articles) {
            println(article)
        }

    }

    @Test
    fun countAllTest() {

        var count = 0L
        val elapsedTimeMillis = measureTimeMillis {
            count = articleRepository.countAllArticles(1L, 10000L)
        }
        println("쿼리 수행 ${elapsedTimeMillis/1000.0} 초 소요")
        println("게시글 수: $count")
    }

}