package com.board.comment.ui.response

class CommentPageResponse(
    val comments: List<CommentResponse>,
    val commentCount: Long,
) {

    companion object {

        @JvmStatic
        fun of(comments: List<CommentResponse>, commentCount: Long): CommentPageResponse {
            return CommentPageResponse(comments, commentCount)
        }

    }

}