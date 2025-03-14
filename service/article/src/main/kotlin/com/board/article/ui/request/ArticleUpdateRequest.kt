package com.board.article.ui.request

import com.board.article.domain.Article

data class ArticleUpdateRequest(
    val title: String,
    val content: String,
) {

    fun toEntity(): Article {
        return Article(
            title = title,
            content = content
        )
    }
}