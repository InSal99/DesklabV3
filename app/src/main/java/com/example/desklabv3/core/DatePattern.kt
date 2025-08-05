package com.example.desklabv3.core

enum class DatePattern {
    WithDayFullMonth {
        override fun toString() = "EEEE, dd MMMM yyyy"
    },
    FullMonth {
        override fun toString() = "dd MMMM yyyy"
    },
    ShortMonth {
        override fun toString() = "dd MMM yyyy"
    }
}