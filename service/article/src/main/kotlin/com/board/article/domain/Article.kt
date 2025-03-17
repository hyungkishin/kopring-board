package com.board.article.domain

import com.board.article.domain.base.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "articles")
class Article(

    @Id
    @Column(name = "article_id")
    val id: Long? = null,

    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "board_id")
    val boardId: Long? = null, // shard key

    @Column(name = "writer_id")
    val writerId: Long? = null,

    ) : BaseTimeEntity() {

    fun update(incomingArticle: Article) {
        this.title = incomingArticle.title
        this.content = incomingArticle.content
    }

    override fun toString(): String {
        return "Article(id=$id, title='$title', content='$content', boardId=$boardId, writerId=$writerId)"
    }

}