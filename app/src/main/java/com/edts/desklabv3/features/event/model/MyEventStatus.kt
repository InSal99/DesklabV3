package com.edts.desklabv3.features.event.model

enum class MyEventStatus {
    BERLANGSUNG,
    TERDAFTAR,
    HADIR,
    TIDAK_HADIR;

    companion object {
        fun fromString(value: String): MyEventStatus? {
            return try {
                valueOf(value.uppercase().replace(" ", "_"))
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}