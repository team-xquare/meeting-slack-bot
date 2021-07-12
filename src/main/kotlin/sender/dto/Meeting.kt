package sender.dto

class Meeting (
    val agenda: String,
    val attender: List<String>,
    val description: String,
    val date: String,
    val hour: String,
    val minute: String
)