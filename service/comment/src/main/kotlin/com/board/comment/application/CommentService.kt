package com.board.comment.application

import com.board.comment.domain.Comment
import com.board.comment.domain.repository.CommentRepository
import com.board.comment.ui.request.CommentCreateRequest
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

    companion object {
        const val HAS_CHILD_COMMENT_COUNT = 2L
    }

}