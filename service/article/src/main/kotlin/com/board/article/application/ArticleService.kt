package com.board.article.application

import com.board.article.domain.Article
import com.board.article.domain.repository.ArticleRepository
import com.board.article.ui.request.ArticleCreateRequest
import com.board.article.ui.request.ArticleUpdateRequest
import com.board.article.ui.response.ArticleResponse
import kuke.board.common.snowflake.Snowflake
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
) {

    private val snowflake: Snowflake = Snowflake()

    @Transactional
    fun create(request: ArticleCreateRequest): ArticleResponse {
        val nextId = snowflake.nextId()
        val article = articleRepository.save(request.toEntity(nextId))

        return ArticleResponse.fromEntity(article);
    }

    @Transactional
    fun update(id: Long, request: ArticleUpdateRequest): ArticleResponse {
        val article = findArticle(id)
        article.update(request.toEntity())

        return ArticleResponse.fromEntity(article);
    }

    private fun findArticle(id: Long): Article {
        return articleRepository.findById(id)
            .orElseThrow { throw IllegalArgumentException("게시글을 찾을 수 없습니다.") }
    }

    @Transactional(readOnly = true)
    fun read(id: Long): ArticleResponse {
        val article = findArticle(id)

        return ArticleResponse.fromEntity(article)
    }

    @Transactional
    fun delete(id: Long) {
        articleRepository.deleteById(id)
    }


}