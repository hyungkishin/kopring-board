package com.board.comment.ui.request

data class CommentCreateRequestV2(
    val articleId: Long,
    val content: String,
    val parentPath: String?,
    val writerId: Long,
)