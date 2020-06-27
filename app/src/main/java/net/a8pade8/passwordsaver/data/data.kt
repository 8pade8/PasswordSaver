package net.a8pade8.passwordsaver.data

data class Record(val id: Int, val resourceName: String, val login: String, val password: String) {
    override fun toString(): String {
        return "$resourceName $login"
    }
}