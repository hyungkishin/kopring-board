package com.board.comment.domain.repository

import com.board.comment.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentRepository : JpaRepository<Comment, Long> {

    @Query(
        value =
            """
            select count(*) from (
                select comment_id
                from comments
                where article_id = :articleId and parent_comment_id = :parentCommentId
                limit :limit
            ) t
        """,
        nativeQuery = true
    )
    fun countBy(
        @Param("articleId") articleId: Long,
        @Param("parentCommentId") parentCommentId: Long,
        @Param("limit") limit: Long,
    ): Long

    @Query(
        value =
            """
            select 
                comments.comment_id, 
                comments.content,
                comments.parent_comment_id,
                comments.article_id,
                comments.writer_id,
                comments.deleted,
                comments.created_at,
                comments.updated_at
            from (
                select comment_id
                from comments where article_id = :articleId
                order by parent_comment_id asc, comment_id asc
                limit :limit offset :offset
            ) t left join comments on t.comment_id = comments.comment_id
        """,
        nativeQuery = true
    )
    fun findAll(
        @Param("articleId") articleId: Long,
        @Param("offset") offset: Long,
        @Param("limit") limit: Long,
    ): List<Comment>

    @Query(
        value = """
            select count(*)
            from (
                select comment_id from comments
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
            select comment_id, content, parent_comment_id, article_id, writer_id, deleted, created_at, updated_at
            from comments
            where article_id = :articleId 
            order by parent_comment_id asc, comment_id asc
            limit :limit
        """,
        nativeQuery = true
    )
    fun findAllInfiniteScroll(
        @Param("articleId") articleId: Long,
        @Param("limit") limit: Long,
    ): List<Comment>

    @Query(
        value = """
            select comment_id, content, parent_comment_id, article_id, writer_id, deleted, created_at, updated_at
            from comments
            where article_id = :articleId and (
                parent_comment_id > :lastParentCommentId or 
                parent_comment_id = :lastParentCommentId and 
                comment_id > :lastCommentId
            )
            order by parent_comment_id asc, comment_id asc
            limit :limit
        """,
        nativeQuery = true
    )
    fun findAllInfiniteScroll(
        @Param("articleId") articleId: Long,
        @Param("limit") limit: Long,
        @Param("lastParentCommentId") lastParentCommentId: Long,
        @Param("lastCommentId") lastCommentId: Long,
    ): List<Comment>

}