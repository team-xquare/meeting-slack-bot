package handler

import com.mongodb.client.MongoCollection
import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.request.builtin.SlashCommandRequest
import com.slack.api.bolt.response.Response
import com.slack.api.bolt.response.ResponseTypes
import handler.dto.GetScheduleDto
import model.Meeting
import org.litote.kmongo.eq
import view.block.buildAttenderScheduleListBlock

class AttenderHandler (
    private val col: MongoCollection<Meeting>
) {
    fun getSchedule(request: SlashCommandRequest, ctx: SlashCommandContext): Response {
        val meetings = if (request.payload.text.length == 10) col.find(Meeting::date eq request.payload.text) else col.find()
        val schedules = meetings.map { meeting -> GetScheduleDto(agenda = meeting.agenda, date = meeting.date, time = meeting.time, approves = meeting.approves, denys = meeting.denys) }

        val blocks = buildAttenderScheduleListBlock(schedules)

        ctx.respond { it
            .responseType(ResponseTypes.ephemeral)
            .text("회의 참석자 및 불참자")
            .attachments(blocks)
        }

        return ctx.ack()
    }
}