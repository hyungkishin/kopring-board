package com.board.comment.ui.response

import com.board.comment.domain.Comment

data class CommentResponse(
    val commentId: Long,
    val content: String,
    val parentCommentId: Long,
    val articleId: Long,
    val writerId: Long,
    val deleted: Boolean,
    val createdAt: String,
) {
    companion object {

        @JvmStatic
        fun fromEntity(comment: Comment): CommentResponse {
            return CommentResponse(
                commentId = comment.id!!,
                content = comment.content,
                parentCommentId = comment.parentCommentId!!,
                articleId = comment.articleId,
                 writerId = comment.writerId,
                deleted = comment.deleted,
                createdAt = comment.createdAt.toString(),
            )
        }
    }

}
