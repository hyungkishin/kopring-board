package com.board.comment.domain

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThatThrownBy
import org.junit.jupiter.api.Test

class CommentPathTest {


    @Test
    fun createChildCommentTest() {
        // 00000 <- 생성 ( 상위 댓글 이 없는 최초의 댓글 상태 )
        createChildComment(CommentPath.create(""), null, "00000")

        // 000000
        // 000000 <- 생성
        createChildComment(CommentPath.create("00000"), null, "0000000000")

        // 00000
        // 00001 <- 생성
        createChildComment(CommentPath.create(""), "00000", "00001")

        // 00000z
        //      abcdz
        //          zzzzz
        //      abce0 <- 생성
        createChildComment(CommentPath.create("0000z"), "0000zabcdzzzzzzzzzz", "0000zabce0")
    }

    fun createChildComment(commentPath: CommentPath, descendantsTopPath: String?, expectedChildPath: String) {
        val childCommentPath = commentPath.createChildCommentPath(descendantsTopPath)
        assertThat(childCommentPath.path).isEqualTo(expectedChildPath)
    }

    @Test
    fun createChildCommentPathIfMaxDepthTest() {
        assertThatThrownBy {
            CommentPath.create("zzzzz".repeat(5))
                .createChildCommentPath(null)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun createChildCommentPathIfChunkOverflowTest() {
        // given
        val commentPath = CommentPath.create("")

        // when & then
        assertThatThrownBy { commentPath.createChildCommentPath("zzzzz") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

}