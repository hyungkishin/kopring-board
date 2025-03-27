package com.board.comment.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class CommentPath(

    @Column(name = "path")
    val path: String,
) {

    fun getDepth(): Int {
        return calDepth(path)
    }

    fun isRoot(): Boolean {
        return calDepth(path) == 1
    }

    fun getParendPath(): String {
        return path.substring(0, path.length - DEPTH_CHUNK_SIZE)
    }

    fun createChildCommentPath(descendantsTopPath: String?): CommentPath {
        if (descendantsTopPath == null) {
            return create(path + MIN_CHUNK)
        }

        val childrenTopPath = findChildrenTopPath(descendantsTopPath)
        return create(increase(childrenTopPath))
    }

    private fun findChildrenTopPath(descendantsTopPath: String): String {
        return descendantsTopPath.substring(0, (getDepth() + 1) * DEPTH_CHUNK_SIZE)
    }

    private fun increase(path: String): String {
        val lastChunk = path.substring(path.length - DEPTH_CHUNK_SIZE)
        if (isChunkOverFlowed(lastChunk)) {
            throw IllegalArgumentException()
        }

        val charsetLength = CHARSET.length

        var value = 0
        for (ch in lastChunk.toCharArray()) {
            value = value * charsetLength + CHARSET.indexOf(ch)
        }

        value += 1

        var result = ""

        for (i in 0 until DEPTH_CHUNK_SIZE) {
            result = CHARSET[value % charsetLength] + result
            value /= charsetLength
        }

        return path.substring(0, path.length - DEPTH_CHUNK_SIZE) + result
    }

    private fun isChunkOverFlowed(lastChunk: String): Boolean {
        return MAX_CHUNK == lastChunk
    }

    companion object {

        private const val CHARSET: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

        private const val DEPTH_CHUNK_SIZE: Int = 5

        private const val MAX_DEPTH: Int = 5

        // MIN_CHUNK = "00000",  MAX_CHUNK = "zzzzz"
        private val MIN_CHUNK: String = CHARSET[0].toString().repeat(DEPTH_CHUNK_SIZE)

        private val MAX_CHUNK: String = CHARSET[CHARSET.length - 1].toString().repeat(DEPTH_CHUNK_SIZE)

        fun create(path: String): CommentPath {
            if (isDepthOverflowed(path)) {
                throw IllegalArgumentException("depth overflowed")
            }

            return CommentPath(
                path = path
            )
        }

        private fun isDepthOverflowed(path: String): Boolean {
            return calDepth(path) > MAX_DEPTH
        }

        private fun calDepth(path: String): Int {
            return path.length / DEPTH_CHUNK_SIZE
        }

    }

}