package com.board.comment.ui

import com.board.comment.application.CommentService
import com.board.comment.ui.request.CommentCreateRequest
import com.board.comment.ui.response.CommentResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/comments")
@RestController
class CommentController(
    private val commentService: CommentService,
) {

    @GetMapping("/{commentId}")
    fun readComment(@PathVariable("commentId") commentId: Long): CommentResponse {
        return commentService.read(commentId)
    }

    @PostMapping
    fun createComment(@RequestBody request: CommentCreateRequest): CommentResponse {
        return commentService.createComment(request)
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable("commentId") commentId: Long) {
        commentService.delete(commentId)
    }

}