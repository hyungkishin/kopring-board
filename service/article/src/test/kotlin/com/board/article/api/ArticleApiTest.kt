package com.board.article.api

import com.board.article.ui.request.ArticleCreateRequest
import com.board.article.ui.request.ArticleUpdateRequest
import com.board.article.ui.response.ArticlePageResponse
import com.board.article.ui.response.ArticleResponse
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.client.RestClient

class ArticleApiTest {

    private val restClient = RestClient.create("http://localhost:9000")

    @Test
    fun `게시글 생성`() {
        val response = create(
            ArticleCreateRequest(
                title = "제목",
                content = "내용",
                boardId = 1,
                writeId = 1
            )
        )

        assertThat(response.title).isEqualTo("제목")
        assertThat(response.content).isEqualTo("내용")
    }

    private fun create(request: ArticleCreateRequest): ArticleResponse {
        return restClient.post()
            .uri("/articles")
            .body(request)
            .retrieve()
            .body(ArticleResponse::class.java)!!
    }

    @Test
    fun `게시글 수정`() {
        val createResponse = create(
            ArticleCreateRequest(
                title = "제목",
                content = "내용",
                boardId = 1,
                writeId = 1
            )
        )

        val updateResponse = update(
            createResponse.id,
            ArticleUpdateRequest(
                title = "제목 수정",
                content = "내용 수정"
            )
        )

        assertThat(updateResponse.title).isEqualTo("제목 수정")
        assertThat(updateResponse.content).isEqualTo("내용 수정")
    }

    private fun update(id: Long, request: ArticleUpdateRequest): ArticleResponse {
        return restClient.put()
            .uri("/articles/${id}")
            .body(request)
            .retrieve()
            .body(ArticleResponse::class.java)!!
    }

    @Test
    fun `게시글 페이징 조회`() {
        val articlePageResponse = restClient.get()
            .uri("/articles?boardId=1&page=1&pageSize=30")
            .retrieve()
            .body(ArticlePageResponse::class.java)!!

        assertThat(articlePageResponse).isNotNull
    }

    @Test
    fun `게시글 무한 스크롤 조회`() {
        val firstPages = restClient.get()
            .uri("/articles/infinity-scroll?boardId=1&pageSize=5")
            .retrieve()
            .body(object : ParameterizedTypeReference<List<ArticleResponse>>() {})!!

        println("firstPages")
        for (article in firstPages) {
            println("article id : ${article.id}")
        }

        val lastArticleId = firstPages.last().id
        val secondPages = restClient.get()
            .uri("/articles/infinity-scroll?boardId=1&pageSize=5&lastArticleId=${lastArticleId}")
            .retrieve()
            .body(object : ParameterizedTypeReference<List<ArticleResponse>>() {})!!

        println("secondPages")
        for (article in secondPages) {
            println("article id : ${article.id}")
        }
    }

}