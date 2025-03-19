package com.board.comment.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kuke.board.common.entity.base.BaseTimeEntity

@Entity
@Table(name = "comments")
class Comment(

    @Id
    @Column(name = "comment_id")
    val id: Long,

    @Column(name = "content")
    var content: String,

    @Column(name = "parent_comment_id", nullable = true)
    var parentCommentId: Long?,

    @Column(name = "article_id")
    val articleId: Long, // shard key

    @Column(name = "writer_id")
    val writerId: Long,

    @Column(name = "deleted")
    var deleted: Boolean = false

) : BaseTimeEntity() {

    fun isRoot(): Boolean {
        return this.parentCommentId == this.id
    }

    fun deleted() {
        this.deleted = true
    }

    override fun toString(): String {
        return "Comment(id=$id, content='$content', parentCommentId=$parentCommentId, articleId=$articleId, writerId=$writerId, deleted=$deleted)"
    }

    companion object {

        @JvmStatic
        fun create(
            id: Long,
            content: String,
            parentCommentId: Long?,
            articleId: Long,
            writerId: Long,
        ): Comment {
            return Comment(
                id = id,
                content = content,
                parentCommentId = parentCommentId,
                articleId = articleId,
                writerId = writerId,
                deleted = false
            )
        }
    }

}