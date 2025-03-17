package com.board.article.ui.response

import com.board.article.domain.Article
import java.time.LocalDateTime

data class ArticleResponse(
    val id: Long,
    val title: String,
    val content: String,
    val boardId: Long,
    val writeId: Long,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
) {

    companion object {
        fun fromEntity(article: Article): ArticleResponse {
            return ArticleResponse(
                id = article.id!!,
                title = article.title,
                content = article.content,
                boardId = article.boardId!!,
                writeId = article.writerId!!,
                createdAt = article.createdAt,
                updatedAt = article.updatedAt,
            )
        }
    }

}
