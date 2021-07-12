package model

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class Meeting (
    @BsonId
    val id: UUID = UUID.randomUUID(),
    val meetingId: String,
    val date: String,
    val time: String,
    val attenders: List<String>,
    val approves: Set<String>,
    val denys: Set<String>
)