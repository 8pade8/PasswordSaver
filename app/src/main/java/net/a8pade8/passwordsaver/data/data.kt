package net.a8pade8.passwordsaver.data

import androidx.databinding.BaseObservable

data class Record(val id: Long, var resourceName: String, var login: String, var password: String,
                  var comment: String, var favorite: Boolean = false): BaseObservable() {

    override fun toString(): String {
        return "$resourceName $login"
    }
}