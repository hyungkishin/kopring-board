package com.board.comment.application

import com.board.comment.application.util.calculatePageLimit
import com.board.comment.domain.CommentPath
import com.board.comment.domain.CommentV2
import com.board.comment.domain.repository.CommentRepositoryV2
import com.board.comment.ui.request.CommentCreateRequestV2
import com.board.comment.ui.response.CommentPageResponse
import com.board.comment.ui.response.CommentResponse
import kuke.board.common.snowflake.Snowflake
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentServiceV2(
    private val commentRepository: CommentRepositoryV2,
) {

    private val snowflake: Snowflake = Snowflake()

    @Transactional
    fun createComment(request: CommentCreateRequestV2): CommentResponse {
        val parent = findParent(request)

        val parentCommentPath = parent?.commentPath ?: CommentPath.create("")

        val createChildCommentPath = parentCommentPath.createChildCommentPath(
            commentRepository.findDescendantsTopPath(
                request.articleId,
                parentCommentPath.path // 이게 String 타입이라고
            )
        )

        val comment = commentRepository.save(
            CommentV2.create(
                snowflake.nextId(),
                request.content,
                request.articleId,
                request.writerId,
                createChildCommentPath
            )
        )

        return CommentResponse.fromEntityV2(comment)
    }

    private fun findParent(request: CommentCreateRequestV2): CommentV2? {
        return request.parentPath?.let { path ->
            commentRepository.findByPath(path)
                ?.takeIf { !it.deleted }
        }
    }

    fun read(commentId: Long): CommentResponse {
        val comment =
            commentRepository.findById(commentId)
                .orElseThrow { throw IllegalArgumentException("Comment not found") }
        return CommentResponse.fromEntityV2(comment)
    }

    fun delete(commentId: Long) {
        commentRepository.findById(commentId)
            .filter { !it.deleted }
            .ifPresent { comment ->
                if (hasChildren(comment)) {
                    comment.deleted()
                } else {
                    delete(comment)
                }
            }
    }

    private fun hasChildren(comment: CommentV2): Boolean {
        return commentRepository.findDescendantsTopPath(
            comment.articleId,
            comment.commentPath.path
        )?.isNotEmpty() ?: false
    }

    private fun delete(comment: CommentV2) {
        commentRepository.delete(comment)
        if (!comment.isRoot()) {
            val parent = commentRepository.findByPath(comment.commentPath.getParendPath())
            if (parent != null && parent.deleted && !hasChildren(parent)) {
                delete(parent)
            }
        }
    }

    @Transactional(readOnly = true)
    fun readAll(
        articleId: Long,
        page: Long,
        pageSize: Long
    ): CommentPageResponse {
        val pageResponse = commentRepository.findAll(articleId, (page - 1) * pageSize, pageSize)
            .map { CommentResponse.fromEntityV2(it) }

        val count = commentRepository.count(articleId, calculatePageLimit(page, pageSize, 10L))

        return CommentPageResponse.of(pageResponse, count)
    }

    @Transactional(readOnly = true)
    fun readAllInfiniteScroll(
        articleId: Long,
        lastPath: String?,
        pageSize: Long
    ): List<CommentResponse> {

        val comments: List<CommentV2> =
            lastPath?.let { commentRepository.findAllInfiniteScroll(articleId, lastPath, pageSize) }
                ?: commentRepository.findAllInfiniteScroll(articleId, pageSize)

        return comments.map { CommentResponse.fromEntityV2(it) }
    }

}