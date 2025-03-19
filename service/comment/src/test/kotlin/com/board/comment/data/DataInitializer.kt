package com.board.comment.data

import com.board.comment.domain.Comment
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
class DataInitializer {

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
            executorService.submit {
                insert()
                latch.countDown()
                println("latch count: ${latch.count}")
            }
        }

        latch.await()
        executorService.shutdown()
    }

    fun insert() {
        transactionTemplate.executeWithoutResult {
            var prev: Comment? = null

            repeat(BULK_INSERT_SIZE) { i ->
                val comment = Comment.create(
                    id = snowflake.nextId(),
                    content = "내용 : $i",
                    parentCommentId = if (i % 2 == 0) null else prev?.id,
                    articleId = 1L,
                    writerId = 1L,
                ).also { prev = it }

                entityManager.persist(comment)
            }
        }
    }

    companion object {
        private const val EXECUTE_COUNT = 6000
        const val BULK_INSERT_SIZE = 2000
    }

}