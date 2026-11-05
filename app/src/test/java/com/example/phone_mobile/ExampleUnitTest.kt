package com.example.phone_mobile

import com.example.phone_mobile.data.model.sampleContacts
import com.example.phone_mobile.util.removeVietnameseDiacritics
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testRemoveVietnameseDiacritics() {
        assertEquals("Nguyen Van A", "Nguyễn Văn A".removeVietnameseDiacritics())
        assertEquals("Dang Van C", "Đặng Văn C".removeVietnameseDiacritics())
        assertEquals("Le Thuy D", "Lê Thùy D".removeVietnameseDiacritics())
        assertEquals("Do Minh Hai", "Đỗ Minh Hải".removeVietnameseDiacritics())
    }

    @Test
    fun testUnaccentedSearch() {
        fun searchContacts(query: String) = sampleContacts.filter { contact ->
            val normalizedQuery = query.trim().removeVietnameseDiacritics().lowercase()
            val normalizedName = contact.name.removeVietnameseDiacritics().lowercase()
            normalizedName.contains(normalizedQuery) || contact.phoneNumber.contains(normalizedQuery)
        }

        val resultNguyen = searchContacts("nguyen")
        assertTrue(resultNguyen.any { it.name == "Nguyễn Văn A" })
        assertTrue(resultNguyen.any { it.name == "Nguyễn Ngọc B" })

        val resultDang = searchContacts("dang")
        assertTrue(resultDang.any { it.name == "Đặng Văn C" })

        val resultLe = searchContacts("le")
        assertTrue(resultLe.any { it.name == "Lê Thị B" })
        assertTrue(resultLe.any { it.name == "Lê Thùy D" })

        val resultHai = searchContacts("hai")
        assertTrue(resultHai.any { it.name == "Đỗ Minh Hải" })
    }

    @Test
    fun testDuplicatePhoneValidation() {
        fun isPhoneDuplicate(newPhone: String, currentContactId: String? = null): Boolean {
            val clean = newPhone.trim().replace(" ", "").replace("-", "")
            return sampleContacts.any { existing ->
                existing.id != currentContactId &&
                        existing.phoneNumber.trim().replace(" ", "").replace("-", "") == clean
            }
        }

        // Số "0123456789" thuộc về Nguyễn Văn A (id = "1")
        assertTrue("Trùng số khi thêm mới", isPhoneDuplicate("0123456789", null))
        assertTrue("Trùng số dù có khoảng trắng", isPhoneDuplicate("0123 456 789", null))
        assertFalse("Không báo trùng khi sửa chính liên hệ đó", isPhoneDuplicate("0123456789", "1"))
        assertFalse("Số mới hoàn toàn không bị trùng", isPhoneDuplicate("0999888777", null))
    }
}
