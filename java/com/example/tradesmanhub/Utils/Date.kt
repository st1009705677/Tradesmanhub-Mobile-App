package com.example.tradesmanhub.Utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Date {
    fun getCurrentDateInSAST(): String {
        val sastZoneId = ZoneId.of("Africa/Johannesburg")
        val currentDateTime = LocalDateTime.now(sastZoneId)

        // format the date
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDateTime.format(formatter)
    }
}