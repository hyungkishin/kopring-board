package com.board.article.application.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class PageLimitCalculatorKtTest {

    @ParameterizedTest
    @CsvSource(
        "1, 30, 10, 301",
        "7, 30, 10, 301",
        "10, 30, 10, 301",
        "11, 30, 10, 601",
        "12, 30, 10, 601"
    )
    fun `페이지 limit`(page: Long, pageSize: Long, movablePageCount: Long, expected: Long) {
        // given & when
        val result = calculatePageLimit(page, pageSize, movablePageCount)

        // then
        assertEquals(expected, result)
    }

}