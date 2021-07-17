package handler.dto

class GetScheduleDto (
    val agenda: String,
    val date: String,
    val time: String,
    val approves: Set<String>,
    val denys: Set<String>,
    val denyReason: MutableMap<String, String>
)