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
        val meetings = col.find(Meeting::date eq request.payload.text)
        val schedules = meetings.map { meeting -> GetScheduleDto(date = meeting.date, time = meeting.time, approves = meeting.approves, denys = meeting.denys) }

        ctx.respond { respond -> respond
            .responseType(ResponseTypes.ephemeral)
            .text("회의 목록과 참여 여부")
            .attachments(buildAttenderScheduleListBlock(schedules))
        }

        return ctx.ack()
    }
}