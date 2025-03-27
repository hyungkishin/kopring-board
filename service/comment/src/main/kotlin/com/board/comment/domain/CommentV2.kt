package com.board.comment.domain

import jakarta.persistence.*
import kuke.board.common.entity.base.BaseTimeEntity

@Entity
@Table(name = "comments_v2")
class CommentV2 constructor(

    @Id
    @Column(name = "comment_id")
    val id: Long,

    @Column(name = "content")
    var content: String,

    @Column(name = "article_id")
    val articleId: Long, // shard key

    @Column(name = "writer_id")
    val writerId: Long,

    @Embedded
    val commentPath: CommentPath,

    @Column(name = "deleted")
    var deleted: Boolean = false,

    ): BaseTimeEntity() {

    fun isRoot(): Boolean {
        return commentPath.isRoot()
    }

    fun deleted() {
        this.deleted = true
    }

    override fun toString(): String {
        return "Comment(id=$id, content='$content', articleId=$articleId, writerId=$writerId, deleted=$deleted)"
    }

    companion object {

        @JvmStatic
        fun create(
            id: Long,
            content: String,
            articleId: Long,
            writerId: Long,
            commentPath: CommentPath,
        ): CommentV2 {
            return CommentV2(
                id = id,
                content = content,
                articleId = articleId,
                writerId = writerId,
                commentPath = commentPath,
                deleted = false
            )
        }
    }

}