package com.hin.flixcomix.utils.extensions

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.maskEmail(): String {
    val parts = this.split("@")
    if (parts.size != 2) return this

    val name = parts[0]
    val domain = parts[1]

    return if (name.length <= 2) {
        // Nếu tên quá ngắn thì chỉ giữ ký tự đầu
        name.first() + "*".repeat(name.length - 1) + "@" + domain
    } else {
        val first = name.first()
        val last = name.last()
        val stars = "*".repeat(name.length - 2)
        "$first$stars$last@$domain"
    }
}
