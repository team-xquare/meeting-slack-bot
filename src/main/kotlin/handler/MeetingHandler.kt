package handler

import com.slack.api.bolt.context.builtin.ActionContext
import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.context.builtin.ViewSubmissionContext
import com.slack.api.bolt.request.builtin.BlockActionRequest
import com.slack.api.bolt.request.builtin.SlashCommandRequest
import com.slack.api.bolt.request.builtin.ViewSubmissionRequest
import com.slack.api.bolt.response.Response
import sender.dto.Meeting
import sender.notification.NotificationSender
import view.modal.buildScheduleModal

/**
 * Handle requests for adding or deleting meeting schedules
 * @author smoothbear
 */
class MeetingHandler(
    private val nfSender: NotificationSender
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

        val meeting = Meeting(
            agenda = value["meeting-agenda-input-block"]?.get("meeting-agenda")?.value ?: "",
            attender = value["meeting-attender-input-block"]?.get("attender-picker")?.selectedUsers ?: listOf(),
            description = value["meeting-block"]?.get("meeting-description")?.value ?: "",
            date = value["meeting-date-input-block"]?.get("date-picker")?.selectedDate ?: "",
            hour = value["meeting-time-hour-input-block"]?.get("hour-picker")?.selectedOption?.text?.text ?: "",
            minute = value["meeting-time-minute-input-block"]?.get("minute-picker")?.selectedOption?.text?.text ?: ""
        )

        nfSender.sendNotification(meeting)

        return ctx.ack()
    }

    /**
     * Handle approve meeting from notification message
     * @return Response
     */
    fun handleApproveAction(request: BlockActionRequest, ctx: ActionContext): Response {
        // TODO
    }

    /**
     * Handle deny meeting from notification message
     */
    fun handleDenyAction(request: BlockActionRequest, ctx: ActionContext): Response {
        // TODO
    }
}