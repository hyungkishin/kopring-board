package com.board.comment.ui

import com.board.comment.application.CommentServiceV2
import com.board.comment.ui.request.CommentCreateRequestV2
import com.board.comment.ui.response.CommentPageResponse
import com.board.comment.ui.response.CommentResponse
import org.springframework.web.bind.annotation.*

@RequestMapping("/v2/comments")
@RestController
class CommentController2(
    private val commentService: CommentServiceV2,
) {

    @GetMapping("/{commentId}")
    fun readComment(@PathVariable("commentId") commentId: Long): CommentResponse {
        return commentService.read(commentId)
    }

    @PostMapping
    fun createComment(@RequestBody request: CommentCreateRequestV2): CommentResponse {
        return commentService.createComment(request)
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable("commentId") commentId: Long) {
        commentService.delete(commentId)
    }

    @GetMapping
    fun readAll(
        @RequestParam("articleId") articleId: Long,
        @RequestParam("page") page: Long,
        @RequestParam("pageSize") pageSize: Long,
    ): CommentPageResponse {
        return commentService.readAll(
            articleId,
            page,
            pageSize
        )
    }

    @GetMapping("/infinity-scroll")
    fun readAll(
        @RequestParam("articleId") articleId: Long,
        @RequestParam(value = "lastPath", required = false) lastPath: String?,
        @RequestParam("pageSize") pageSize: Long,
    ): List<CommentResponse> {
        return commentService.readAllInfiniteScroll(
            articleId,
            lastPath,
            pageSize
        )
    }

}