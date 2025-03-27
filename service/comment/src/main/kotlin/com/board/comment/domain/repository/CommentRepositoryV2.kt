package com.board.comment.domain.repository

import com.board.comment.domain.Comment
import com.board.comment.domain.CommentV2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentRepositoryV2 : JpaRepository<CommentV2, Long> {

    @Query("SELECT c FROM CommentV2 c WHERE c.commentPath.path = :path")
    fun findByPath(@Param("path") path: String): CommentV2?

    @Query(
        value = """
            SELECT path
            FROM comments_v2
            WHERE article_id = :articleId AND path > :pathPrefix
            AND comments_v2.path LIKE CONCAT(:pathPrefix, '%')
            ORDER BY path DESC LIMIT 1
        """,
        nativeQuery = true
    )
    fun findDescendantsTopPath(
        @Param("articleId") articleId: Long,
        @Param("pathPrefix") pathPrefix: String,
    ): String?

    @Query(
        value = """
            select comments.*
            from (
                select comments_v2.comment_id
                from comments_v2
                where comments_v2.article_id = :articleId
                order by comments_v2.path asc
                limit :limit offset :offset
            ) t left join comments_v2 comments on t.comment_id = comments.comment_id
        """,
        nativeQuery = true
    )
    fun findAll(
        @Param("articleId") articleId: Long,
        @Param("offset") offset: Long,
        @Param("limit") limit: Long,
    ): List<CommentV2>

    @Query(
        value = """
            select count(*) from (
                select comments_v2.comment_id 
                from comments_v2
                where article_id = :articleId 
                limit :limit
            ) t 
        """,
        nativeQuery = true
    )
    fun count(
        @Param("articleId") articleId: Long,
        @Param("limit") limit: Long,
    ): Long

    @Query(
        value = """
            select comments.*
            from comments_v2 comments
            where article_id = :articleId
            order by path asc
            limit :limit
        """,
        nativeQuery = true
    )
    fun findAllInfiniteScroll(
        @Param("articleId") articleId: Long,
        @Param("limit") limit: Long,
    ): List<CommentV2>

    @Query(
        value = """
            select comments.*
            from comments_v2 comments
            where article_id = :articleId and path > :lastPath
            order by path asc
            limit :limit
        """,
        nativeQuery = true
    )
    fun findAllInfiniteScroll(
        @Param("articleId") articleId: Long,
        @Param("lastPath") lastPath: String,
        @Param("limit") limit: Long,
    ): List<CommentV2>

}