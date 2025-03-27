package com.board.comment.api

import com.board.comment.ui.request.CommentCreateRequestV2
import com.board.comment.ui.response.CommentPageResponse
import com.board.comment.ui.response.CommentResponse
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.client.RestClient

class CommentApiV2Test {

    private val restClient = RestClient.create("http://localhost:9001")

    @Test
    fun create() {
        val response1 = create(
            CommentCreateRequestV2(1L, "my comment1", null, 1L)
        )
        val response2 = create(
            CommentCreateRequestV2(1L, "my comment2", response1.path, 1L)
        )
        val response3 = create(
            CommentCreateRequestV2(1L, "my comment3", response2.path, 1L)
        )

        println("${response1.path}")
        println("\t${response2.path}")
        println("\t\t${response3.path}")
    }

    private fun create(request: CommentCreateRequestV2): CommentResponse {
        return restClient.post()
            .uri("/v2/comments")
            .body(request)
            .retrieve()
            .body(CommentResponse::class.java) ?: throw IllegalArgumentException()
    }

    @Test
    fun read() {
        val response = restClient.get()
            .uri("/v2/comments/${162181733228961792}")
            .retrieve()
            .body(CommentResponse::class.java)

        println("응답: ${response}")
    }

    @Test
    fun delete() {
        restClient.delete()
            .uri("/v2/comments/${162181733228961792}")
            .retrieve()
    }

    @Test
    fun readAll() {
        val response = restClient.get()
            .uri("/v2/comments?articleId=${1}&pageSize=${10}&page=${1}")
            .retrieve()
            .body(CommentPageResponse::class.java) ?: throw IllegalArgumentException("read All 조회 실패")

        for (comment in response.comments) {
            println("id : ${comment.commentId}")
        }
    }

    @Test
    fun readAllInfiniteScroll() {
        val responses = restClient.get()
            .uri("/v2/comments/infinity-scroll?articleId=${1}&pageSize=${5}")
            .retrieve()
            .body(object : ParameterizedTypeReference<List<CommentResponse>>() {})
            ?: throw IllegalArgumentException("infinite scroll 조회 실패")

        for (response in responses) {
            println("${response.commentId} / ${response.path}")
        }

    }
}