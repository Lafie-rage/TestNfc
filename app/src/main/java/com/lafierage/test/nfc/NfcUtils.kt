package com.lafierage.test.nfc

fun ByteArray.toHex(): String {
    val sb = StringBuilder()
    for (i in indices) {
        val b: Int = if (this[i].toInt() < 0) 255 + this[i] + 1 else this[i].toInt()
        if (b < 0x10) sb.append('0')
        sb.append(Integer.toHexString(b))
        if (i < indices.last) {
            sb.append(" ")
        }
    }
    return sb.toString()
}

