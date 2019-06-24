package com.intworkers.application.handler

import com.intworkers.application.exception.ValidationError
import java.text.SimpleDateFormat
import java.util.*

class ErrorDetail {
    var title: String? = null
    var status: Int = 0
    var detail: String? = null
    var timestamp: String? = null
        private set
    var developerMessage: String? = null
    var errors: Map<String, List<ValidationError>> = HashMap()

    fun setTimestamp(timestamp: Long?) {
        this.timestamp = SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(Date(timestamp!!))
    }
}