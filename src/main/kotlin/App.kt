import com.slack.api.bolt.App
import com.slack.api.bolt.socket_mode.SocketModeApp
import handler.MeetingHandler
import sender.notification.NotificationMessageSender

fun main() {
    val app = App()
    val notifySender = NotificationMessageSender()
    val meetingHandler = MeetingHandler(notifySender)

    // Commands
    app.command("/meeting", meetingHandler::addSchedule)

    // Modal
    app.viewSubmission("add-schedule", meetingHandler::handleViewSchedule)
    app.viewClosed("add-schedule") { _, ctx ->
        ctx.ack()
    }

    // Actions
    app.blockAction("approve-meeting", meetingHandler::handleApproveAction)
    app.blockAction("deny-meeting", meetingHandler::handleDenyAction)

    SocketModeApp(app).start()
}