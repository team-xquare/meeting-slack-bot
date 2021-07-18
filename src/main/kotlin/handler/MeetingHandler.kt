package handler

import com.mongodb.client.MongoCollection
import com.slack.api.bolt.context.builtin.ActionContext
import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.context.builtin.ViewSubmissionContext
import com.slack.api.bolt.request.builtin.BlockActionRequest
import com.slack.api.bolt.request.builtin.SlashCommandRequest
import com.slack.api.bolt.request.builtin.ViewSubmissionRequest
import com.slack.api.bolt.response.Response
import com.slack.api.bolt.response.ResponseTypes
import com.slack.api.model.block.ActionsBlock
import com.slack.api.model.block.element.ButtonElement
import model.Meeting
import org.litote.kmongo.*
import sender.dto.DenyMeeting
import sender.dto.SenderMeeting
import sender.exception.MeetingNotFoundException
import sender.notification.DeleteMeetingSender
import sender.notification.DenySender
import sender.notification.NotificationSender
import view.modal.buildDeleteModal
import view.modal.buildDenyModal
import view.modal.buildScheduleModal
import java.util.*

/**
 * Handle requests for adding or deleting meeting schedules
 * @author smoothbear
 */
class MeetingHandler(
    private val nfSender: NotificationSender,
    private val denySender: DenySender,
    private val deleteSender: DeleteMeetingSender,
    private val col: MongoCollection<Meeting>,
    private val channel: String
) {
    /**
     * Handle request for adding schedules
     * @return Response
     */
    fun addSchedule(request: SlashCommandRequest, ctx: SlashCommandContext): Response {
        val modal = buildScheduleModal()

        val response = ctx.client().viewsOpen { it
            .triggerId(ctx.triggerId)
            .view(modal)
        }

        return if (response.isOk) ctx.ack()
        else Response.builder().statusCode(500).body(response.error).build()
    }

    /**
     * Handle adding request from modal
     * @return Response
     */
    fun handleViewSchedule(request: ViewSubmissionRequest, ctx: ViewSubmissionContext): Response {

        val value = request.payload.view.state.values

        val meeting = SenderMeeting(
            agenda = value["meeting-agenda-input-block"]?.get("meeting-agenda")?.value ?: "",
            attender = value["meeting-attender-input-block"]?.get("attender-picker")?.selectedUsers ?: listOf(),
            description = value["meeting-block"]?.get("meeting-description")?.value ?: "",
            date = value["meeting-date-input-block"]?.get("date-picker")?.selectedDate ?: "",
            hour = value["meeting-time-hour-input-block"]?.get("hour-picker")?.selectedOption?.text?.text ?: "",
            minute = value["meeting-time-minute-input-block"]?.get("minute-picker")?.selectedOption?.text?.text ?: ""
        )

        val colId = UUID.randomUUID()

        col.insertOne(
            Meeting(
                meetingId = colId.toString(),
                agenda = meeting.agenda,
                date = meeting.date,
                time = meeting.hour + " " + meeting.minute,
                attenders = meeting.attender,
                approves = setOf(),
                denys = setOf(),
                denyReason = hashMapOf()
            )
        )

        meeting.initialId(colId.toString())

        nfSender.sendNotification(meeting)

        return ctx.ack()
    }

    /**
     * Handle approve meeting from notification message
     *
     * If the user is not a participant in the meeting, the request is denied.
     * @return Response
     */
    fun handleApproveAction(request: BlockActionRequest, ctx: ActionContext): Response {
        val userId = request.payload.user.id

        val value = extractValue(request, ctx)

        val result: Meeting? = col.findOne(Meeting::meetingId eq value)

        result ?: throw MeetingNotFoundException()

        if (!result.attenders.contains(userId)) {
            ctx.respond { it
                .responseType(ResponseTypes.ephemeral)
                .text("⚠️ 회의 멤버에 존재하지 않습니다.")
            }

            return ctx.ack()
        }

        if (result.denys.contains(userId)) {
            val deny = result.denys.minus(userId)

            col.updateOne(result::meetingId eq value, Meeting::denys setTo deny)
        }

        val approve = result.approves.plus(userId)

        col.updateOne(result::meetingId eq value, Meeting::approves setTo approve)

        ctx.respond { it
            .responseType(ResponseTypes.ephemeral)
            .text("✅ 회의 참여 신청이 성공적으로 완료되었습니다.")
        }

        return ctx.ack()
    }

    /**
     * Handle deny meeting from notification message
     *
     * If the user is not a participant in the meeting, the request is denied.
     * @return Response
     */
    fun handleDenyAction(request: BlockActionRequest, ctx: ActionContext): Response {
        val userId = request.payload.user.id

        val value = extractValue(request, ctx)

        val result: Meeting? = col.findOne(Meeting::meetingId eq value)

        result ?: throw MeetingNotFoundException()

        if (!result.attenders.contains(userId)) {
            ctx.respond { it
                .responseType(ResponseTypes.ephemeral)
                .text("⚠️ 회의 멤버에 존재하지 않습니다.")
            }

            return ctx.ack()
        }

        ctx.client().viewsOpen { it
            .triggerId(ctx.triggerId)
            .view(buildDenyModal(value))
        }

        return ctx.ack()
    }

    /**
     * Handle deny request from deny modal
     *
     * @return Response
     */
    fun handleDenyView(request: ViewSubmissionRequest, ctx: ViewSubmissionContext): Response {

        val userId = request.payload.user.id

        val value = request.payload.view.privateMetadata
        val values = request.payload.view.state.values

        val reason = values["meeting-deny-reason-input-block"]!!["meeting-deny-reason"]!!.value

        val result: Meeting? = col.findOne(Meeting::meetingId eq value)

        result ?: throw MeetingNotFoundException()

        if (result.approves.contains(userId)) {
            val approves = result.approves.minus(userId)

            col.updateOne(result::meetingId eq value, Meeting::approves setTo approves)
        }

        val denys = result.denys.plus(userId)
        result.denyReason[userId] = reason

        col.updateOne(result::meetingId eq value, Meeting::denys setTo denys)
        col.updateOne(result::meetingId eq value, Meeting::denyReason setTo result.denyReason)

        denySender.sendNotification(
            DenyMeeting(
                userId, channel, reason
            )
        )

        return ctx.ack()
    }

    fun deleteMeeting(request: SlashCommandRequest, ctx: SlashCommandContext): Response {
        val date = request.payload.text

        val results = col.find(Meeting::date eq date)

        ctx.client().viewsOpen { view -> view
            .triggerId(ctx.triggerId)
            .view(buildDeleteModal(results))
        }

        return ctx.ack()
    }

    fun handleDeleteMeetingView(request: ViewSubmissionRequest, ctx: ViewSubmissionContext): Response {
        val meetingId = request.payload.view.state.values["delete-meeting-input-block"]!!["select-meeting"]!!.selectedOption.value

        val result = col.findOneAndDelete(Meeting::meetingId eq meetingId) ?: throw MeetingNotFoundException()

        deleteSender.sendNotification(result)

        return ctx.ack()
    }

    /**
     * Extract button value(meetingId) from BlockActionRequest.
     *
     * @return String
     */
    private fun extractValue(request: BlockActionRequest, ctx: ActionContext): String {
        val actionBlock = request.payload.message.attachments[0].blocks[6] as ActionsBlock
        val button = actionBlock.elements[0] as ButtonElement

        return button.value
    }

//    /**
//     * Getting Meeting List by agenda
//     */
//    private fun getMeeting(request: SlashCommandRequest, ctx: ActionContext): String {
//        val agenda = request.payload.text
//
//    }
}