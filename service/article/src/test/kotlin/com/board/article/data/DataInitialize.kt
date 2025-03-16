package com.board.article.data

import com.board.article.domain.Article
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
class DataInitialize {

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
        transactionTemplate.executeWithoutResult { status ->
            for (i in 0 until BULK_INSERT_SIZE) {
                val article = Article(
                    id = snowflake.nextId(),
                    title = "제목 : $i",
                    content = "내용 : $i",
                    boardId = 1L,
                    writeId = 1L,
                )

                entityManager.persist(article)
            }
        }
    }

    companion object {
        const val BULK_INSERT_SIZE = 2000
        const val EXECUTE_COUNT = 6000
    }

}
