package handler

import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.context.builtin.ViewSubmissionContext
import com.slack.api.bolt.request.builtin.SlashCommandRequest
import com.slack.api.bolt.request.builtin.ViewSubmissionRequest
import com.slack.api.bolt.response.Response
import view.modal.buildScheduleModal

/**
 * Handle requests for adding or deleting meeting schedules
 * @author smoothbear
 */
class AmsHandler {
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
     * Handle adding request from view
     * @return Response
     */
    fun handleViewSchedule(request: ViewSubmissionRequest, ctx: ViewSubmissionContext): Response {
        return ctx.ack()
    }
}