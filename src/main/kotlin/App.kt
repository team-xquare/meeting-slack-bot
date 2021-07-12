import com.slack.api.bolt.App
import com.slack.api.bolt.socket_mode.SocketModeApp
import handler.AmsHandler
import sender.notification.NotificationMessageSender

fun main() {
    val app = App()
    val notifySender = NotificationMessageSender()
    val amsHandler = AmsHandler(notifySender)

    app.command("/meeting", amsHandler::addSchedule)
    app.viewSubmission("add-schedule", amsHandler::handleViewSchedule)
    app.viewClosed("add-schedule") { _, ctx ->
        ctx.ack()
    }

    SocketModeApp(app).start()
}