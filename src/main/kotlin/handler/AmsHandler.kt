package handler

import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.context.builtin.ViewSubmissionContext
import com.slack.api.bolt.request.builtin.SlashCommandRequest
import com.slack.api.bolt.request.builtin.ViewSubmissionRequest
import com.slack.api.bolt.response.Response
import com.slack.api.bolt.response.ResponseTypes
import view.block.buildNotifyScheduleBlock
import view.modal.buildScheduleModal
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

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
     * Handle adding request from modal
     * @return Response
     */
    fun handleViewSchedule(request: ViewSubmissionRequest, ctx: ViewSubmissionContext): Response {

        val values = request.payload.view.state.values

        ctx.respond { res -> res
            .responseType(ResponseTypes.inChannel)
            .blocks(buildNotifyScheduleBlock(values))
        }

        return ctx.ack()
    }
}