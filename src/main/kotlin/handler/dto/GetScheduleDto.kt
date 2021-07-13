package handler.dto

class GetScheduleDto (
    val date: String,
    val time: String,
    val approves: Set<String>,
    val denys: Set<String>
)