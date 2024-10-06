package com.bez.recipebiometrics.utils

import java.util.Base64

object EncryptionHelper {

    fun encrypt(text: String): String {
        // Simple encryption logic (for demonstration purposes)
        return Base64.getEncoder().encodeToString(text.toByteArray())
    }

    fun decrypt(encryptedText: String): String {
        // Simple decryption logic (for demonstration purposes)
        return String(Base64.getDecoder().decode(encryptedText))
    }
}
