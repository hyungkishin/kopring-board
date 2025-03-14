package com.board.article.ui.request

import com.board.article.domain.Article

data class ArticleCreateRequest(
    val title: String,
    val content: String,
    val boardId: Long,
    val writeId: Long,
) {

    fun toEntity(nextId: Long): Article {
        return Article(
            id = nextId,
            title = title,
            content = content,
            boardId = boardId,
            writeId = writeId,
        )
    }

}
