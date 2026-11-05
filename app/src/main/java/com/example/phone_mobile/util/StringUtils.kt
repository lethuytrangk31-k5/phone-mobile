package com.example.phone_mobile.util

import java.text.Normalizer
import java.util.regex.Pattern

/**
 * Loại bỏ dấu tiếng Việt để phục vụ tìm kiếm không dấu (VD: "Nguyễn" -> "Nguyen", "Đặng" -> "Dang")
 */
fun String.removeVietnameseDiacritics(): String {
    val nfd = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(nfd)
        .replaceAll("")
        .replace("đ", "d")
        .replace("Đ", "D")
}
