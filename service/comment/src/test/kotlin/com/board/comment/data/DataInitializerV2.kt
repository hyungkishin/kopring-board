package com.board.comment.data

import com.board.comment.domain.CommentPath
import com.board.comment.domain.CommentV2
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kuke.board.common.snowflake.Snowflake
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class DataInitializerV2 {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    private val snowflake = Snowflake()

    private val latch = CountDownLatch(EXECUTE_COUNT)

    @Test
    fun initialize() {
        val executorService = Executors.newFixedThreadPool(10)
        for (i in 0 until EXECUTE_COUNT) {
            val start = i * BULK_INSERT_SIZE
            val end = (i + 1) * BULK_INSERT_SIZE

            executorService.submit {
                insert(start, end)
                latch.countDown()
                println("latch count: ${latch.count}")
            }

        }

        latch.await()
        executorService.shutdown()
    }

    fun insert(start: Int, end: Int) {
        transactionTemplate.executeWithoutResult {
            var prev: CommentV2? = null
            for (i in start until end) {
                val comment = CommentV2.create(
                    id = snowflake.nextId(),
                    content = "내용 : $i",
                    articleId = 1L,
                    writerId = 1L,
                    commentPath = toPath(i),
                )
                prev = comment
                entityManager.persist(comment)
            }
        }
    }

    private fun toPath(value: Int): CommentPath {
        var path = ""
        var currentValue = value

        for (i in 0 until DEPTH_CHUNK_SIZE) {
            path = CHARSET[currentValue % CHARSET.length] + path
            currentValue /= CHARSET.length
        }

        return CommentPath.create(path)
    }

    companion object {
        private const val EXECUTE_COUNT = 6000

        private const val BULK_INSERT_SIZE = 2000

        private const val CHARSET: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

        private const val DEPTH_CHUNK_SIZE: Int = 5

    }

}