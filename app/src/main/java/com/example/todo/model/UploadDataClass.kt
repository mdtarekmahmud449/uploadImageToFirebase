package com.example.todo.model

import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone

data class UploadDataClass(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val imageUrl: String? = null
)
