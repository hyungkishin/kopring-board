package com.board.comment.api

import com.board.comment.ui.request.CommentCreateRequest
import com.board.comment.ui.response.CommentResponse
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClient

class CommentApiTest {

    private val restClient: RestClient = RestClient.create("http://localhost:9001")

    @Test
    fun `댓글 생성`() {
        val response1 = createComment(CommentCreateRequest(1L, "댓글 내용 1", null, 1L))
        val response2 = createComment(CommentCreateRequest(1L, "댓글 내용 2", response1.commentId, 1L))
        val response3 = createComment(CommentCreateRequest(1L, "댓글 내용 2", response1.commentId, 1L))


        println("commentId1: ${response1.commentId} , commentId2: ${response2.commentId} , commentId3: ${response3.commentId}")

    }

    private fun createComment(request: CommentCreateRequest): CommentResponse {
        return restClient.post()
            .uri("/v1/comments")
            .body(request)
            .retrieve()
            .body(CommentResponse::class.java) ?: throw IllegalArgumentException("댓글 생성 실패")
    }

//    @Test
//    fun `댓글 조회`() {
//        val response = restClient.get()
//            .uri("/v1/comments/{commentId}", 160329374324809728)
//            .retrieve()
//            .body(CommentResponse::class.java)
//
//        assertThat(response?.commentId).isNotNull()
//    }

//    @Test
//    fun `댓글 삭제`() {
//        restClient.delete()
//            .uri("/v1/comments/{commentId}", 160329374324809728)
//            .retrieve()
//            .body(Void::class.java)
//    }

}