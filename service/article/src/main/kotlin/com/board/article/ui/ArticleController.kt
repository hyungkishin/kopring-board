package com.board.article.ui

import com.board.article.application.ArticleService
import com.board.article.ui.request.ArticleCreateRequest
import com.board.article.ui.request.ArticleUpdateRequest
import com.board.article.ui.response.ArticlePageResponse
import com.board.article.ui.response.ArticleResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/articles")
class ArticleController(
    private val articleService: ArticleService
) {

    @PostMapping
    fun create(@RequestBody request: ArticleCreateRequest): ArticleResponse {
        return articleService.create(request)
    }

    @GetMapping
    fun readAll(
        @RequestParam("boardId") boardId: Long,
        @RequestParam("page") page: Long,
        @RequestParam("pageSize") pageSize: Long,
    ): ArticlePageResponse {
        return articleService.readAll(boardId, page, pageSize)
    }

    @GetMapping( "/infinity-scroll")
    fun readAllInfinityScroll(
        @RequestParam("boardId") boardId: Long,
        @RequestParam("pageSize") pageSize: Long,
        @RequestParam("lastArticleId") lastArticleId: Long?,
    ): List<ArticleResponse> {
        return articleService.readAllInfinityScroll(boardId, pageSize, lastArticleId)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Long): ArticleResponse {
        return articleService.read(id)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: ArticleUpdateRequest): ArticleResponse {
        return articleService.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        articleService.delete(id)
    }

}