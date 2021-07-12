package model

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class Meeting (
    @BsonId
    val id: UUID = UUID.randomUUID(),
    val date: String,
    val time: String,
    val attenders: List<String>,
    val approves: List<String>,
    val denys: List<String>
)