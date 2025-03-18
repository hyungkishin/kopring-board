package com.board.comment.application

import com.board.comment.domain.Comment
import com.board.comment.domain.repository.CommentRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class CommentServiceTest {

    @InjectMocks
    private lateinit var commentService: CommentService

    @Mock
    private lateinit var commentRepository: CommentRepository

    @Test
    fun `삭제할 댓글의 자식이 존재하는 경우, 삭제 표시만 한다`() {
        // given
        val articleId = 1L
        val commentId = 2L
        val comment = createComment(articleId, commentId)

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment))

        given(commentRepository.countBy(articleId, commentId, HAS_CHILDREN)).willReturn(HAS_CHILDREN)

        // when
        commentService.delete(commentId)

        // then
        verify(comment).deleted()
    }

    @Test
    fun `하위 댓글이 삭제되고, 삭제하지 않은 부모면, 하위댓글만 삭제한다`() {
        // given
        val articleId = 1L
        val commentId = 2L
        val parentCommentId = 1L
        val comment = createComment(articleId, commentId, parentCommentId)

        given(comment.isRoot()).willReturn(false)

        val parentComment = mock(Comment::class.java)
         
        given(parentComment.deleted).willReturn(false)

        given(commentRepository.findById(commentId)) .willReturn(Optional.of(comment))

        given(commentRepository.countBy(articleId, commentId, HAS_CHILDREN)).willReturn(1L)

        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(parentComment))

        // when
        commentService.delete(commentId)

        // then
        verify(commentRepository).delete(comment)
        verify(commentRepository, never()).delete(parentComment)
    }

    @Test
    fun `하위 댓글이 삭제되고, 삭제된 부모면, 재귀적으로 삭제한다`() {
        // given
        val articleId = 1L
        val commentId = 2L
        val parentCommentId = 1L
        val comment = createComment(articleId, commentId, parentCommentId)

        given(comment.isRoot()).willReturn(false)

        val parentComment = createComment(articleId, parentCommentId)

        given(parentComment.isRoot()).willReturn(true)
        given(parentComment.deleted).willReturn(true)

        given(commentRepository.findById(commentId)) .willReturn(Optional.of(comment))

        given(commentRepository.countBy(articleId, commentId, HAS_CHILDREN)).willReturn(1L)

        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(parentComment))

        given(commentRepository.countBy(articleId, parentCommentId, HAS_CHILDREN)).willReturn(1L)

        // when
        commentService.delete(commentId)

        // then
        verify(commentRepository).delete(comment)
        verify(commentRepository).delete(parentComment)
    }

    private fun createComment(articleId: Long, commentId: Long): Comment {
        val comment = mock(Comment::class.java)
        given(comment.articleId).willReturn(articleId)
        given(comment.id).willReturn(commentId)
        return comment
    }

    private fun createComment(articleId: Long, commentId: Long, parentCommentId: Long): Comment {
        val comment = createComment(articleId, commentId)
        given(comment.parentCommentId).willReturn(parentCommentId)
        return comment
    }


    companion object {
        private const val HAS_CHILDREN = 2L
    }
    
}