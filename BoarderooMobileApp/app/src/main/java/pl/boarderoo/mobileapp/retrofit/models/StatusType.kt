package pl.boarderoo.mobileapp.retrofit.models

enum class StatusType
{
    ORDERED,
    PAID,
    CONFIRMED,
    CANCELLED,
    FINISHED
}

fun intToEnum(value: Int): StatusType? {
    return StatusType.entries.getOrNull(value)
}