package com.board.article.domain.repository

import com.board.article.domain.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ArticleRepository : JpaRepository<Article, Long> {

    @Query(
        value = """
           select articles.article_id,
                  articles.title,
                  articles.content,
                  articles.board_id,
                  articles.writer_id,
                  articles.created_at,
                  articles.updated_at
           from (
                select article_id from articles
                where board_id = :boardId
                order by article_id desc
                limit :limit 
                offset :offset
           ) t left join articles on t.article_id = articles.article_id
        """,
        nativeQuery = true
    )
    fun findAllArticles(
        @Param("boardId") boardId: Long,
        @Param("offset") offset: Long,
        @Param("limit") limit: Long,
    ): List<Article>

    @Query(
        value = """
        select count(*)
        from (
            select article_id from articles
            where board_id = :boardId limit :limit
        ) t
        """,
        nativeQuery = true
    )
    fun countAllArticles(
        @Param("boardId") boardId: Long,
        @Param("limit") limit: Long
    ): Long

}