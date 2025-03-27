package com.board.comment.application

import com.board.comment.application.util.calculatePageLimit
import com.board.comment.domain.Comment
import com.board.comment.domain.repository.CommentRepository
import com.board.comment.ui.request.CommentCreateRequest
import com.board.comment.ui.response.CommentPageResponse
import com.board.comment.ui.response.CommentResponse
import kuke.board.common.snowflake.Snowflake
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
) {

    private val snowflake: Snowflake = Snowflake()

    @Transactional
    fun createComment(request: CommentCreateRequest): CommentResponse {
        val commentId = findParentComment(request.parentCommentId)?.id
            ?: snowflake.nextId()

        val comment = commentRepository.save(
            Comment.create(
                id = commentId,
                articleId = request.articleId,
                content = request.content,
                parentCommentId = commentId,
                writerId = request.writerId,
            )
        )

        return CommentResponse.fromEntity(comment)
    }

    private fun findParentComment(parentCommentId: Long?): Comment? {
        return parentCommentId?.let {
            commentRepository.findById(it)
                .filter { comment -> !comment.deleted && comment.isRoot() }
                .orElseThrow { IllegalArgumentException("부모 댓글이 존재하지 않습니다.") }
        }
    }

    @Transactional(readOnly = true)
    fun read(commentId: Long): CommentResponse {
        return CommentResponse.fromEntity(
            commentRepository.findById(commentId).orElseThrow()
        )
    }

    @Transactional
    fun delete(id: Long) {
        commentRepository.findById(id)
            .filter { !it.deleted }
            .ifPresent { comment ->
                if (hasChildren(comment)) {
                    comment.deleted()
                } else {
                    deleteCommentAndCheckParent(comment)
                }
            }
    }

    private fun hasChildren(comment: Comment): Boolean {
        return commentRepository.countBy(
            comment.articleId,
            comment.id,
            HAS_CHILD_COMMENT_COUNT
        ) == HAS_CHILD_COMMENT_COUNT
    }

    private fun deleteCommentAndCheckParent(comment: Comment) {
        commentRepository.delete(comment)
        if (!comment.isRoot()) {
            comment.parentCommentId?.let {
                commentRepository.findById(it)
                    .filter { it.deleted }
                    .filter { !hasChildren(it) }
                    .ifPresent { deleteCommentAndCheckParent(it) }
            }
        }
    }

    @Transactional(readOnly = true)
    fun readAll(articleId: Long, page: Long, pageSize: Long): CommentPageResponse {
        val comments = commentRepository.findAll(articleId, (page - 1) * pageSize, pageSize)
            .map { CommentResponse.fromEntity(it) }

        val offset = calculatePageLimit(page, pageSize, MOVABLE_PAGE_COUNT)

        val commentCount = commentRepository.count(articleId, offset)

        return CommentPageResponse.of(comments, commentCount)
    }

    @Transactional(readOnly = true)
    fun readAll(articleId: Long, limit: Long, lastParentCommentId: Long?, lastCommentId: Long?): List<CommentResponse> {
        val comments = if (lastCommentId == null || lastParentCommentId == null) {
            commentRepository.findAllInfiniteScroll(articleId, limit)
        } else {
            commentRepository.findAllInfiniteScroll(articleId, limit, lastParentCommentId, lastCommentId)
        }

        return comments.map { CommentResponse.fromEntity(it) }
    }

    companion object {
        const val HAS_CHILD_COMMENT_COUNT = 2L
        const val MOVABLE_PAGE_COUNT = 10L
    }

}