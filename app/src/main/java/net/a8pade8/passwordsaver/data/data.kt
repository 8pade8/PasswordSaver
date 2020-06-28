package net.a8pade8.passwordsaver.data

data class Record(val id: Long, var resourceName: String, var login: String, var password: String) {
    override fun toString(): String {
        return "$resourceName $login"
    }

    fun toMap(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map.let {
            it["id"] = id.toString()
            it["resourceName"] = resourceName
            it["login"] = login
            it["password"] = password}
        return map
    }
}