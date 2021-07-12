package sender.dto

class SenderMeeting (
    val agenda: String,
    val attender: List<String>,
    val description: String,
    val date: String,
    val hour: String,
    val minute: String
) {
    lateinit var id: String

    fun initialId(id: String) {
        this.id = id
    }
}