package net.a8pade8.passwordsaver.data

import androidx.databinding.BaseObservable

data class Record(val id: Long, var resourceName: String, var login: String, var password: String): BaseObservable() {

    override fun toString(): String {
        return "$resourceName $login"
    }
}