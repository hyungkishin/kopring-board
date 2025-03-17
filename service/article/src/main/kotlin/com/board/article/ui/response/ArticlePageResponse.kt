package com.board.article.ui.response

class ArticlePageResponse(
    val articles: List<ArticleResponse>,
    val articleCount: Long,
) {

    companion object {
        @JvmStatic
        fun of(articles: List<ArticleResponse>, articleCount: Long): ArticlePageResponse {
            return ArticlePageResponse(articles, articleCount)
        }
    }

}