package com.lafierage.test.nfc

import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertIdTagTest {

    @Test
    fun `convert negative value to hex format`() {
        val number = -32

        val result = if(number < 0) 255 + number + 1 else number

        assertEquals(0xE0, result)
    }

    @Test
    fun `convert a tag ID to its hex format`() {
        val id = byteArrayOf(97, 70, 3, -26, 80, 1, 4, -32)
        val excepted = "61 46 03 E6 50 01 04 E0"
        val result = id.toHex().uppercase()

        assertEquals(excepted, result)
    }
}